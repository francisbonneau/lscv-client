package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventProcessor {
	
	public ArrayList<Event> processData(Map<String, String> rawData,
			float latencyRoundup) {		
		
		ArrayList<Event> processedData = new ArrayList<Event>();
		
		Iterator<String> events = rawData.keySet().iterator();
		Iterator<String> eventsData = rawData.values().iterator();		
		
		while(events.hasNext()) {
			
			/**
			  Example data : 
			   Key :		   				   
			   franck.ibus-daemon.poll
			   				    
			   Value :
			     83381\tres=1250 data=*3..$7..message..\n 
			 	 48949\tres=4 data=:1..\n
		 		 85287\tres=658 data=*3..$7..message..$4.\n 
				 27340\tres=4 data=:1..\n			 	 
				
				Format : <event latency>\t<event args>\n 
			 */
	
			String key = events.next();
			String value = eventsData.next();
			
			// breakdown the event key to get the fields
			String[] splittedKey = key.split("\\.");
			String userName = splittedKey[0];
			String processName = splittedKey[1];
			String syscallName = splittedKey[2];
			
			// new Event to store the data
			Event event = new Event(userName, processName, syscallName);
					
			HashMap<Integer, Integer> latencyBreakdown = new HashMap<Integer, Integer>();
			HashMap<Integer, String> eventsArguments = new HashMap<Integer, String>();
					
			for (String val : value.split("\\n")) {
							
				// process the values format
				String[] splittedVal = val.split("\\t");
											
				String latency = splittedVal[0];
				String args;
				if (splittedVal.length == 3) {
					 args = splittedVal[1] + " " + splittedVal[2];
				} else {
					args = "";
				}
				
				// aggregate the events latency in buckets determined
				// by the latencyRoundup parameter
				
				Double exp = Math.pow(10, 9 - latencyRoundup);
				Double tmp = Math.floor(Double.parseDouble(latency) / exp);
				int roundedVal = (int) (tmp * exp);
				
				if (latencyBreakdown.get(roundedVal) == null) {
					// hashmaps initialisation
					latencyBreakdown.put(roundedVal, 1);
					eventsArguments.put(roundedVal, "");
				} else {
					// increment the existing values
					int newVal = latencyBreakdown.get(roundedVal) + 1;
					latencyBreakdown.put(roundedVal, newVal);
					
					// and combine the events arguments				
					String oldArgs = eventsArguments.get(roundedVal);
					eventsArguments.put(roundedVal, oldArgs + "\n" + args);
				}			
								
			}
			
			event.latencyBreakdown = latencyBreakdown;
			event.arguments = eventsArguments;
			processedData.add(event);
		}
		
		return processedData;
		
	}
}
