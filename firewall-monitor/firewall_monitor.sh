#!/bin/bash

# ============================================================
# SCRIPT DI MONITORAGGIO TRAFFICO REAL-TIME DOCKER - OTTIMIZZATO
# AUTO-START - CATTURA DETTAGLIATA DEL TRAFFICO
# ============================================================

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configurazione logging
LOG_FILE="/var/log/docker-traffic-$(date +%Y%m%d-%H%M%S).log"
VERBOSE_MODE=true
DETAILED_ANALYSIS=true
SHOW_PAYLOAD=true

# Verifica privilegi root
if [ "$EUID" -ne 0 ]; then
    echo -e "${RED}❌ Questo script deve essere eseguito come root.${NC}"
    exit 1
fi

# Verifica dipendenze
for cmd in tcpdump docker ntpdate; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
        echo -e "${RED}❌ $cmd non installato.${NC}"
        case "$cmd" in
            tcpdump) echo -e "${YELLOW}💡 Installare con: apt-get install tcpdump${NC}" ;;
            docker) echo -e "${YELLOW}💡 Installare Docker${NC}" ;;
            ntpdate) echo -e "${YELLOW}💡 Installare con: apt-get install ntpdate${NC}" ;;
        esac
        exit 1
    fi
done

# Sincronizzazione orario di sistema
sync_system_time() {
    echo -e "${CYAN}🕐 Sincronizzazione orario di sistema...${NC}"
    if ntpdate -s time.nist.gov 2>/dev/null || ntpdate -s pool.ntp.org 2>/dev/null; then
        echo -e "${GREEN}✅ Orario sincronizzato con server NTP${NC}"
    else
        echo -e "${YELLOW}⚠️  Impossibile sincronizzare con NTP, usando orario locale${NC}"
    fi
    echo -e "${BLUE}🕐 Orario corrente: $(date '+%Y-%m-%d %H:%M:%S %Z')${NC}"
}

# Definizione servizi e IP (estesa con i tuoi IP/porte)
declare -A SERVICES=(
    ["172.28.0.10"]="MySQL-DB"
    ["172.28.0.20"]="Keycloak-Auth"
    ["172.28.0.30"]="Vault-Secrets"
    ["172.28.0.40"]="AppCore-Backend"
    ["172.28.0.50"]="Nginx-Proxy"
    ["172.28.0.60"]="Vault-Init"
)

# Porte e servizi (estesa con le tue porte)
declare -A PORTS=(
    ["3306"]="MySQL"
    ["8443"]="Keycloak-HTTPS"
    ["8200"]="Vault-API"
    ["8080"]="AppCore-HTTP"
    ["80"]="HTTP"
    ["443"]="HTTPS"
)

# Contatori statistiche
declare -A STATS_COUNTER=(
    ["total_packets"]=0
    ["tcp_packets"]=0
    ["udp_packets"]=0
    ["icmp_packets"]=0
    ["arp_packets"]=0
    ["other_packets"]=0
    ["total_bytes"]=0
    ["syn_packets"]=0
    ["fin_packets"]=0
    ["rst_packets"]=0
    ["psh_packets"]=0
    ["urg_packets"]=0
)

# Array per tracciare connessioni attive
declare -A ACTIVE_CONNECTIONS=()

# Funzioni helper
get_service_name() {
    local ip=$1
    echo "${SERVICES[$ip]:-$ip}"
}

get_port_name() {
    local port=$1
    echo "${PORTS[$port]:-$port}"
}

# Funzione migliorata per colorare l'output
colorize_service() {
    local service=$1
    case $service in
        *MySQL*|*DB*) echo -e "${RED}$service${NC}" ;;
        *Keycloak*|*Auth*) echo -e "${BLUE}$service${NC}" ;;
        *Vault*|*Secret*) echo -e "${PURPLE}$service${NC}" ;;
        *AppCore*|*Backend*) echo -e "${GREEN}$service${NC}" ;;
        *Nginx*|*Proxy*) echo -e "${CYAN}$service${NC}" ;;
        *) echo -e "${YELLOW}$service${NC}" ;;  # Usiamo giallo invece di grigio
    esac
}

# Funzione avanzata per analisi TCP flags
analyze_tcp_flags() {
    local line=$1
    local flags=""

    [[ $line == *"[S]"* ]] && flags+="SYN " && ((STATS_COUNTER[syn_packets]++))
    [[ $line == *"[.]"* ]] && flags+="ACK "
    [[ $line == *"[P]"* ]] && flags+="PSH " && ((STATS_COUNTER[psh_packets]++))
    [[ $line == *"[F]"* ]] && flags+="FIN " && ((STATS_COUNTER[fin_packets]++))
    [[ $line == *"[R]"* ]] && flags+="RST " && ((STATS_COUNTER[rst_packets]++))
    [[ $line == *"[U]"* ]] && flags+="URG " && ((STATS_COUNTER[urg_packets]++))
    [[ $line == *"[E]"* ]] && flags+="ECE "
    [[ $line == *"[W]"* ]] && flags+="CWR "

    echo "${flags% }"
}

# Funzione per determinare il tipo di connessione
get_connection_type() {
    local line=$1
    local tcp_flags=$(analyze_tcp_flags "$line")

    if [[ $line == *"UDP"* ]]; then
        echo "📦 UDP"
    elif [[ $tcp_flags == *"SYN"* ]] && [[ $tcp_flags != *"ACK"* ]]; then
        echo "🔵 SYN"
    elif [[ $tcp_flags == *"SYN"* ]] && [[ $tcp_flags == *"ACK"* ]]; then
        echo "🟢 SYN-ACK"
    elif [[ $tcp_flags == *"FIN"* ]]; then
        echo "🔴 FIN"
    elif [[ $tcp_flags == *"RST"* ]]; then
        echo "❌ RST"
    elif [[ $tcp_flags == *"PSH"* ]]; then
        echo "📤 PUSH"
    elif [[ $tcp_flags == *"ACK"* ]]; then
        echo "✅ ACK"
    elif [[ $tcp_flags == *"URG"* ]]; then
        echo "🚨 URGENT"
    else
        echo "📊 DATA"
    fi
}

# Funzione avanzata per estrarre informazioni dettagliate sui pacchetti
extract_packet_info() {
    local line=$1
    local info=""

    # Estrai sequence number
    if [[ $line =~ seq[[:space:]]+([0-9]+) ]]; then
        info+=" seq:${BASH_REMATCH[1]}"
    fi

    # Estrai acknowledgment number
    if [[ $line =~ ack[[:space:]]+([0-9]+) ]]; then
        info+=" ack:${BASH_REMATCH[1]}"
    fi

    # Estrai window size
    if [[ $line =~ win[[:space:]]+([0-9]+) ]]; then
        info+=" win:${BASH_REMATCH[1]}"
    fi

    # Estrai TTL
    if [[ $line =~ ttl[[:space:]]+([0-9]+) ]]; then
        info+=" ttl:${BASH_REMATCH[1]}"
    fi

    # Estrai ID
    if [[ $line =~ id[[:space:]]+([0-9]+) ]]; then
        info+=" id:${BASH_REMATCH[1]}"
    fi

    # Estrai checksum
    if [[ $line =~ cksum[[:space:]]+([0-9A-Fa-f]+) ]]; then
        info+=" cksum:${BASH_REMATCH[1]}"
    fi

    # Estrai opzioni TCP
    if [[ $line =~ options[[:space:]]*\[([^\]]+)\] ]]; then
        info+=" opts:${BASH_REMATCH[1]}"
    fi

    # Estrai lunghezza payload
    if [[ $line =~ length[[:space:]]+([0-9]+) ]]; then
        info+=" len:${BASH_REMATCH[1]}"
        ((STATS_COUNTER[total_bytes]+=${BASH_REMATCH[1]}))
    fi

    # Estrai MAC address se presente
    if [[ $line =~ ([0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}) ]]; then
        info+=" mac:${BASH_REMATCH[1]}"
    fi

    # Estrai ToS (Type of Service)
    if [[ $line =~ tos[[:space:]]+([0-9xA-Fa-f]+) ]]; then
        info+=" tos:${BASH_REMATCH[1]}"
    fi

    # Estrai offset
    if [[ $line =~ offset[[:space:]]+([0-9]+) ]]; then
        info+=" offset:${BASH_REMATCH[1]}"
    fi

    echo "$info"
}

# Funzione per rilevare l'interfaccia bridge Docker
get_docker_bridge() {
    local network_name="instore-network"
    local bridge_id=$(docker network inspect -f '{{.Id}}' $network_name 2>/dev/null | cut -c1-12)

    if [ -z "$bridge_id" ]; then
        echo -e "${YELLOW}⚠️  Rete '$network_name' non trovata, cercando altre reti Docker...${NC}" >&2
        local networks=$(docker network ls --filter driver=bridge --format "{{.Name}}" | grep -v "^bridge$")
        for net in $networks; do
            bridge_id=$(docker network inspect -f '{{.Id}}' $net 2>/dev/null | cut -c1-12)
            if [ -n "$bridge_id" ]; then
                echo -e "${CYAN}✅ Usando rete Docker: $net${NC}" >&2
                break
            fi
        done
    else
        echo -e "${GREEN}✅ Trovata rete target: $network_name${NC}" >&2
    fi

    if [ -z "$bridge_id" ]; then
        echo -e "${RED}❌ Nessuna rete Docker personalizzata trovata. Usando docker0${NC}" >&2
        echo "docker0"
        return
    fi

    local bridge_interface="br-$bridge_id"
    echo "$bridge_interface"
}

# Funzione avanzata per parsing e formattazione
parse_and_format() {
    local packet_count=0

    while IFS= read -r line; do
        ((packet_count++))
        ((STATS_COUNTER[total_packets]++))

        # Timestamp sincronizzato con sistema
        local timestamp=$(date '+%Y-%m-%d %H:%M:%S.%3N')
        local epoch=$(date +%s.%3N)

        # Parsing avanzato TCP/UDP
        if [[ $line =~ ([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+)\.([0-9]+).*\>.*([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+)\.([0-9]+) ]]; then
            local src_ip="${BASH_REMATCH[1]}"
            local src_port="${BASH_REMATCH[2]}"
            local dst_ip="${BASH_REMATCH[3]}"
            local dst_port="${BASH_REMATCH[4]}"

            local src_service=$(get_service_name "$src_ip")
            local dst_service=$(get_service_name "$dst_ip")
            local dst_port_name=$(get_port_name "$dst_port")
            local src_port_name=$(get_port_name "$src_port")

            # Determina protocollo
            local protocol="TCP"
            local conn_type=""
            if [[ $line == *"UDP"* ]]; then
                protocol="UDP"
                conn_type="📦 UDP"
                ((STATS_COUNTER[udp_packets]++))
            else
                conn_type=$(get_connection_type "$line")
                ((STATS_COUNTER[tcp_packets]++))
            fi

            # Estrai dimensione pacchetto e dettagli
            local size=""
            local bytes=0
            if [[ $line =~ length[[:space:]]+([0-9]+) ]]; then
                bytes=${BASH_REMATCH[1]}
                size=" [${bytes}B]"
            fi

            # Informazioni aggiuntive sui pacchetti
            local packet_details=""
            if [[ $DETAILED_ANALYSIS == true ]]; then
                packet_details=$(extract_packet_info "$line")
            fi

            # Traccia connessioni attive
            local conn_key="${src_ip}:${src_port}->${dst_ip}:${dst_port}"
            ACTIVE_CONNECTIONS["$conn_key"]="$timestamp"

            # Output formattato dettagliato
            printf "%s %s %s %s:%s(%s) → %s:%s(%s)%s%s\n" \
                "$timestamp" \
                "$conn_type" \
                "$protocol" \
                "$(colorize_service "$src_service")" \
                "$src_port" \
                "$(echo -e "${YELLOW}$src_port_name${NC}")" \
                "$(colorize_service "$dst_service")" \
                "$dst_port" \
                "$(echo -e "${YELLOW}$dst_port_name${NC}")" \
                "$size" \
                "$(echo -e "${BLUE}$packet_details${NC}")"

            # Log su file
            if [[ $VERBOSE_MODE == true ]]; then
                echo "$timestamp|$protocol|$src_ip:$src_port|$dst_ip:$dst_port|$bytes|$conn_type|$packet_details" >> "$LOG_FILE"
            fi

        # Parsing ICMP migliorato
        elif [[ $line =~ ([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+).*\>.*([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+).*ICMP ]]; then
            local src_ip="${BASH_REMATCH[1]}"
            local dst_ip="${BASH_REMATCH[2]}"

            ((STATS_COUNTER[icmp_packets]++))

            local src_service=$(get_service_name "$src_ip")
            local dst_service=$(get_service_name "$dst_ip")

            # Determina tipo di messaggio ICMP
            local icmp_type="ping"
            [[ $line == *"unreachable"* ]] && icmp_type="unreachable"
            [[ $line == *"redirect"* ]] && icmp_type="redirect"
            [[ $line == *"time exceeded"* ]] && icmp_type="timeout"

            # Estrai codice e tipo ICMP
            local icmp_code=""
            if [[ $line =~ icmp[[:space:]]+([0-9]+) ]]; then
                icmp_code=" type:${BASH_REMATCH[1]}"
            fi
            if [[ $line =~ code[[:space:]]+([0-9]+) ]]; then
                icmp_code+=" code:${BASH_REMATCH[1]}"
            fi

            printf "%s 🏓 ICMP %s %s → %s%s\n" \
                "$timestamp" \
                "$icmp_type" \
                "$(colorize_service "$src_service")" \
                "$(colorize_service "$dst_service")" \
                "$(echo -e "${BLUE}$icmp_code${NC}")"

        # Parsing ARP con dettagli aggiuntivi
        elif [[ $line == *"ARP"* ]]; then
            ((STATS_COUNTER[arp_packets]++))

            local arp_type="request"
            [[ $line == *"reply"* ]] && arp_type="reply"

            # Estrai MAC address
            local mac_info=""
            if [[ $line =~ ([0-9a-fA-F:]{17}) ]]; then
                mac_info=" mac:${BASH_REMATCH[1]}"
            fi

            printf "%s 🔍 ARP %s %s%s\n" \
                "$timestamp" \
                "$arp_type" \
                "$(echo -e "${PURPLE}${line#*ARP}${NC}")" \
                "$(echo -e "${BLUE}$mac_info${NC}")"

        # Altri protocolli con analisi più dettagliata
        else
            ((STATS_COUNTER[other_packets]++))

            # Estrai protocollo se riconosciuto
            local protocol="OTHER"
            [[ $line == *"DNS"* ]] && protocol="DNS"
            [[ $line == *"HTTP"* ]] && protocol="HTTP"
            [[ $line == *"TLS"* ]] && protocol="TLS"

            printf "%s 📋 %s %s\n" \
                "$timestamp" \
                "$(echo -e "${YELLOW}$protocol${NC}")" \
                "$(echo -e "${BLUE}${line:0:100}${NC}")"
        fi

        # Mostra statistiche ogni 50 pacchetti
        if ((packet_count % 50 == 0)); then
            show_live_stats
        fi

    done
}

# Funzione per mostrare statistiche in tempo reale
show_live_stats() {
    local uptime=$(awk '{print int($1)}' /proc/uptime)
    local runtime=$(($(date +%s) - START_TIME))
    local avg_pkt=$(echo "scale=2; ${STATS_COUNTER[total_packets]}/$runtime" | bc -l 2>/dev/null || echo "N/A")
    local avg_bytes=$(echo "scale=2; ${STATS_COUNTER[total_bytes]}/$runtime" | bc -l 2>/dev/null || echo "N/A")

    echo -e "\n${BLUE}📊 STATISTICHE LIVE (Runtime: ${runtime}s)${NC}"
    echo -e "${GREEN}   Total: ${STATS_COUNTER[total_packets]} pacchetti (${STATS_COUNTER[total_bytes]} bytes)${NC}"
    echo -e "${CYAN}   TCP: ${STATS_COUNTER[tcp_packets]} | UDP: ${STATS_COUNTER[udp_packets]} | ICMP: ${STATS_COUNTER[icmp_packets]} | ARP: ${STATS_COUNTER[arp_packets]}${NC}"
    echo -e "${YELLOW}   SYN: ${STATS_COUNTER[syn_packets]} | FIN: ${STATS_COUNTER[fin_packets]} | RST: ${STATS_COUNTER[rst_packets]}${NC}"
    echo -e "${PURPLE}   PSH: ${STATS_COUNTER[psh_packets]} | URG: ${STATS_COUNTER[urg_packets]}${NC}"
    echo -e "${PURPLE}   Connessioni attive: ${#ACTIVE_CONNECTIONS[@]}${NC}"
    echo -e "${BLUE}   Media: $avg_pkt pkt/s | $avg_bytes B/s${NC}"
    echo -e "${BLUE}────────────────────────────────────────${NC}"
}

# Funzione migliorata per gestire l'interruzione
cleanup() {
    local end_time=$(date +%s)
    local total_runtime=$((end_time - START_TIME))
    local avg_pkt=$(echo "scale=2; ${STATS_COUNTER[total_packets]}/$total_runtime" | bc -l 2>/dev/null || echo "N/A")
    local avg_bytes=$(echo "scale=2; ${STATS_COUNTER[total_bytes]}/$total_runtime" | bc -l 2>/dev/null || echo "N/A")

    echo -e "\n${YELLOW}🛑 Monitoraggio interrotto dopo ${total_runtime} secondi...${NC}"
    echo -e "${BLUE}📊 STATISTICHE FINALI:${NC}"

    # Statistiche pacchetti
    echo -e "${BLUE}   📦 Pacchetti totali: ${STATS_COUNTER[total_packets]}${NC}"
    echo -e "${GREEN}   📊 TCP: ${STATS_COUNTER[tcp_packets]} | UDP: ${STATS_COUNTER[udp_packets]} | ICMP: ${STATS_COUNTER[icmp_packets]} | ARP: ${STATS_COUNTER[arp_packets]}${NC}"
    echo -e "${CYAN}   📈 Bytes totali: ${STATS_COUNTER[total_bytes]} ($(echo "scale=2; ${STATS_COUNTER[total_bytes]}/1024/1024" | bc -l 2>/dev/null || echo "N/A") MB)${NC}"
    echo -e "${YELLOW}   🔄 Rate medio: $avg_pkt pacchetti/sec | $avg_bytes bytes/sec${NC}"

    # Top connessioni
    echo -e "\n${PURPLE}🔝 TOP CONNESSIONI:${NC}"
    {
        echo -e "${BLUE}SRC_IP:PORT → DST_IP:PORT${NC}"
        for conn in "${!ACTIVE_CONNECTIONS[@]}"; do
            echo "$conn"
        done | sort | uniq -c | sort -nr | head -n 5 | while read count conn; do
            echo -e "${CYAN}   $count x ${GREEN}$conn${NC}"
        done
    }

    # Statistiche interfaccia
    if [ -f /proc/net/dev ] && grep -q "$BRIDGE_INTERFACE" /proc/net/dev; then
        echo -e "\n${PURPLE}🌐 Statistiche interfaccia $BRIDGE_INTERFACE:${NC}"
        grep "$BRIDGE_INTERFACE" /proc/net/dev | awk '{
            printf "      📥 RX: %.2f MB (%s pacchetti, %s errori)\n", $2/1024/1024, $3, $4
            printf "      📤 TX: %.2f MB (%s pacchetti, %s errori)\n", $10/1024/1024, $11, $12
        }'
    fi

    # Salva log finale
    if [[ $VERBOSE_MODE == true ]]; then
        echo -e "${BLUE}   💾 Log salvato in: $LOG_FILE${NC}"
    fi

    echo -e "${GREEN}✅ Monitoraggio terminato.${NC}"
    exit 0
}

# Avvio script
clear
echo -e "${PURPLE}╔══════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${PURPLE}║            🐳 DOCKER NETWORK TRAFFIC MONITOR - OPTIMIZED 🐳           ║${NC}"
echo -e "${PURPLE}║                        ADVANCED PACKET ANALYSIS                       ║${NC}"
echo -e "${PURPLE}╚══════════════════════════════════════════════════════════════════════╝${NC}"
echo

# Sincronizza orario
sync_system_time
echo

# Inizializza timestamp di avvio
START_TIME=$(date +%s)

# Rileva interfaccia bridge Docker
BRIDGE_INTERFACE=$(get_docker_bridge)
echo -e "${GREEN}✅ Interfaccia rilevata: $BRIDGE_INTERFACE${NC}"

# Verifica interfaccia
if ! ip link show "$BRIDGE_INTERFACE" >/dev/null 2>&1; then
    echo -e "${RED}❌ Interfaccia $BRIDGE_INTERFACE non trovata!${NC}"
    echo -e "${YELLOW}🔍 Interfacce disponibili:${NC}"
    ip link show | grep -E '^[0-9]+:' | awk '{print "   " $2}' | sed 's/:$//'
    exit 1
fi

# Mostra configurazione
echo -e "${BLUE}⚙️  Configurazione:${NC}"
echo -e "   📊 Analisi dettagliata: $([ "$DETAILED_ANALYSIS" = true ] && echo -e "${GREEN}Abilitata${NC}" || echo -e "${YELLOW}Disabilitata${NC}")"
echo -e "   📝 Logging verboso: $([ "$VERBOSE_MODE" = true ] && echo -e "${GREEN}Abilitato${NC}" || echo -e "${YELLOW}Disabilitato${NC}")"
echo -e "   💾 File di log: $LOG_FILE"
echo

# Mostra servizi monitorati
echo -e "${BLUE}📋 Servizi nella rete Docker:${NC}"
for ip in "${!SERVICES[@]}"; do
    echo -e "   $ip → $(colorize_service "${SERVICES[$ip]}")"
done
echo

# Setup trap per CTRL+C
trap cleanup INT TERM

# Informazioni di avvio
echo -e "${GREEN}🚀 Avvio cattura traffico su: $BRIDGE_INTERFACE${NC}"
echo -e "${CYAN}⏰ Inizio monitoraggio: $(date '+%Y-%m-%d %H:%M:%S %Z')${NC}"
echo -e "${YELLOW}💡 Premi CTRL+C per terminare${NC}"
echo -e "${PURPLE}════════════════════════════════════════════════════════════════════════${NC}"

# Avvia tcpdump con cattura dettagliata
if [[ $SHOW_PAYLOAD == true ]]; then
    tcpdump -i "$BRIDGE_INTERFACE" -l -n -t -v -X 2>/dev/null | parse_and_format
else
    tcpdump -i "$BRIDGE_INTERFACE" -l -n -t -v 2>/dev/null | parse_and_format
fi