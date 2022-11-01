# Docker notes
## Using Docker
Using docker is no biggie, you just need to 
Docker projects are based on two files: A `Dockerfile` and a `docker-compose.yml` file. Each file has a different purpose and while you can get by using only the Dockerfile, it is important to understand both for effective development.

![Docker containers](https://i.imgur.com/LTyPF5o.png "Docker containers")
[//]: # (![Docker vs VM](https://i.imgur.com/7Q4SeKh.png "Docker vs Virtual Machine"))

The `Dockerfile` is a lower-level set of instructions which details what to run in the project.
The `docker-compose.yml` constructs a higher-level environment for how to run the project. 

[//]: # (They have a different syntax. Further detail for each is below.)

## Dockerfile
Below is a simple `Dockerfile` used to run a java application.

Java applicaition:
```java
//Main.java
public class Main {
	public static void main(String[] args){
		System.out.println("Hello World!");
	}
}
```
Dockerfile:
```dockerfile
FROM ubuntu:20.04

RUN apt update
RUN apt install default-jdk -y
COPY . . 
RUN javac Main.java

CMD ["java", "Main"]
```

While not optimal, this `Dockerfile` represents the core concepts of what's great about Docker. 
First, the code pulls an ubuntu image `FROM` the [docker library](https://hub.docker.com/_/ubuntu/).
It then installs the JDK (java development kit), compiles the java code via `RUN javac Main.java` and finally runs the Main, compiled, application resulting in `Hello World` being printed to your console.

Once you have these two files in your project, you can run these two scrips to build and run your container.

```bash
docker build -t hello .
docker run hello
#> Hello World!
```

This system is great for shipping code as all dependencies are packaged along with the program. The only overhead is that the container is running its own virtual version of Ubuntu. For enterprise projects with many dependencies, this tradeoff is usually worth it. In addition, images can be pulled `FROM` the docker library which are smaller and more specialised than ubuntu.

```dockerfile
FROM openjdk:12-alpine

COPY . . 

RUN javac Main.java

CMD ["java", "Main"]
```

This dockerfile does the same job as the first one but uses Alpine Linux, which has a small footprint ideal for docker containers. 
In addition, openjdk 12 is a [package of Alpine linux](https://pkgs.alpinelinux.org/packages?name=openjdk12), meaning it can be pre-installed as above.

Since adding openjdk is online one extra argument, this can be avoided by using this `Dockerfile` instead:
```dockerfile
FROM alpine:latest

RUN apk add openjdk12

COPY . . 

RUN javac Main.java

CMD ["java", "Main"]
```


## Docker-compose
Running more complex projects usually requires other services to run alongisde each other. For example an SQL database to interface with a Java application; or a routing engine to interface with different pages being served.

Here is a `Dockerfile` to serve a small [Flask](https://pypi.org/project/Flask/) application. The full application can be found [here](https://docs.docker.com/compose/gettingstarted/).

```dockerfile
# syntax=docker/dockerfile:1
FROM python:3.7-alpine
WORKDIR /code
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
EXPOSE 5000
COPY . .
CMD ["flask", "run"]
```

Now, a `docker-compose.yml` defines a [redis](https://hub.docker.com/_/redis/) database which interacts with Flask, and serves the application on the local port 8000.

```yml
version: "3.9"
services:
  web:
    build: .
    ports:
      - "8000:5000"
  redis:
    image: "redis:alpine"
```

Because redis is configured within the Flask app, no extra configuration is needed. Running this container results in a 'Hello world' page with a counter.
```bash
docker compose up -d
###
curl localhost:8000
#> Hello World! I've been seen 1 times.
```

Restarting the container persists the value:
```bash
docker compose down
docker compose up -d
###
curl localhost:8000
#> Hello World! I've been seen 2 times.
```

More docker compose documantation is [here](https://docs.docker.com/get-started/08_using_compose/)

## Cheatsheet
### Quick notes
Unless you have added docker to your user permissions, you will have to prepend most of these commands with `sudo` or use `sudo su`

### View local images
`docker images` or `docker image ls`
```
REPOSITORY    TAG       IMAGE ID       CREATED          SIZE
hello_qt      latest    f8d6e15a378c   24 minutes ago   792MB
ubuntu        20.04     680e5dfb52c7   14 hours ago     72.8MB
tomcat        latest    543fd2c4284f   11 days ago      473MB
hello-world   latest    feb5d9fea6a5   13 months ago    13.3kB
```

### Build local docker
```bash
cd project_folder
docker build -t <name> .
docker run <name>
```

## Files
### Basic Java Dockerfile
```dockerfile
FROM ubuntu:20.04

RUN apt update
RUN apt install default-jdk -y
COPY . . 
RUN javac Main.java

CMD ["java", "Main"]

# Main.java
# public class Main {
# 	public static void main(String[] args){
# 		System.out.println("Hello World!");
# 	}
# }
```

### Basic Alpine Docker
This file uses Alpine Linux instead of Ubuntu. Alpine is preferrable because it is smaller and safer than Ubuntu, but it is okay to also use Ubuntu as in the basic Java configuration.
```dockerfile
FROM openjdk:12 alpine

COPY . . 

RUN javac Main.java

CMD ["java", "Main"]
```

### Basic Java Project Dockerfile
This file introduces directory management. `project/` contains the java project and has two directories `src/` and `bin/`. Java files in `src` are compiled to `bin` and this is then run in the final line. Using folders helps with file name conflicts and increases clarity.
```dockerfile
FROM openjdk:12-alpine

COPY project/ .

WORKDIR src

RUN javac Main.java -d ../bin

CMD ["java", "-cp", "../bin", "Main"]

```

