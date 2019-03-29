# Deconz to MQTT Gateway

## Openhab example 

mqtt.things
```
Bridge mqtt:broker:mosquitto [ host="localhost", port="1883", secure=false, clientID="openhab"] {
	Thing topic sensora "Sensor" @ "Home" {
    Channels:
    	Type number : Temperature [stateTopic="a/temperature"]
    	Type number : Humidity [stateTopic="a/humidity"]
		Type number : Pressure [stateTopic="a/pressure"]
		Type number : Battery [stateTopic="a/battery"]
	}
	Thing topic frontdoor "Contact" @ "Home" {
    Channels:
    	Type string : contact [
				stateTopic="haus/eg/haustuere/open", 
				allowedStates="true,false"
			]
	}
}
```

mqtt.items
```
Number SensorA_Temperatur "Temperatur [%.1f °C]" (gHeizung) {
	channel="mqtt:topic:mosquitto:sensora:Temperature"
}
Number SensorA_Humidity "Humidity [%.1f %%]" (gHeizung) {
	channel="mqtt:topic:mosquitto:sensora:Humidity"
}
Number SensorA_Pressure "Air pressure [%.1f hPa]" (gHeizung) {
	channel="mqtt:topic:mosquitto:sensora:Pressure"
}
Number SensorA_Battery "Battery [%.1f %%]" {
	channel="mqtt:topic:mosquitto:sensora:Battery"
}

Contact Contact_FrontDoor "Haustüre" ["ContactSensor"]
String Raw_FrontDoor "Haustüre" {
	channel="mqtt:topic:mosquitto:frontdoor:contact"
}
```

mqtt.rules
```
rule "Raw_FrontDoor" 
when Item Raw_FrontDoor received update
then 
    if (Raw_FrontDoor.state == "false") {
        sendCommand(Contact_FrontDoor, CLOSED)
    }
    else {
        sendCommand(Contact_FrontDoor, OPEN)
    }
end
```