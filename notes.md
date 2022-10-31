# Docker notes
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

