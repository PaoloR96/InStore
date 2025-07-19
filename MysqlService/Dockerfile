FROM mysql:9.2.0

USER root

#RUN microdnf install -y logrotate

#RUN mkdir -p /etc/logrotate.d/mysql
#RUN chmod 755 /etc/logrotate.d/mysql
#RUN chown root:root /etc/logrotate.d/mysql

RUN mkdir -p /etc/mysql/certs
RUN chown -R mysql:mysql /etc/mysql/certs

RUN mkdir -p /var/log/mysql
RUN chown -R mysql:mysql /var/log/mysql

RUN echo '{ "components": "file://component_keyring_file" }' > /usr/sbin/mysqld.my && \
    chown mysql:mysql /usr/sbin/mysqld.my && \
    chmod 640 /usr/sbin/mysqld.my

RUN echo '{ "path": "/var/lib/mysql-keyring/keyring", "read_only": false }' > /usr/lib64/mysql/plugin/component_keyring_file.cnf && \
    chown mysql:mysql /usr/lib64/mysql/plugin/component_keyring_file.cnf && \
    chmod 640 /usr/lib64/mysql/plugin/component_keyring_file.cnf

RUN chown -R 999:999 /var/lib/mysql-keyring

USER mysql