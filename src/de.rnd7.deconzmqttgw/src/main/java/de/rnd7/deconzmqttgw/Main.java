package de.rnd7.deconzmqttgw;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnt7.deconzmqttgwmqtt.GwMqttClient;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		if (args.length != 1) {
			LOGGER.error("Expected configuration file as argument");
			return;
		}
		
		try {
			final Config config = ConfigParser.parse(new File(args[0]));
			
			new GwWebSocketClient(new URI(config.getDeconzWebSocket()))
			.setMqttClient(new GwMqttClient(config.getMqttBroker()))
			.setSensorTopicLookup(config.getLookup())
			.connect();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
