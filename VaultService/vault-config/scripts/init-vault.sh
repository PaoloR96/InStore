#!/bin/bash

set -e

VAULT_ADDR=http://127.0.0.1:8200
export VAULT_ADDR

# Initialize Vault
vault operator init -key-shares=1 -key-threshold=1 > /vault-config/secrets/init-output.txt
UNSEAL_KEY=$(grep 'Unseal Key 1:' /vault-config/secrets/init-output.txt | awk '{print $NF}')
ROOT_TOKEN=$(grep 'Initial Root Token:' /vault-config/secrets/init-output.txt | awk '{print $NF}')

# Unseal Vault
vault operator unseal $UNSEAL_KEY

# Login as root
vault login $ROOT_TOKEN

# Enable KV secrets engine
vault secrets enable -path=secret kv-v2

# Apply policies
vault policy write sensitive-data /vault-config/policies/vault-policy.hcl

# Load secrets
#vault kv put secret/data/oauth2-ssd @/vault-config/secrets/secrets.json

# Save tokens
#echo $ROOT_TOKEN > /vault-config/secrets/root-token.txt
#echo $UNSEAL_KEY > /vault-config/secrets/unseal-key.txt

echo "Vault has been initialized and configured."
