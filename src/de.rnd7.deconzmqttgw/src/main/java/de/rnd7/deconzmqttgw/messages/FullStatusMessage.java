package de.rnd7.deconzmqttgw.messages;

import java.util.Map;

import org.json.JSONObject;

public class FullStatusMessage extends GwMessage {

	private JSONObject message = new JSONObject();
	
	public FullStatusMessage() {
		super(-1, "");
	}

	@Override
	public String getValueString() {
		return this.message.toString();
	}

	@Override
	protected String getTopicValueName() {
		return "";
	}

	@Override
	public String toTopic(Map<Integer, String> idLookup) {
		return "sensordata";
	}
	
	public void add(String topic, StateMessage state) {
		if (topic != null) {
			this.message.put(topic.replace("/", "."), state.getValue());
		}
	}

}
