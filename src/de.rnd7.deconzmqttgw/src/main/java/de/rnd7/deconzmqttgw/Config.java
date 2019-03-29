package de.rnd7.deconzmqttgw;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private Map<Integer, String> lookup = new HashMap<>();
	
	private String deconzWebSocket;
	private String mqttBroker;
	
	public Map<Integer, String> getLookup() {
		return lookup;
	}
	
	public void putLookup(Integer id, String value) {
		lookup.put(id, value);
	}
	
	public void setDeconzWebSocket(String deconzWebSocket) {
		this.deconzWebSocket = deconzWebSocket;
	}

	public String getDeconzWebSocket() {
		return deconzWebSocket;
	}
	
	public void setMqttBroker(String mqttBroker) {
		this.mqttBroker = mqttBroker;
	}
	
	public String getMqttBroker() {
		return mqttBroker;
	}
	
}
