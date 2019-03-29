#!/bin/sh
mvn clean install assembly:single
if [ $? -eq 0 ]; then
    scp ./de.rnd7.deconzmqttgw/target/deconz-to-mqtt-gw.jar 192.168.3.104:/home/pi/deconz-to-mqtt-gw.jar
    echo "deploy finished"
else
    echo FAIL
fi