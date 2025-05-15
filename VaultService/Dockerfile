FROM hashicorp/vault:1.18.4

USER root

RUN apk add --no-cache libcap \
    && setcap cap_ipc_lock=+ep /bin/vault

RUN adduser -D -u 1001 -g 1001 vaultuser