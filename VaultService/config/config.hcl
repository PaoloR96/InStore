storage "file" {
   path    = "./vault/data"
}

listener "tcp" {
   address     = "0.0.0.0:8200"
   tls_cert_file = "/vault/certs/fullchain1.pem"
   tls_key_file  = "/vault/certs/privkey1.pem"
   tls_disable_client_certs = true
}

api_addr = "https://127.0.0.1:8200"
cluster_addr = "https://127.0.0.1:8201"
ui = true

log_level = "info"
log_file = "/vault/logs/vault.log"
log_rotate_duration = "24h"
log_rotate_max_files = 7
