FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/zioposty/AnonymousChatP2P.git

FROM maven:3-jdk-11
WORKDIR /app
COPY --from=0 /app/AnonymousChatP2P /app
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
COPY --from=1 /app/target/AnonymousChat-1.0-jar-with-dependencies.jar /app

CMD /usr/bin/java -jar AnonymousChat-1.0-jar-with-dependencies.jar -m $MASTERIP -id $ID