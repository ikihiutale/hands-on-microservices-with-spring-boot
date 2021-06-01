

# Create skeleton code for microservices

If you want to learn more about the spring init CLI, you can run the spring help init command.
To see what dependencies you can add, run the spring init --list command.


spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=product-service --package-name=foo.bar.microservices.core.product --groupId=foo.bar.microservices.core.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=review-service --package-name=foo.bar.microservices.core.review --groupId=foo.bar.microservices.core.review --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT review-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=recommendation-service --package-name=foo.bar.microservices.core.recommendation --groupId=foo.bar.microservices.core.recommendation --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT recommendation-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=product-composite-service --package-name=foo.bar.microservices.composite.product --groupId=foo.bar.microservices.composite.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-composite-service

# Buid

hands-on-microservices-with-spring-boot\product-service> ./gradlew build

The first time we run a command with gradlew, it will download Gradle automatically.
The Gradle version that's used is determined by the distributionUrl property in the gradle/wrapper/gradle-wrapper.properties file

## Root project


Copy gradle folder, .gitignore file, gradlew and gradlew.bar files from 
product-composite-service folder to microservices root folder

Add settings.gradle file into the microservices root folder
```
include ':microservices:product-service'
include ':microservices:review-service'
include ':microservices:recommendation-service'
include ':microservices:product-composite-service'

```

All project can be compiled from root folder: ./gradlew build --info

Note:
From a DevOps perspective, a multi-project setup might not be preferred. Instead, setting up a separate build pipeline
for each microservice project would probably be preferred. However, for the purposes of this book, we will use the 
multi-project setup to make it easier to build and deploy the whole system landscape with a single command.

# Add shared projects

First, we will add two projects (api and util) that will contain code that is shared by the microservice project.
From a DevOps perspective, it would be preferable to build all the projects in their own build pipeline and have
version-controlled dependencies for the api and util projects in the microservice projects; that is, so that each
microservice can choose what versions of the api and util projects to use. But to keep the build and deployment
steps simple in the context of this book, we will make the api and util projects part of the multi-project build

The api project will be packaged as a library; that is, it won't have its own main application class. Unfortunately,
Spring Initializr doesn't support the creation of library projects. Instead, a library project has to be created
manually from scratch.

Api build.gradle retains Spring Boot dependency management while we are replacing the construction of a fat JAR in the
build step with the creation of normal JAR files; that is, they only contain the library project's own class and property files.

The util project will be packaged as a library in the same way as the api project
Except for the code in ServiceUtil.java, these classes are reusable utility classes that we can use to map Java exceptions to 
proper HTTP status codes, as described in the Adding error handling section. The main purpose of ServiceUtil.java is to find 
out the hostname, IP address, and port used by the microservice. The class exposes a method, getServiceAddress(), that can be 
used by the microservices to find their hostname, IP address, and port.


spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=api --package-name=foo.bar.microservices.composite.product --groupId=foo.bar.microservices.composite.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-composite-service

# Link Api and Utils project

We need to add the api and util projects as dependencies in our build.gradle file
build.gradle:
```
dependencies {
	implementation project(':api')
	implementation project(':util')
```

# Build & run product-service

cd D:\ws\hands-on-microservices-with-spring-boot\microservices\product-service
./gradlew build

microservices\recommendation-service\build\libs> java -jar recommendation-service-1.0.0-SNAPSHOT.jar
microservices\review-service\build\libs> java -jar review-service-1.0.0-SNAPSHOT.jar
microservices\product-service\build\libs> java -jar product-service-1.0.0-SNAPSHOT.jar
microservices\product-composite-service\build\libs> java -jar product-composite-service-1.0.0-SNAPSHOT.jar


..or run all four on STS Spring Boot Dashboard at the same time...



# Test
./gradlew test

# Create a container that runs Ubuntu based on the latest version that's available of the official Docker image

docker run -it --rm ubuntu

The -it option is used so that we can interact with the container using Terminal,
and the --rm option tells Docker to remove the container once we exit the Terminal session

Test: cat /etc/os-release | grep 'VERSION='

# List Ubuntu containers

docker ps -a

Verify that the Ubuntu container no longer exits. With the -a option also stopped containers are listed

# List containers

docker ps -a
lists the container IDs of all the running and stopped containers in the Docker engine

docker ps -q
The -q option reduces the output from the docker ps command so that it only lists the container ID


# Clean "unvanted" containers

docker rm -f $(docker ps -aq)

Removes running and stopped containers

# Test Java resources on jshell

How many available processors, that is, CPU cores, Java sees when running outside of Docker:
echo 'Runtime.getRuntime().availableProcessors()' | jshell -q

The maximum size that it thinks it can allocate for the heap. We can achieve this by asking the
JVM for extra runtime information using the -XX:+PrintFlags. Final Java option and then using the
grep command to filter out the MaxHeapSize parameter, like so:

java -XX:+PrintFlagsFinal -version | grep MaxHeapSize 
java -Xmx200m -XX:+PrintFlagsFinal -version | grep MaxHeapSize


# Test Java resources on Docker

echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i openjdk:12.0.2 jshell -q

Restrict the Docker container to only be allowed to use three CPU cores using the --cpus 3 

Let's also try to specify a relative share of the available CPUs instead of an exact number of CPUs.
1,024 shares correspond to one core by default, so if we want to limit the container to two cores,
we set the --cpu-shares Docker option to 2,048, like so:
echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i --cpu-shares 2048 openjdk:12.0.2 jshell -q


With no memory constraints:
docker run -it --rm openjdk:12.0.2 java -XX:+PrintFlagsFinal -version | grep MaxHeapSize


Docker container to only use up to 1 GB of memory:
docker run -it --rm -m=1024M openjdk:12.0.2 java -XX:+PrintFlagsFinal -version | grep MaxHeapSize


Allow the heap to use 800 MB of the total 1 GB we have, we can specify that using the -Xmx800m Java option:
docker run -it --rm -m=1024M openjdk:12.0.2 java -Xmx800m -XX:+PrintFlagsFinal -version | grep MaxHeapSize

# Docker

microservices/product-service/Dockerfile. It looks like this:
```
FROM openjdk:12.0.2

EXPOSE 8080

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

```

Some things to take note of are as follows:We will base our Docker image on the
official Docker image for OpenJDK and use the Java SE v12.0.2.

We will expose port 8080 to other Docker containers.

We add our fat-jar file to the Docker image from the Gradle build library,
build/libs.

We will specify the command to be used by Docker when a container is started up using this
Docker image, that is, java -jar /app.jar.

Check fat jar file: ls -l microservices/product-service/build/libs

Next, we will build the Docker image and name it product-service, as follows:
cd microservices/product-service
docker build -t product-service .

## Starting up the service
docker run --rm -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" product-service

docker run: The Docker run command will start the container and display log output in Terminal.
Terminal will be locked as long as the container runs.We have seen the --rm option already;
it will tell Docker to clean up the container once we stop the execution from Terminal using Ctrl + C.

The -p8080:8080 option maps port 8080 in the container to port 8080 in the Docker host, which makes it
possible to call it from the outside. In the case of Docker for macOS, which runs Docker in a local Linux
virtual machine, the port will also be port-forwarded to macOS, which is made available on localhost.
We can only have one container mapping to a specific port in the Docker host!

With the -e option, we can specify environment variables for the container, which in this case is
SPRING_PROFILES_ACTIVE=docker. The SPRING_PROFILES_ACTIVE environment variable is used to tell Spring
what profile to use. In our case, we want Spring to use the docker profile.

Finally, we have product-service, which is the name of the Docker image that Docker will use to start the container.

### Verify that we got a Docker image, as expected
docker images | grep product-service


### Try out the following code in another Terminal window
curl localhost:8080/product/3

Note: hostname is docker container id:
docker ps

## Running the container detached

Running the container without locking Terminal is done by adding the -d option and at the same time
giving it a name using the --name option. The --rm option is no longer required since we will stop
and remove the container explicitly when we are done with it:
docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name my-prd-srv product-service

Meet the Docker logs command:
docker logs my-prd-srv -f

# Wrap this up by stopping and removing the container by name

docker rm -f my-prd-srv


# Managing a landscape of microservices using Docker Compose

the purpose of docker-compose. By using single commands, we can
build, start, log, and stop a group of cooperating microservices running as Docker containers!

To be able to use Docker Compose, we need to create a configuration file, docker-compose.yml

## Build docker image

docker-compose.yml:
```
version: '2.1'

services:
    product:
        build: microservices/product-service
        mem_limit: 350m
        environment:
            - SPRING_PROFILES_ACTIVE=docker
    recommendation:
        build: microservices/recommendation-service
        mem_limit: 350m
        environment:
            - SPRING_PROFILES_ACTIVE=docker
    review:
        build: microservices/review-service
        mem_limit: 350m
        environment:
            - SPRING_PROFILES_ACTIVE=docker
    product-composite:
        build: microservices/product-composite-service
        mem_limit: 350m
        ports:
            - "8080:8080"
        environment:
            - SPRING_PROFILES_ACTIVE=docker

```
Build code: 
```
./gradlew build
```

Build docker compose: 
```
docker-compose build
```
Check images:
```
docker images | grep hands-on-microservices-with-spring-boot_
```
Start up the microservices landscape with the following command:
```
docker-compose up -d

docker-compose ps

```

We can follow the startup by monitoring the output that's written to
each container log with the following command
```
docker-compose logs -f
```

Test that the microservices works as expected:
```
curl localhost:8080/product-composite/123 -s | jq .
```


Shut down the microservices:
```
docker-compose down
```

If required, you can restart a failed microservice with the docker-compose up -d --scale command.
For example, you would use the following code if you wanted to restart the product service:


```
docker-compose up -d --scale product=0
```


# How to use Spring Boot profiles

Spring Boot Maven Plugin:
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=foo,bar

Running jar:
java -jar -Dspring.profiles.active=dev XXX.jar

You can also set the profile using a SPRING_PROFILES_ACTIVE environment variable or a spring.profiles.active
system property

https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.properties-and-configuration.set-active-spring-profiles





