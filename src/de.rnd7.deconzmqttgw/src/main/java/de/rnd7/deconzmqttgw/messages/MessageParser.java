package de.rnd7.deconzmqttgw.messages;

import org.json.JSONObject;

public class MessageParser {
	private static final String PRESSURE = "pressure";
	private static final String HUMIDITY = "humidity";
	private static final String TEMPERATURE = "temperature";
	private static final String OPEN = "open";

	public DeconzMessage parse(String messageJson) {
		JSONObject message = new JSONObject(messageJson);
		
		if (message.has("state")) {
			return parseStateMessage(message, (JSONObject) message.get("state"));
		}
		else if (message.has("config")) {
			return parseConfigMessage(message, (JSONObject) message.get("config"));
		}
		
		
		return null;
	}

	private DeconzMessage parseStateMessage(JSONObject message, JSONObject state) {
		StateMessage result = new StateMessage(message.getInt("id"), message.getString("uniqueid"));

		if (state.has(OPEN)) {
			result.setValue(state.get(OPEN))
			.setTopicValueName(OPEN);
		}
		else if (state.has(TEMPERATURE)) {
			result.setValue(state.getInt(TEMPERATURE) / 100d)
			.setTopicValueName(TEMPERATURE);
		}
		else if (state.has(HUMIDITY)) {
			result.setValue(state.getInt(HUMIDITY) / 100d)
			.setTopicValueName(HUMIDITY);
		}
		else if (state.has(PRESSURE)) {
			result.setValue(state.getInt(PRESSURE))
			.setTopicValueName(PRESSURE);
		}
		
		return result;
		
	}
	
	private DeconzMessage parseConfigMessage(JSONObject message, JSONObject state) {
		ConfigMessage result = new ConfigMessage(message.getInt("id"), message.getString("uniqueid"));

		if (state.has("battery")) {
			result.setValue(state.getInt("battery"));
		}
		
		return result;
		
	}
}