package de.rnd7.deconzmqttgw.messages;

import org.json.JSONObject;

public class MessageParser {
	private static final String BUTTON = "buttonevent";
	private static final String PRESSURE = "pressure";
	private static final String HUMIDITY = "humidity";
	private static final String TEMPERATURE = "temperature";
	private static final String OPEN = "open";

	public GwMessage parse(final String messageJson) {
		final JSONObject message = new JSONObject(messageJson);
		
		if (message.has("state")) {
			return this.parseStateMessage(message, (JSONObject) message.get("state"));
		}
		else if (message.has("config")) {
			return this.parseConfigMessage(message, (JSONObject) message.get("config"));
		}
		else if (message.has("e") && message.getString("e").equals("changed")) {
			return this.parseChangeMessage(message, message);
		}
		
		return null;
	}

	private GwMessage parseChangeMessage(final JSONObject message, final JSONObject change) {
		
		if (change.has("name")) {
			final NameChangeMessage result = new NameChangeMessage(message.getInt("id"), message.getString("uniqueid"));
			
			result.setValue(change.getString("name"));
			
			return result;
		}
		
		return null;
	}

	private GwMessage parseStateMessage(final JSONObject message, final JSONObject state) {
		final int id = message.getInt("id");
		final String uniqueId = message.getString("uniqueid");
		return this.parseStateMessage(state, id, uniqueId);
		
	}

	public StateMessage parseStateMessage(final JSONObject state, final int id, final String uniqueId) {
		final StateMessage result = new StateMessage(id, uniqueId);

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
	
	private GwMessage parseConfigMessage(final JSONObject message, final JSONObject state) {
		final int id = message.getInt("id");
		final String uniqueId = message.getString("uniqueid");
		return this.parseConfigMessage(state, id, uniqueId);
		
	}

	public GwMessage parseConfigMessage(final JSONObject config, final int id, final String uniqueId) {
		final ConfigMessage result = new ConfigMessage(id, uniqueId);

		if (config.has("battery")) {
			result.setValue(config.getInt("battery"));
		}
		
		return result;
	}
}
