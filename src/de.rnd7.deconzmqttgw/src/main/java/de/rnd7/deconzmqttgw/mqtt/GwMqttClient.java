package de.rnd7.deconzmqttgw.mqtt;


import java.util.Optional;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.deconzmqttgw.Config;
import de.rnd7.deconzmqttgw.messages.GwMessage;

public class GwMqttClient {
	private static final int QOS = 2;
	private static final String CLIENTID = "deconz-mqtt-gw";

	private static final Logger LOGGER = LoggerFactory.getLogger(GwMqttClient.class);

	private final MemoryPersistence persistence = new MemoryPersistence();
	private final Object mutex = new Object();
	private final Config config;

	private Optional<MqttClient> client;
	
	public GwMqttClient(Config config) {
		this.config = config;
		this.client = connect();
	}
	
	private Optional<MqttClient> connect() {
		try {
			LOGGER.info("Connecting MQTT client");
			MqttClient result = new MqttClient(this.config.getMqttBroker(), CLIENTID, persistence);
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
			if (!client.filter(MqttClient::isConnected).isPresent()) {
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

	public void publish(GwMessage message) {
		String topic = message.toTopic(config.getLookup());
		String valueString = message.getValueString();
		
		LOGGER.info("{} = {}", topic, valueString);
		
		publish(topic, valueString);
	}
}
