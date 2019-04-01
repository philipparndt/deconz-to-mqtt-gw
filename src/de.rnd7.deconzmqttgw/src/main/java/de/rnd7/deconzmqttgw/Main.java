package de.rnd7.deconzmqttgw;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import de.rnd7.deconzmqttgw.config.Config;
import de.rnd7.deconzmqttgw.config.ConfigParser;
import de.rnd7.deconzmqttgw.deconz.DeconzRestAPI;
import de.rnd7.deconzmqttgw.deconz.GwDeconzClient;
import de.rnd7.deconzmqttgw.mqtt.GwMqttClient;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	private final Config config;

	private final EventBus eventBus = new EventBus();
	
	@SuppressWarnings("squid:S2189")
	public Main(final Config config) {
		this.config = config;

		this.eventBus.register(new GwMqttClient(config));
		
		try {
			final GwDeconzClient deconzClient = new GwDeconzClient(config, this.eventBus);
			
			this.scheduleTasks(config, deconzClient);
			
			while (true) {
				this.sleep();
			}
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void scheduleTasks(final Config config, final GwDeconzClient deconzClient) {
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		final long checkConnectionInterval = config.getCheckConnectionInterval().getSeconds();
		LOGGER.error("Scheduling connection check every {} seconds", checkConnectionInterval);
		executor.scheduleAtFixedRate(deconzClient::refreshConnection, checkConnectionInterval, checkConnectionInterval, TimeUnit.SECONDS);

		if (config.isPublishFullMessage()) {
			final long fullMessageInterval = config.getFullMessageInterval().getSeconds();
			LOGGER.error("Scheduling full message every {} seconds", fullMessageInterval);
			
			executor.scheduleAtFixedRate(this::fullUpdate, 0, fullMessageInterval, TimeUnit.SECONDS);
		}
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (final InterruptedException e) {
			LOGGER.debug(e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}
	
	private void fullUpdate() {
		new DeconzRestAPI(this.config, this.eventBus).fullUpdate();
	}

	public static void main(final String[] args) {
		if (args.length != 1) {
			LOGGER.error("Expected configuration file as argument");
			return;
		}
		
		try {
			new Main(ConfigParser.parse(new File(args[0])));
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
	}
}
