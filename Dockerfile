FROM openjdk:8-jdk-alpine

LABEL maintainer="Philipp Arndt <2f.mail@gmx.de>"
LABEL version="1.0"
LABEL description="deconz to mqtt gateway"

ENV LANG en_US.UTF-8
ENV TERM xterm

WORKDIR /opt/deconz-to-mqtt-gw

RUN apk update --no-cache && apk add --no-cache maven

COPY src /opt/deconz-to-mqtt-gw

VOLUME /root/.m2

RUN mvn install assembly:single
RUN cp ./de.rnd7.deconzmqttgw/target/deconz-to-mqtt-gw.jar ./deconz-to-mqtt-gw.jar
COPY config.json /opt/deconz-to-mqtt-gw/config.json

CMD java -jar deconz-to-mqtt-gw.jar config.json