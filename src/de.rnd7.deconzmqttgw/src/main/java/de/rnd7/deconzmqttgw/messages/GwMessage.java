package de.rnd7.deconzmqttgw.messages;

import java.util.Map;

public abstract class GwMessage {
	private final int id;
	private final String uid;

	public GwMessage(int id, String uid) {
		this.id = id;
		this.uid = uid;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUniqueId() {
		return uid;
	}
	
	public abstract String getValueString();
	
	protected abstract String getTopicValueName();
	
	public String toTopic(Map<Integer, String> idLookup) {
		String topicPart = idLookup.get(id);
		if (topicPart == null) {
			topicPart = "" + id;
		}
		
		StringBuilder result = new StringBuilder();
		result.append(topicPart);
		
		if (!topicPart.endsWith("/")) {
			result.append("/");
		}
		
		result.append(getTopicValueName());
		
		return result.toString();
	}
	
}
