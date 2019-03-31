package de.rnd7.deconzmqttgw;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.deconzmqttgw.messages.FullStatusMessage;
import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.messages.StateMessage;
import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class DeconzRestAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeconzRestAPI.class);

	private Config config;

	private GwMqttClient mqttClient;

	public DeconzRestAPI(Config config, GwMqttClient mqttClient) {
		this.config = config;
		this.mqttClient = mqttClient;
	}

	public void update() {
		try {
			initDeconzWebSocketPort();
			initDeconzDevices();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private void initDeconzWebSocketPort() throws IOException {
		URL url = new URL(String.format("http://%s/api/%s/config", 
				config.getDeconzIp(), 
				config.getDeconzApiKey()));
		
		try (InputStream in = url.openStream()) {
			JSONObject deconzConfig = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
			
			config.setDeconzWebSocketPort(deconzConfig.getInt("websocketport"));
		}
	}
	
	private void initDeconzDevices() throws IOException {
		final URL url = new URL(String.format("http://%s/api/%s/sensors", 
				config.getDeconzIp(), 
				config.getDeconzApiKey()));

		final MessageParser messageParser = new MessageParser();
		
		final FullStatusMessage fullStatusMessage = new FullStatusMessage();
		
		try (InputStream in = url.openStream()) {
			JSONObject deconzDevices = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
			
			deconzDevices.keySet().stream()
			.map(Integer::parseInt)
			.sorted().forEach(key -> {
				JSONObject device = deconzDevices.getJSONObject("" + key);
				
				if (device.has("name")) {
					String name = device.getString("name");
					
					LOGGER.info("device {}: {}", key, name);
					
					config.putLookupIfAbsent(key, name);
				}
				
				if (device.has("state")) {
					final StateMessage singelStateMessage = messageParser.parseStateMessage(device.getJSONObject("state"), key, device.getString("uniqueid"));
					final String topic = singelStateMessage.toTopic(config.getLookup());
					
					fullStatusMessage.add(topic, singelStateMessage);
					
					 mqttClient.publish(singelStateMessage);
				}
			});
		}
		
		mqttClient.publish(fullStatusMessage);
	}
	
	public void fullUpdate() {
		try {
			initDeconzDevices();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
