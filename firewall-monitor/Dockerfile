FROM ubuntu:20.04

# Imposta il terminale per evitare l'errore "TERM environment variable not set"
ENV TERM=xterm

# Installa i pacchetti necessari
RUN apt update && apt install -y  iproute2 iputils-ping procps sudo docker.io tcpdump ntpdate xxd jq bc

# Aggiungi lo script di monitoraggio e rendilo eseguibile
COPY firewall_monitor.sh /usr/local/bin/firewall_monitor.sh
RUN chmod +x /usr/local/bin/firewall_monitor.sh

# Comando per avviare il monitoraggio
CMD ["/bin/bash", "/usr/local/bin/firewall_monitor.sh"]

