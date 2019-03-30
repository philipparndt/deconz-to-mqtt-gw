package de.rnd7.deconzmqttgw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigParser.class);

	private ConfigParser() {
	}
	
	public static Config parse(File file) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) {
			return parse(in);
		}
	}

	public static Config parse(InputStream in) throws IOException {
		Config config = new Config();
		
		JSONObject jsonObject = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
		
		config.setDeconzApiKey(jsonObject.getString("deconz-api-key"));
		config.setDeconzIp(jsonObject.getString("deconz-ip"));
		config.setMqttBroker(jsonObject.getString("mqtt-url"));

		if (jsonObject.has("mapping")) {
			JSONObject mapping = (JSONObject) jsonObject.get("mapping");
			
			for (String value : JSONObject.getNames(mapping)) {
				JSONArray array = mapping.getJSONArray(value);
				for (Object key : array) {
					config.putLookup((Integer) key, value);
				}
			}
		}
		
		return config;
	}
	
}
