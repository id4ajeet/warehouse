FROM postgres:13.1
COPY init.d /docker-entrypoint-initdb.d/
CMD ["docker-entrypoint.sh", "postgres"]