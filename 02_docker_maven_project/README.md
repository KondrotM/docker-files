# 02: Maven project in Docker
## A bigger java project
The previous module compiled a java hello world app, which only had one file and no dependencies. 
Compiling an app with multiple files means folders and file structure should be considered.

The `java_project` directory contains a small, OO java applet. This is the dockerfile it builds from:
```Dockerfile
FROM openjdk:12-alpine

COPY project/ .

WORKDIR src

RUN javac Main.java -d ../bin

CMD ["java", "-cp", "../bin", "Main"]
```

To run the program:
```bash
sudo docker build -t java-applet .
sudo docker run -it java-applet
```

Because the program requires input from the user, docker is ran using `it` to make the process interactive.


## Java with Maven dependencies
Running a project with maven dependencies adds another configuration layer, but docker can simplify this by using a Maven image. `maven_docker` installs dependencies, runs tests and compiles the application.
```Dockerfile
FROM    maven:3.6.0-jdk-8 AS build

RUN     mkdir /project

WORKDIR /project

COPY    pom.xml .
COPY    src src

RUN     mvn clean package

FROM openjdk:11-jre-slim
COPY --from=build /docker /usr/local/docker

ENTRYPOINT ["java", "-cp", "/usr/local/docker/target/demo.jar","org.mk.App"]
```

This project doens't have to run interactively, and so can be compiled using:
```bash
sudo docker build -t maven-applet .
sudo docker run -it maven-applet
```
