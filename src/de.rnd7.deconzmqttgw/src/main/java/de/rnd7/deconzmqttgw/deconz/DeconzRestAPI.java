package de.rnd7.deconzmqttgw.deconz;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import de.rnd7.deconzmqttgw.config.Config;
import de.rnd7.deconzmqttgw.messages.FullStatusMessage;
import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.messages.StateMessage;

public class DeconzRestAPI {
	private static final MessageParser MESSAGE_PARSER = new MessageParser();

	private static final Logger LOGGER = LoggerFactory.getLogger(DeconzRestAPI.class);

	private final Config config;
	private final EventBus eventBus;
	
	private static class Device {
		int key;
		JSONObject data;
		
		public Device(final int key, final JSONObject data) {
			this.key = key;
			this.data = data;
		}
	}
	
	public DeconzRestAPI(final Config config, final EventBus eventBus) {
		this.config = config;
		this.eventBus = eventBus;
	}

	public void update() {
		try {
			this.initDeconzWebSocketPort();
			this.initDeconzDevices();
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private URL getRestUrl(final String path) throws IOException {
		return new URL(String.format("http://%s/api/%s/%s", 
				this.config.getDeconzIp(), 
				this.config.getDeconzApiKey(),
				path));
	}
	
	
	private void initDeconzWebSocketPort() throws IOException {
		try (InputStream in = this.getRestUrl("config").openStream()) {
			final JSONObject deconzConfig = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
			
			this.config.setDeconzWebSocketPort(deconzConfig.getInt("websocketport"));
		}
	}

	private Stream<Device> streamAllDevices() throws IOException {
		try (InputStream in = this.getRestUrl("sensors").openStream()) {
			final JSONObject deconzDevices = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
			
			return deconzDevices.keySet().stream()
			.map(Integer::parseInt)
			.sorted()
			.map(key -> {
				final JSONObject device = deconzDevices.getJSONObject("" + key);
				return new Device(key, device);
			});
		}
	}
	
	private void initDeconzDevices() throws IOException {
		this.streamAllDevices().forEach(device -> {
			if (device.data.has("name")) {
				final String name = device.data.getString("name");
				
				LOGGER.info("device {}: {}", device.key, name);
				
				this.config.putLookupIfAbsent(device.key, name);
			}
		});
	}
	
	public void fullUpdate() {
		try {
			final FullStatusMessage fullStatusMessage = new FullStatusMessage();
			
			this.streamAllDevices().forEach(device -> {
				if (device.data.has("state")) {
					final StateMessage singelStateMessage = MESSAGE_PARSER.parseStateMessage(device.data.getJSONObject("state"), device.key, device.data.getString("uniqueid"));
					final String topic = singelStateMessage.toTopic(this.config.getLookup());
					
					fullStatusMessage.add(topic, singelStateMessage);
					
					this.eventBus.post(singelStateMessage);
				}
			});
			
			if (this.config.isPublishFullMessage()) {
				this.eventBus.post(fullStatusMessage);
			}
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
