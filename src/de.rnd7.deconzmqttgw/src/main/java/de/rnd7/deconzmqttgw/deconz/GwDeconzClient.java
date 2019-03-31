package de.rnd7.deconzmqttgw.deconz;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import de.rnd7.deconzmqttgw.config.Config;

public class GwDeconzClient {
	private final Config config;
	private final EventBus eventBus;

	private Optional<GwWebSocketClient> client = Optional.empty();
	
	private static final Object MUTEX = new Object();
	private static final Logger LOGGER = LoggerFactory.getLogger(GwDeconzClient.class);
	
	public GwDeconzClient(final Config config, final EventBus eventBus) {
		this.config = config;
		this.eventBus = eventBus;
		
		this.refreshConnection();
	}
	
	public void refreshConnection() {
		this.client = this.connect();
	}

	private Optional<GwWebSocketClient> connect() {
		synchronized(MUTEX) {
			if (!this.isConnected()) {
				try {
					return Optional.of(this.createClient());
				} catch (final Exception e) {
					LOGGER.error(e.getMessage(), e);
					return Optional.empty();
				}
			}
			
			return this.client;
		}
	}
	
	private GwWebSocketClient createClient() {
		new DeconzRestAPI(this.config, this.eventBus).update();
		
		final GwWebSocketClient result = new GwWebSocketClient(this.config, this.eventBus);
		
		result.setConnectionLostTimeout(10);
		result.connect();
		
		return result;
	}
	
	private boolean isConnected() {
		return this.client.filter(cl -> !cl.isClosed() && !cl.isClosing()).isPresent();
	}

}
