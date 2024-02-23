Simple app to use CRUD with `spring-boot-data-rest` and a PostgreSQL database

This uses testcontainers and WebClientTest for tests.


### PostgreSQL installation
See https://www.postgresql.org/download/linux/ubuntu/
then
```
CREATE DATABASE mydb;
CREATE USER myuser WITH ENCRYPTED PASSWORD 'mypass';
GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;
```