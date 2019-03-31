#!/bin/bash
pid=`ps aux | grep deconz-to-mqtt-gw | awk '{print $2}'`
kill -9 $pid