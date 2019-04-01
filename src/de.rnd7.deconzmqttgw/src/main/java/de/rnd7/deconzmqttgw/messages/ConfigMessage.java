package de.rnd7.deconzmqttgw.messages;

public class ConfigMessage extends GwMessage {

	private int value = -1;
	
	public ConfigMessage(final int id, final String uid) {
		super(id, uid);
	}
	
	@Override
	public Integer getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	@Override
	public String getValueString() {
		return Integer.toString(this.value);
	}

	@Override
	protected String getTopicValueName() {
		return "battery";
	}
	
}
