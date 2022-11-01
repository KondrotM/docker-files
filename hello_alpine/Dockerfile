FROM alpine:latest

RUN apk add openjdk12

COPY . . 

RUN javac Main.java

CMD ["java", "Main"]

