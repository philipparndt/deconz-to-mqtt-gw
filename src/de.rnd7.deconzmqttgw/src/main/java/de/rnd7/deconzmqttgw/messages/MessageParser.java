package de.rnd7.deconzmqttgw.messages;

import org.json.JSONObject;

public class MessageParser {
	private static final String BUTTON = "buttonevent";
	private static final String PRESSURE = "pressure";
	private static final String HUMIDITY = "humidity";
	private static final String TEMPERATURE = "temperature";
	private static final String OPEN = "open";

	public GwMessage parse(String messageJson) {
		JSONObject message = new JSONObject(messageJson);
		
		if (message.has("state")) {
			return parseStateMessage(message, (JSONObject) message.get("state"));
		}
		else if (message.has("config")) {
			return parseConfigMessage(message, (JSONObject) message.get("config"));
		}
		else if (message.has("e") && message.getString("e").equals("changed")) {
			return parseChangeMessage(message, message);
		}
		
		return null;
	}

	private GwMessage parseChangeMessage(JSONObject message, JSONObject change) {
		
		if (change.has("name")) {
			NameChangeMessage result = new NameChangeMessage(message.getInt("id"), message.getString("uniqueid"));
			
			result.setValue(change.getString("name"));
			
			return result;
		}
		
		return null;
	}

	private GwMessage parseStateMessage(JSONObject message, JSONObject state) {
		int id = message.getInt("id");
		String uniqueId = message.getString("uniqueid");
		return parseStateMessage(state, id, uniqueId);
		
	}

	public StateMessage parseStateMessage(JSONObject state, int id, String uniqueId) {
		StateMessage result = new StateMessage(id, uniqueId);

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
		else if (state.has(BUTTON)) {
			result.setValue(state.getInt(BUTTON))
			.setTopicValueName("button");
		}
		
		return result;
	}
	
	private GwMessage parseConfigMessage(JSONObject message, JSONObject state) {
		ConfigMessage result = new ConfigMessage(message.getInt("id"), message.getString("uniqueid"));

		if (state.has("battery")) {
			result.setValue(state.getInt("battery"));
		}
		
		return result;
		
	}
}
