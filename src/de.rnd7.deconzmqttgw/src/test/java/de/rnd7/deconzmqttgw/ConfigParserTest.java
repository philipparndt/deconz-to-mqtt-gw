package de.rnd7.deconzmqttgw;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class ConfigParserTest {
	@Test
	public void test_parse_example_config() throws Exception {
		try (InputStream in = ConfigParserTest.class.getResourceAsStream("config-example.json")) {
			Config config = ConfigParser.parse(in);
			assertEquals("ws://192.168.0.1:433", config.getDeconzWebSocket());
			assertEquals("tcp://192.168.0.1:1883", config.getMqttBroker());
			
			assertEquals("sensorA", config.getLookup().get(1));
			assertEquals("sensorA", config.getLookup().get(2));
			assertEquals("sensorA", config.getLookup().get(3));

			assertEquals("sensorB", config.getLookup().get(4));
			assertEquals("sensorB", config.getLookup().get(5));
			assertEquals("sensorB", config.getLookup().get(6));
		}
	}
	
	@Test
	public void test_parse_example_config_without_mapping() throws Exception {
		try (InputStream in = ConfigParserTest.class.getResourceAsStream("config-example-without-mapping.json")) {
			Config config = ConfigParser.parse(in);
			assertEquals("ws://192.168.0.1:433", config.getDeconzWebSocket());
			assertEquals("tcp://192.168.0.1:1883", config.getMqttBroker());
			
			assertNull(config.getLookup().get(1));
		}
	}
}
