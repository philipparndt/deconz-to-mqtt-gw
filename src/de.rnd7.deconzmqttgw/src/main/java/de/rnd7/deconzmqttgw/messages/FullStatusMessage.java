package de.rnd7.deconzmqttgw.messages;

import java.util.Map;

import org.json.JSONObject;

public class FullStatusMessage extends GwMessage {

	private final JSONObject message = new JSONObject();
	
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
	public String toTopic(final Map<Integer, String> idLookup) {
		return "sensordata";
	}
	
	@Override
	public Object getValue() {
		return null;
	}
	
	public void add(final String topic, final GwMessage state) {
		if (topic != null) {
			this.message.put(topic.replace("/", "."), state.getValue());
		}
	}

}
