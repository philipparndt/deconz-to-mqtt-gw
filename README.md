# Deconz to MQTT Gateway

## Create an API key

You neen a deconz API key. To get this:

- Open the phoscon web application and navigate to: Gateway / Extended
- Click on: `Connect App`
- Execute the following command within the next 60 secounds: `curl -H "Content-Type: application/json" -X POST -d '{"devicetype": "deconz-mqtt"}' http://<host>/api`

the given username can be used as API key.

## Start the gateway

`java -jar deconz-to-mqtt-gw.jar config.json`

Example configuration:

```json
{
    "deconz-api-key": "123456789",
    "deconz-ip": "192.168.0.100",

    "mqtt-url": "tcp://192.168.0.100:1883",

    "check-connection-interval": 1,

    "full-message-topic": "sensordata",
    "full-message-interval": 60,

    "mapping": {
        "a": [2, 3, 4],
        "b": [5, 6, 7],
        "haus/eg/haustuere": [8]
    }
}
```

every `check-connection-interval` (default: `1`) seconds the websocket 
connection will be checked. When the connection is broken, a new websocket is
created. After creation of a new connection all sensor data will be posted
to ensure the data will be refreshed and is up to date.

When the `full-message-topic` is defined, a full massage in json format will 
be posted every `full-message-interval` seconds (default: `60`).

The mapping maps the target mqtt topic to the deconz ids.
It is possible to map a list of deconz ids to one topic.
This can be used to map multiple properties of a sensor to one 
topic path, for example when using a `Xiaomi Aquara Termperature/Humidity/Pressure` sensor.

## openHAB configuration

see [openHAB.md]