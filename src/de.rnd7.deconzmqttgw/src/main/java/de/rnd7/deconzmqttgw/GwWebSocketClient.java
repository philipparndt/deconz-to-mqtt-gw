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
import de.rnd7.deconzmqttgw.messages.NameChangeMessage;
import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class GwWebSocketClient extends WebSocketClient  {
	private static final Logger LOGGER = LoggerFactory.getLogger(GwWebSocketClient.class);
	private GwMqttClient mqttClient;
	private Config config;

	public GwWebSocketClient(Config config, URI serverUri) {
		super(serverUri);
		this.config = config;
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
		
		if (message instanceof NameChangeMessage) {
			NameChangeMessage msg = (NameChangeMessage) message;
			this.config.putLookup(msg.getId(), msg.getValue());
			return;
		}
		
		this.mqttClient.publish(message);
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
