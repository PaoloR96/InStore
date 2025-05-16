#!/bin/bash

# ====================================================
# ANALISI PERIODICA DEI LOG IPTABLES
# ====================================================

LOG_FILE="/var/log/syslog"
REPORT_FILE="/var/log/iptables-monitor-report.log"
DATE_NOW=$(date '+%F %T')

# Crea i file se non esistono
for FILE in "$LOG_FILE" "$REPORT_FILE"; do
  if [ ! -f "$FILE" ]; then
    echo "📁 Creazione file mancante: $FILE"
    touch "$FILE"
    chmod 640 "$FILE"
    chown syslog:adm "$FILE" 2>/dev/null || true
  fi
done

echo "📊 Analisi log IPTABLES - $DATE_NOW" >> "$REPORT_FILE"
echo "-------------------------------------------------" >> "$REPORT_FILE"

# Conteggi base
grep "BLOCKED:" "$LOG_FILE" | wc -l | xargs -I{} echo "❌ Pacchetti bloccati: {}" >> "$REPORT_FILE"
grep "ACCEPTED_HTTP:" "$LOG_FILE" | wc -l | xargs -I{} echo "🌐 Accessi HTTP accettati: {}" >> "$REPORT_FILE"
grep "ACCEPTED_HTTPS:" "$LOG_FILE" | wc -l | xargs -I{} echo "🔐 Accessi HTTPS accettati: {}" >> "$REPORT_FILE"
grep "LIMIT_HTTP:" "$LOG_FILE" | wc -l | xargs -I{} echo "🚫 Tentativi HTTP limitati (DoS): {}" >> "$REPORT_FILE"
grep "LIMIT_HTTPS:" "$LOG_FILE" | wc -l | xargs -I{} echo "🚫 Tentativi HTTPS limitati (DoS): {}" >> "$REPORT_FILE"

# Log tra servizi
echo "🔁 Traffico tra servizi:" >> "$REPORT_FILE"
grep "SERVICE:" "$LOG_FILE" | awk '{print $NF}' | sort | uniq -c | sort -nr >> "$REPORT_FILE"

echo "✅ Report aggiornato: $REPORT_FILE"
