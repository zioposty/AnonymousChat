FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/zioposty/AnonymousChatP2P.git

#FROM maven:3-jdk-11
FROM maven:3-jdk-13
WORKDIR /app
COPY --from=0 /app/AnonymousChatP2P /app
RUN mvn package

FROM openjdk:13.0.1-slim
WORKDIR /app
COPY --from=1 /app/testImages /app
ENV MASTERIP=127.0.0.1
ENV ID=0
ENV DISPLAY=host.docker.internal:0.0
COPY --from=1 /app/target/ac-1.0-jar-with-dependencies.jar /app

RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*
RUN apt-get update && apt-get install --no-install-recommends -y xorg libgl1-mesa-glx && rm -rf /var/lib/apt/lists/*
RUN apt-get remove nvidia*
CMD java -jar ac-1.0-jar-with-dependencies.jar ${ID} ${MASTERIP}
# -e host.docker.internal:0.0      da aggiungere al run