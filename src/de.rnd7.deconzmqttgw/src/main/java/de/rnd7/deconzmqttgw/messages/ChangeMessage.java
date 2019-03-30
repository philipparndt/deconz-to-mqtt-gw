package de.rnd7.deconzmqttgw.messages;

public class ChangeMessage extends DeconzMessage {

	private String value;

	public ChangeMessage(int id, String uid) {
		super(id, uid);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getValueString() {
		return value;
	}


	@Override
	protected String getTopicValueName() {
		return null;
	}

}
