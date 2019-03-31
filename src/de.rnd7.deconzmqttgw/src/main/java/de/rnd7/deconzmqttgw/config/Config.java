package de.rnd7.deconzmqttgw.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import de.rnd7.deconzmqttgw.GwRuntimeExecption;

public class Config {
	private final Map<Integer, String> lookup = new HashMap<>();
	
	private String mqttBroker;
	private String deconzApiKey;
	private String deconzIp;
	private int deconzWsPort;
	
	private Duration checkConnectionInterval = Duration.ofSeconds(1);
	
	private String fullMessageName = null;
	private Duration fullMessageInterval = Duration.ofSeconds(60);
	
	public Map<Integer, String> getLookup() {
		return this.lookup;
	}
	
	public void putLookup(final Integer id, final String value) {
		this.lookup.put(id, value);
	}

	public void putLookupIfAbsent(final Integer id, final String value) {
		if (!this.lookup.containsKey(id)) {
			this.putLookup(id, value);
		}
	}
	
	public String getDeconzWebSocket() {
		return String.format("ws://%s:%s", this.deconzIp, this.deconzWsPort);
	}
	
	public void setMqttBroker(final String mqttBroker) {
		this.mqttBroker = mqttBroker;
	}
	
	public String getMqttBroker() {
		return this.mqttBroker;
	}

	public void setDeconzApiKey(final String deconzApiKey) {
		this.deconzApiKey = deconzApiKey;
	}

	public String getDeconzApiKey() {
		return this.deconzApiKey;
	}
	
	public void setDeconzIp(final String deconzIp) {
		this.deconzIp = deconzIp;
	}
	
	public String getDeconzIp() {
		return this.deconzIp;
	}

	public void setDeconzWebSocketPort(final int deconzWsPort) {
		this.deconzWsPort = deconzWsPort;
	}

	public URI getDeconzUri() {
		try {
			return new URI(String.format("ws://%s:%s", this.deconzIp, this.deconzWsPort));
		} catch (final URISyntaxException e) {
			throw new GwRuntimeExecption(e);
		}
	}

	public void setFullMessageName(final String fullMessageName) {
		this.fullMessageName = fullMessageName;
	}
	
	public String getFullMessageName() {
		return this.fullMessageName;
	}
	
	public boolean isPublishFullMessage() {
		return this.fullMessageName != null;
	}

	public void setFullMessageInterval(final Duration fullMessageInterval) {
		this.fullMessageInterval = fullMessageInterval;
	}
	
	public Duration getFullMessageInterval() {
		return this.fullMessageInterval;
	}
	
	public void setCheckConnectionInterval(final Duration checkConnectionInterval) {
		this.checkConnectionInterval = checkConnectionInterval;
	}
	
	public Duration getCheckConnectionInterval() {
		return this.checkConnectionInterval;
	}
}
