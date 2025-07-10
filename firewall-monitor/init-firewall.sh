#!/bin/bash

# ============================================================
# SCRIPT DI CONFIGURAZIONE FIREWALL PER AMBIENTE DOCKER
# CON LOGGING AVANZATO
# ============================================================

if [ "$EUID" -ne 0 ]; then
  echo "❌ Questo script deve essere eseguito come root."
  exit 1
fi

echo "🔥 Configurazione firewall in corso..."

iptables-save > /root/iptables-backup-$(date +%F_%T).rules
echo "📦 Backup regole attuali salvato in /root/iptables-backup-..."

sysctl -w net.ipv4.ip_forward=1
echo "net.ipv4.ip_forward=1" | tee -a /etc/sysctl.conf

iptables -F
iptables -t nat -F
iptables -X
iptables -t nat -X
iptables -Z
iptables -X LOGGING 2>/dev/null

iptables -P INPUT DROP
iptables -P FORWARD DROP
iptables -P OUTPUT ACCEPT

# 🔁 Catena logging bloccati
iptables -N LOGGING
iptables -A LOGGING -j LOG --log-prefix "BLOCKED: " --log-level 4 --log-tcp-options --log-ip-options --log-uid
iptables -A LOGGING -j DROP

# 🔧 Regole di base
iptables -A INPUT -i lo -j ACCEPT
iptables -A OUTPUT -o lo -j ACCEPT
iptables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT

# 🌐 Accesso esterno con log accettati
iptables -A INPUT -p tcp --dport 80 -j LOG --log-prefix "ACCEPTED_HTTP: " --log-level 4
iptables -A INPUT -p tcp --dport 80 -j ACCEPT
iptables -A INPUT -p tcp --dport 443 -j LOG --log-prefix "ACCEPTED_HTTPS: " --log-level 4
iptables -A INPUT -p tcp --dport 443 -j ACCEPT
iptables -A INPUT -p icmp --icmp-type echo-request -j ACCEPT

# 🔌 Ottieni nome interfaccia bridge
INSTORE_BR="br-$(docker network inspect -f '{{.Id}}' instore-network | cut -c1-12)"

if [ -z "$INSTORE_BR" ]; then
  echo "❌ Errore: l'interfaccia bridge Docker 'instore-network' non è stata trovata."
  exit 1
fi

echo "🔹 Interfaccia bridge rilevata: $INSTORE_BR"

iptables -A FORWARD -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT

# ============================================================
# TRAFFICO TRA SERVIZI CON LOG
# ============================================================

# Nginx → Application Core
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.40 -p tcp --dport 8080 -j LOG --log-prefix "SERVICE:NGINX→APPCORE " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.40 -p tcp --dport 8080 -j ACCEPT

# Nginx → Keycloak
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.20 -p tcp --dport 8443 -j LOG --log-prefix "SERVICE:NGINX→KEYCLOAK " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.20 -p tcp --dport 8443 -j ACCEPT

# Keycloak → Application Core
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.20 -d 172.28.0.40 -p tcp -j LOG --log-prefix "SERVICE:KEYCLOAK→APPCORE " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.20 -d 172.28.0.40 -p tcp -j ACCEPT

# Application Core → Vault
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.40 -d 172.28.0.30 -p tcp --dport 8200 -j LOG --log-prefix "SERVICE:APPCORE→VAULT " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.40 -d 172.28.0.30 -p tcp --dport 8200 -j ACCEPT

# Vault-init → Vault
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.60 -d 172.28.0.30 -p tcp --dport 8200 -j LOG --log-prefix "SERVICE:VAULTINIT→VAULT " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.60 -d 172.28.0.30 -p tcp --dport 8200 -j ACCEPT

# Application Core → MySQL
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.40 -d 172.28.0.10 -p tcp --dport 3306 -j LOG --log-prefix "SERVICE:APPCORE→MYSQL " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.40 -d 172.28.0.10 -p tcp --dport 3306 -j ACCEPT

# Vault → MySQL
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.30 -d 172.28.0.10 -p tcp --dport 3306 -j LOG --log-prefix "SERVICE:VAULT→MYSQL " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.30 -d 172.28.0.10 -p tcp --dport 3306 -j ACCEPT

# Keycloak → MySQL
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.20 -d 172.28.0.10 -p tcp --dport 3306 -j LOG --log-prefix "SERVICE:KEYCLOAK→MYSQL " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.20 -d 172.28.0.10 -p tcp --dport 3306 -j ACCEPT

# 🔒 Blocca accesso diretto a MySQL da Nginx
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.10 -j LOG --log-prefix "BLOCKED:NGINX→MYSQL " --log-level 4
iptables -A FORWARD -i $INSTORE_BR -o $INSTORE_BR -s 172.28.0.50 -d 172.28.0.10 -j LOGGING

## 🛡️ Protezione DoS base con log
#iptables -A INPUT -p tcp --dport 80 -m limit --limit 25/minute --limit-burst 100 -j LOG --log-prefix "LIMIT_HTTP: " --log-level 4
#iptables -A INPUT -p tcp --dport 80 -m limit --limit 25/minute --limit-burst 100 -j ACCEPT
#iptables -A INPUT -p tcp --dport 443 -m limit --limit 25/minute --limit-burst 100 -j LOG --log-prefix "LIMIT_HTTPS: " --log-level 4
#iptables -A INPUT -p tcp --dport 443 -m limit --limit 25/minute --limit-burst 100 -j ACCEPT

# 📝 Logging di default per pacchetti non gestiti
iptables -A FORWARD -j LOGGING
iptables -A INPUT -j LOGGING

# 💾 Salva configurazione
if command -v iptables-save >/dev/null 2>&1; then
  if [ -d "/etc/iptables" ]; then
    iptables-save > /etc/iptables/rules.v4
    echo "✅ Regole salvate in /etc/iptables/rules.v4"
  else
    iptables-save > /etc/iptables.rules
    echo "✅ Regole salvate in /etc/iptables.rules"
    echo "Per rendere le regole permanenti: systemctl enable iptables.service"
  fi
else
  echo "⚠️ iptables-persistent non trovato. Le regole non persisteranno dopo il riavvio."
  echo "Installare iptables-persistent con: apt-get install iptables-persistent"
fi

echo "✅ Configurazione completata!"
iptables -L -v -n --line-numbers
