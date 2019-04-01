package de.rnd7.deconzmqttgw.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class MessageParserTest {
	@Test
	public void test_change_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("name-change.txt"));
		assertTrue(message instanceof NameChangeMessage);
		
		final NameChangeMessage msg = (NameChangeMessage) message;
		assertEquals(10, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-88-1000", msg.getUniqueId());
		assertEquals("home/remote", msg.getValue());
	}
	
	@Test
	public void test_button_pressed() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("button.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(10, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-01-1000", msg.getUniqueId());
		assertEquals(1002, msg.getValue());
		assertEquals("10/button", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_door_open_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("door-open.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(8, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-01-0006", msg.getUniqueId());
		assertEquals(true, msg.getValue());
		assertEquals("8/open", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_door_close_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("door-close.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(8, msg.getId());
		assertEquals("00:11:22:33:44:55:66:77-01-0006", msg.getUniqueId());
		assertEquals(false, msg.getValue());
		assertEquals("8/open", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_temperature_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("temperature.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(5, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(22.32, msg.getValue());
		assertEquals("5/temperature", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_humidity_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("humidity.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(6, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(36.19, msg.getValue());
		assertEquals("6/humidity", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_pressure_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("pressure.txt"));
		assertTrue(message instanceof StateMessage);
		
		final StateMessage msg = (StateMessage) message;
		assertEquals(7, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(971, msg.getValue());
		assertEquals("7/pressure", msg.toTopic(new HashMap<>()));
	}
	
	@Test
	public void test_battery_message() throws Exception {
		final GwMessage message = new MessageParser().parse(this.read("battery.txt"));
		assertTrue(message instanceof ConfigMessage);
		
		final ConfigMessage msg = (ConfigMessage) message;
		assertEquals(5, msg.getId());
		assertEquals("77:66:55:44:33:22:11:00-01-0402", msg.getUniqueId());
		assertEquals(91, (int) msg.getValue());
		assertEquals("5/battery", msg.toTopic(new HashMap<>()));
	}
	
	private String read(final String name) throws IOException {
		try (InputStream in = MessageParserTest.class.getResourceAsStream(name)) {
			return IOUtils.toString(in, StandardCharsets.UTF_8);
		}
	}
}
