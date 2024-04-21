FROM --platform=linux/amd64 openjdk:17-alpine
COPY build/libs/weather-dress-0.0.1-SNAPSHOT.jar weather-dress.jar
ENTRYPOINT ["java","-DSpring.profiles.active=prod","-jar","weather-dress.jar"]