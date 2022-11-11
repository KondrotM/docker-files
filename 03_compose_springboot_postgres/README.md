# 03: Integrating multiple services with Docker compose
## Springboot and postgres
A backend database and java api can be run on the same container, with a docker compose file used to bridge their networks.
The following file securely bridges postgres and springboot. To run it you don't need to interact with it, you can just run `sudo docker compose up`.

```yml
services:
  backend:
    build: backend
    ports:
      - 8080:8080
    environment:
      - POSTGRES_DB=example
    networks:
      - spring-postgres
  db:
    image: postgres
    restart: always
    secrets:
      - db-password
    volumes:
      - db-data:/var/lib/postgresql/data
    networks:
      - spring-postgres
    environment:
      - POSTGRES_DB=example
      - POSTGRES_PASSWORD_FILE=/run/secrets/db-password
    expose:
      - 5432
volumes:
  db-data:
secrets:
  db-password:
    file: db/password.txt
networks:
  spring-postgres:
```

Once you've ran `docker compose up` and waited for the server to run, you can confirm it works in another terminal.

```bash
curl localhost:8080

# <!DOCTYPE HTML>
# <html>
# <head>
#   <title>Getting Started: Serving Web Content</title>
#   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
# </head>
# <body>
#         <p>Hello from Docker!</p>
# </body>
```
