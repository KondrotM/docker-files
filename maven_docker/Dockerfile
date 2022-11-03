FROM    maven:3.6.0-jdk-8 AS build

#RUN mkdir /app
#WORKDIR /app
#COPY src /app/src
#COPY pom.xml /app
#RUN mvn -f /app/pom.xml clean package
RUN     mkdir /docker

WORKDIR /docker

COPY    pom.xml .
COPY    src src

RUN     mvn clean package

FROM openjdk:11-jre-slim
#COPY --from=build /docker/target/demo.jar /usr/local/lib/demo.jar
COPY --from=build /docker /usr/local/docker

#ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]
ENTRYPOINT ["java", "-cp", "/usr/local/docker/target/demo.jar","org.mk.App"]
