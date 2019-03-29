package de.rnd7.deconzmqttgw.messages;

import java.util.Objects;

public class StateMessage extends DeconzMessage {

	private Object value;
	private String topicValueName;
	
	public StateMessage(int id, String uid) {
		super(id, uid);
	}

	public StateMessage setValue(Object value) {
		this.value = value;
		
		return this;
	}

	public StateMessage setTopicValueName(String topicValueName) {
		this.topicValueName = topicValueName;
		
		return this;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public String getValueString() {
		return Objects.toString(value);
	}

	@Override
	protected String getTopicValueName() {
		return topicValueName;
	}

}
