package de.rnd7.deconzmqttgw;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnt7.deconzmqttgwmqtt.GwMqttClient;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private GwWebSocketClient client;
	
	private final Config config;
	private final Object mutex = new Object();
	
	@SuppressWarnings("squid:S2189")
	public Main(Config config) {
		this.config = config;
		try {
			final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(this::connect, 500, 500, TimeUnit.MILLISECONDS);
			
			this.client = createClient();
			
			while (true) {
				sleep();
			}
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			LOGGER.debug(e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	private void connect() {
		synchronized(mutex) {
			if (client == null || client.isClosed()) {
				try {
					client = createClient();
				} catch (URISyntaxException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private GwWebSocketClient createClient() throws URISyntaxException {
		GwWebSocketClient result = new GwWebSocketClient(new URI(config.getDeconzWebSocket()))
		.setMqttClient(new GwMqttClient(config.getMqttBroker()))
		.setSensorTopicLookup(config.getLookup());
		
		result.connect();
		
		return result;
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			LOGGER.error("Expected configuration file as argument");
			return;
		}
		
		try {
			new Main(ConfigParser.parse(new File(args[0])));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
	}
}
