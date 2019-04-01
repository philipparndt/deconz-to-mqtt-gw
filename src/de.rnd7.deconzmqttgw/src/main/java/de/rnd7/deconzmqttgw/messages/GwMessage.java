package de.rnd7.deconzmqttgw.messages;

import java.util.Map;

public abstract class GwMessage {
	private final int id;
	private final String uid;

	public GwMessage(final int id, final String uid) {
		this.id = id;
		this.uid = uid;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getUniqueId() {
		return this.uid;
	}
	
	public abstract Object getValue();
	
	public abstract String getValueString();
	
	protected abstract String getTopicValueName();
	
	public String toTopic(final Map<Integer, String> idLookup) {
		String topicPart = idLookup.get(this.id);
		if (topicPart == null) {
			topicPart = "" + this.id;
		}
		
		final StringBuilder result = new StringBuilder();
		result.append(topicPart);
		
		if (!topicPart.endsWith("/")) {
			result.append("/");
		}
		
		result.append(this.getTopicValueName());
		
		return result.toString();
	}
	
}
