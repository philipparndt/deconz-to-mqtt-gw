package de.rnd7.deconzmqttgw;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.messages.StateMessage;
import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class DeconzRestAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeconzRestAPI.class);

	private Config config;

	public DeconzRestAPI(Config config) {
		this.config = config;
	}

	public void update(GwMqttClient mqttClient) {
		try {
			initDeconzWebSocketPort(config);
			initDeconzDevices(config, mqttClient);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private static void initDeconzWebSocketPort(Config config) throws IOException {
		URL url = new URL(String.format("http://%s/api/%s/config", 
				config.getDeconzIp(), 
				config.getDeconzApiKey()));
		
		try (InputStream in = url.openStream()) {
			JSONObject deconzConfig = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
			
			config.setDeconzWebSocketPort(deconzConfig.getInt("websocketport"));
		}
	}
	
	private static void initDeconzDevices(Config config, GwMqttClient mqttClient) throws IOException {
		final URL url = new URL(String.format("http://%s/api/%s/sensors", 
				config.getDeconzIp(), 
				config.getDeconzApiKey()));

		final MessageParser messageParser = new MessageParser();
		
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
					 mqttClient.publish(messageParser.parseStateMessage(device.getJSONObject("state"), key, device.getString("uniqueid")));
				}
			});
		}
	}

}
