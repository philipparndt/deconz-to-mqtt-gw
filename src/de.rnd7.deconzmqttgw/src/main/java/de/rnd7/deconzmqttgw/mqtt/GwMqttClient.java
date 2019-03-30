package de.rnd7.deconzmqttgw.mqtt;


import java.util.Optional;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwMqttClient {
	private static final int QOS = 2;
	private static final String CLIENTID = "deconz-mqtt-gw";

	private static final Logger LOGGER = LoggerFactory.getLogger(GwMqttClient.class);

	private MemoryPersistence persistence = new MemoryPersistence();
	private Optional<MqttClient> client;
	private String broker;
	private Object mutex = new Object();
	
	
	public GwMqttClient(String broker) {
		this.broker = broker;
		this.client = connect();
	}
	
	private Optional<MqttClient> connect() {
		try {
			LOGGER.info("Connecting MQTT client");
			MqttClient result = new MqttClient(broker, CLIENTID, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			result.connect(connOpts);
			
			return Optional.of(result);
		} catch (MqttException e) {
			LOGGER.error(e.getMessage(), e);
			return Optional.empty();
		}
	}

	public void publish(String topic, String value) {
		synchronized(mutex) {
			if (!client.map(MqttClient::isConnected).orElse(false)) {
				client = connect();
			}
			
			this.client.ifPresent(mqttClient -> {
				try {
					MqttMessage message = new MqttMessage(value.getBytes());
					message.setQos(QOS);
					mqttClient.publish(topic, message);
				} catch (MqttException e) {
					LOGGER.error(e.getMessage(), e);
				}
			});
		}
	}
}
