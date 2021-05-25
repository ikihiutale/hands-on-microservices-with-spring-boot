

# Create skeleton code for microservices

If you want to learn more about the spring init CLI, you can run the spring help init command.
To see what dependencies you can add, run the spring init --list command.


spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=product-service --package-name=microservices.core.product --groupId=microservices.core.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=review-service --package-name=microservices.core.review --groupId=microservices.core.review --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT review-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=recommendation-service --package-name=microservices.core.recommendation --groupId=microservices.core.recommendation --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT recommendation-service

spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=product-composite-service --package-name=microservices.composite.product --groupId=microservices.composite.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-composite-service

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



./gradlew bootRun



# 
spring init --boot-version=2.5.0.RELEASE --build=gradle --java-version=11 --packaging=jar --name=composite-product-service --package-name=microservices.composite.product --groupId=microservices.composite.product --version=1.0.0-SNAPSHOT composite-product-service

