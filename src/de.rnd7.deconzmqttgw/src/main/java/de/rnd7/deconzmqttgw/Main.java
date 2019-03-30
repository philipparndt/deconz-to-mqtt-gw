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

import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private GwWebSocketClient deconzClient;
	private final GwMqttClient mqttClient;
	
	private final Config config;
	private final Object mutex = new Object();
	
	@SuppressWarnings("squid:S2189")
	public Main(Config config) {
		this.config = config;
		this.mqttClient = new GwMqttClient(config.getMqttBroker());
		
		try {
			final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(this::connect, 500, 500, TimeUnit.MILLISECONDS);
			
			this.deconzClient = createClient();
			
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

	private boolean isConnected() {
		return deconzClient != null && !deconzClient.isClosed();
	}

	private void connect() {
		synchronized(mutex) {
			if (!isConnected()) {
				try {
					deconzClient = createClient();
				} catch (URISyntaxException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private GwWebSocketClient createClient() throws URISyntaxException {
		GwWebSocketClient result = new GwWebSocketClient(config, new URI(config.getDeconzWebSocket()))
		.setMqttClient(mqttClient)
		.setSensorTopicLookup(config.getLookup());
		result.setConnectionLostTimeout(10);
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
