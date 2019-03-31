package de.rnd7.deconzmqttgw.messages;

public class ConfigMessage extends GwMessage {

	private int value = -1;
	
	public ConfigMessage(int id, String uid) {
		super(id, uid);
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String getValueString() {
		return Integer.toString(value);
	}

	@Override
	protected String getTopicValueName() {
		return "battery";
	}
	
}
