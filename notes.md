# Docker notes
## Using Docker
Docker projects are based on two files: A `Dockerfile` and a `docker-compose.yml` file. Each file has a different purpose and while you can get by using only the Dockerfile, it is important to understand both for effective development.


The `Dockerfile` is a lower-level set of instructions which details what to run in the project.
The `docker-compose.yml` constructs a higher-level environment for how to run the project. 
![Docker containers](https://i.imgur.com/LTyPF5o.png "Docker containers")

[//]: # (They have a different syntax. Further detail for each is below.)

## Dockerfile
Below is a simple `Dockerfile` used to run a java application.

Java applicaition:
```java
// Main.java
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

## Maven with Docker
Integrating a Maven project with Docker is a two-step process. One docker command packages the java code, running tests and compiling it, while another command then runs the compiled app. Below is a Dockerfile used for this.
```dockerfile
FROM    maven:3.6.0-jdk-8 AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/maven-docker.jar /usr/local/lib/demo.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]
```

In order to correctly compile the jar, the pom.xml file must be modified to include a `<build>` section. The compiled jar will be in `/target/selenium_test.jar`, but the `finalName` tag can be changed.
```xml
<build>
	<finalName>selenium_test</finalName>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-jar-plugin</artifactId>
			<configuration>
				<archive>
				    <manifest>
					<mainClass>org.mk.App</mainClass>
				    </manifest>
				</archive>
			</configuration>
		</plugin>
	</plugins>
</build>
```

To run the docker project, run the following commands:
```bash
docker build -t maven_project .
docker run maven_project
```

This process allows to you test and run a java project with maven packages in a container.


To package and run the code locally (not needed, sanity check only), use these commands:
```bash
mvn clean package
java -jar target/selenium_test.jar
```

## Selenium with Docker
To open a remote selenium chrome image, use the following command in the command-line.
```bash
sudo docker run -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome

```
This will then launch a selenium instance on `localhost:4444`. You can connect to it in a java project using this command:
```java
ChromeOptions chromeOptions = new ChromeOptions();
WebDriver driver = new RemoteWebDriver(new URL("http://chrome:4444/wd/hub"), chromeOptions );

driver.manage().window().setSize(new Dimension(1280, 1020));
```
Then use your tests like normal. This works well as an example abstraction, but doesn't allow taking advantage of a lot of the benefits of Dockerised selenium, such as Selenium Grid.




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

