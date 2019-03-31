package de.rnd7.deconzmqttgw.deconz;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import de.rnd7.deconzmqttgw.config.Config;
import de.rnd7.deconzmqttgw.messages.GwMessage;
import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.messages.NameChangeMessage;

public class GwWebSocketClient extends WebSocketClient  {
	private static final Logger LOGGER = LoggerFactory.getLogger(GwWebSocketClient.class);

	private final Config config;
	private final EventBus eventBus;

	public GwWebSocketClient(final Config config, final EventBus eventBus) {
		super(config.getDeconzUri());
		
		this.config = config;
		this.eventBus = eventBus;
	}

	@Override
	public void onOpen(final ServerHandshake handshakedata) {
		LOGGER.info("OPEN: {}", handshakedata.getHttpStatus());
	}

	@Override
	public void onMessage(final String messageJson) {
		final GwMessage message = new MessageParser().parse(messageJson);
		if (message == null) {
			LOGGER.error("Unknown message {}", messageJson);
			return;
		}
		
		if (message instanceof NameChangeMessage) {
			final NameChangeMessage msg = (NameChangeMessage) message;
			this.config.putLookup(msg.getId(), msg.getValue());
			return;
		}
		
		this.eventBus.post(message);
	}

	@Override
	public void onClose(final int code, final String reason, final boolean remote) {
		LOGGER.info("CLOSE: {}, {}, {}", code, reason, remote);
	}

	@Override
	public void onError(final Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
	}

}
