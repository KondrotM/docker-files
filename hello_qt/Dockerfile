FROM openjdk:12-alpine

COPY project/ .

WORKDIR src

RUN javac Main.java -d ../bin

CMD ["java", "-cp", "../bin", "Main"]
