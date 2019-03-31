package de.rnd7.deconzmqttgw.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigParser {

	private static final String CHECK_CONNECTION_INTERVAL = "check-connection-interval";
	private static final String FULL_MESSAGE_TOPIC = "full-message-topic";
	private static final String FULL_MESSAGE_INTERVAL = "full-message-interval";

	private ConfigParser() {
	}
	
	public static Config parse(final File file) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) {
			return parse(in);
		}
	}

	public static Config parse(final InputStream in) throws IOException {
		final Config config = new Config();
		
		final JSONObject jsonObject = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
		
		config.setDeconzApiKey(jsonObject.getString("deconz-api-key"));
		config.setDeconzIp(jsonObject.getString("deconz-ip"));
		config.setMqttBroker(jsonObject.getString("mqtt-url"));
		
		if (jsonObject.has(FULL_MESSAGE_TOPIC)) {
			config.setFullMessageName(jsonObject.getString(FULL_MESSAGE_TOPIC));
		}
		
		if (jsonObject.has(FULL_MESSAGE_INTERVAL)) {
			config.setFullMessageInterval(Duration.ofSeconds(jsonObject.getInt(FULL_MESSAGE_INTERVAL)));
		}
		
		if (jsonObject.has(CHECK_CONNECTION_INTERVAL)) {
			config.setCheckConnectionInterval(Duration.ofSeconds(jsonObject.getInt(CHECK_CONNECTION_INTERVAL)));
		}
		
		if (jsonObject.has("mapping")) {
			final JSONObject mapping = (JSONObject) jsonObject.get("mapping");
			
			for (final String value : JSONObject.getNames(mapping)) {
				final JSONArray array = mapping.getJSONArray(value);
				for (final Object key : array) {
					config.putLookup((Integer) key, value);
				}
			}
		}
		
		return config;
	}
	
}
