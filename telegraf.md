# Openhab example 

## telegraf.conf
```conf
# Global tags can be specified here in key="value" format.
[global_tags]

# Configuration for telegraf agent
[agent]
  interval = "10s"
  round_interval = true
  metric_batch_size = 1000
  metric_buffer_limit = 10000
  collection_jitter = "0s"
  flush_interval = "10s"
  flush_jitter = "0s"
  precision = "1s"
  debug = true
  quiet = false
  logfile = ""
  hostname = ""
  omit_hostname = false

# Configuration for sending metrics to InfluxDB
[[outputs.influxdb]]
  urls = ["http://127.0.0.1:8086"]

  database = "mqttdata"
  username = "USERNAME"
  password = "PASSWORD"
  insecure_skip_verify = false

# Read metrics from MQTT topic(s)
[[inputs.mqtt_consumer]]
  servers = ["tcp://127.0.0.1:1883"]
  qos = 0
  connection_timeout = "30s"
  topics = [
    "sensordata",
  ]

  persistent_session = true
  client_id = "telegrafmqtt"
  data_format = "json"
```
