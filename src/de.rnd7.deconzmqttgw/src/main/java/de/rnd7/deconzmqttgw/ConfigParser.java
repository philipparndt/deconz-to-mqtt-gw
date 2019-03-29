package de.rnd7.deconzmqttgw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigParser {
	
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
		
		config.setDeconzWebSocket(jsonObject.getString("deconz-url"));
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