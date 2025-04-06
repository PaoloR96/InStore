storage "file" {
   path    = "./vault/data"
}

listener "tcp" {
   address     = "0.0.0.0:8200"
   tls_cert_file = "/vault/certs/fullchain.pem"
   tls_key_file  = "/vault/certs/privkey.pem"
   tls_disable_client_certs = true
}

api_addr = "https://127.0.0.1:8200"
cluster_addr = "https://127.0.0.1:8201"
ui = true
