package de.rnd7.deconzmqttgw;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private Map<Integer, String> lookup = new HashMap<>();
	
	private String mqttBroker;

	private String deconzApiKey;

	private String deconzIp;

	private int deconzWsPort;
	
	public Map<Integer, String> getLookup() {
		return lookup;
	}
	
	public void putLookup(Integer id, String value) {
		lookup.put(id, value);
	}
	

	public void putLookupIfAbsent(Integer id, String value) {
		if (!lookup.containsKey(id)) {
			putLookup(id, value);
		}
	}
	
	public String getDeconzWebSocket() {
		return String.format("ws://%s:%s", deconzIp, deconzWsPort);
	}
	
	public void setMqttBroker(String mqttBroker) {
		this.mqttBroker = mqttBroker;
	}
	
	public String getMqttBroker() {
		return mqttBroker;
	}

	public void setDeconzApiKey(String deconzApiKey) {
		this.deconzApiKey = deconzApiKey;
	}

	public String getDeconzApiKey() {
		return deconzApiKey;
	}
	
	public void setDeconzIp(String deconzIp) {
		this.deconzIp = deconzIp;
	}
	
	public String getDeconzIp() {
		return deconzIp;
	}

	public void setDeconzWebSocketPort(int deconzWsPort) {
		this.deconzWsPort = deconzWsPort;
	}

	
}
