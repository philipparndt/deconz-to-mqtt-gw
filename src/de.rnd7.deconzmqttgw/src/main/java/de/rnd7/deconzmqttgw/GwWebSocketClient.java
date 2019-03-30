package de.rnd7.deconzmqttgw;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.deconzmqttgw.messages.DeconzMessage;
import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class GwWebSocketClient extends WebSocketClient  {
	private static final Logger LOGGER = LoggerFactory.getLogger(GwWebSocketClient.class);
	private Map<Integer, String> lookup=new HashMap<>();
	private GwMqttClient mqttClient;

	public GwWebSocketClient(URI serverUri) {
		super(serverUri);
	}
	
	public GwWebSocketClient setSensorTopicLookup(Map<Integer, String> lookup) {
		this.lookup = lookup;
		
		return this;
	}

	public GwWebSocketClient setMqttClient(GwMqttClient mqttClient) {
		this.mqttClient = mqttClient;
		
		return this;
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		LOGGER.info("OPEN: {}", handshakedata.getHttpStatus());
	}

	@Override
	public void onMessage(String messageJson) {

		DeconzMessage message = new MessageParser().parse(messageJson);
		if (message == null) {
			LOGGER.error("Unknown message {}", messageJson);
			return;
		}
		
		String topic = message.toTopic(lookup);
		String valueString = message.getValueString();
		
		LOGGER.info("{} = {}", topic, valueString);
		
		this.mqttClient.publish(topic, valueString);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		LOGGER.info("CLOSE: {}, {}, {}", code, reason, remote);
	}

	@Override
	public void onError(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
	}

}
