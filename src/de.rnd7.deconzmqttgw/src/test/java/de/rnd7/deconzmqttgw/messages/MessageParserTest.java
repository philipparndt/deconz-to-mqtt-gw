package de.rnd7.deconzmqttgw.messages;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.rnd7.deconzmqttgw.messages.ConfigMessage;
import de.rnd7.deconzmqttgw.messages.DeconzMessage;
import de.rnd7.deconzmqttgw.messages.MessageParser;
import de.rnd7.deconzmqttgw.messages.StateMessage;

public class MessageParserTest {
	@Test
	public void test_door_open_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("door-open.txt"));
		assertTrue(message instanceof StateMessage);
		
		StateMessage msg = (StateMessage) message;
		assertEquals(8, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-01-0006", msg.getUniqueId());
		assertEquals(true, msg.getValue());
		assertEquals("8/open", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_door_close_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("door-close.txt"));
		assertTrue(message instanceof StateMessage);
		
		StateMessage msg = (StateMessage) message;
		assertEquals(8, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-01-0006", msg.getUniqueId());
		assertEquals(false, msg.getValue());
		assertEquals("8/open", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_temperature_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("temperature.txt"));
		assertTrue(message instanceof StateMessage);
		
		StateMessage msg = (StateMessage) message;
		assertEquals(5, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(22.32, msg.getValue());
		assertEquals("5/temperature", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_humidity_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("humidity.txt"));
		assertTrue(message instanceof StateMessage);
		
		StateMessage msg = (StateMessage) message;
		assertEquals(6, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(36.19, msg.getValue());
		assertEquals("6/humidity", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_pressure_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("pressure.txt"));
		assertTrue(message instanceof StateMessage);
		
		StateMessage msg = (StateMessage) message;
		assertEquals(7, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(971, msg.getValue());
		assertEquals("7/pressure", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_battery_message() throws Exception {
		DeconzMessage message = new MessageParser().parse(read("battery.txt"));
		assertTrue(message instanceof ConfigMessage);
		
		ConfigMessage msg = (ConfigMessage) message;
		assertEquals(5, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(91, msg.getValue());
		assertEquals("5/battery", msg.toTopic(new HashMap<>()));
	}
	
	private String read(String name) throws IOException {
		try (InputStream in = MessageParserTest.class.getResourceAsStream(name)) {
			return IOUtils.toString(in, StandardCharsets.UTF_8);
		}
	}
}
