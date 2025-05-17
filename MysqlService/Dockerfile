FROM mysql:9.3.0

USER root

RUN mkdir -p /etc/mysql/certs

RUN chown -R mysql:mysql /etc/mysql/certs

RUN echo '{ "components": "file://component_keyring_file" }' > /usr/sbin/mysqld.my && \
    chown root:mysql /usr/sbin/mysqld.my && \
    chmod 640 /usr/sbin/mysqld.my

RUN echo '{ "path": "/var/lib/mysql-keyring/keyring", "read_only": false }' > /usr/lib64/mysql/plugin/component_keyring_file.cnf && \
    chmod 640 /usr/lib64/mysql/plugin/component_keyring_file.cnf && \
    chown root:mysql /usr/lib64/mysql/plugin/component_keyring_file.cnf

RUN chown -R 999:999 /var/lib/mysql-keyring

USER mysql