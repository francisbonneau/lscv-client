package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventProcessor {

    /**
      Example of RAW data to process :
       Key :
       franck.ibus-daemon.poll

       Value :
         83381\tres=1250 data=*3..$7..message..\n
          48949\tres=4 data=:1..\n
         85287\tres=658 data=*3..$7..message..$4.\n
         27340\tres=4 data=:1..\n

        Format : <event latency>\t<event args>\n
     */
    public final ArrayList<Event> processData(final Map<String, String> rawData,
            final float latencyRoundup) {

        ArrayList<Event> processedData = new ArrayList<Event>();

        Iterator<String> eventsKeys = rawData.keySet().iterator();
        Iterator<String> eventsData = rawData.values().iterator();

        // for each event in the arraylist of new data received
        while(eventsKeys.hasNext() && eventsData.hasNext()) {

            String key = eventsKeys.next();    // ex : franck.ibus-daemon.poll
            String value = eventsData.next();

            // breakdown the event key to get the fields
            String[] keyStrings = key.split("\\.");
            String userName = keyStrings[0];
            String processName = keyStrings[1];
            String syscallName = keyStrings[2];

            // the list where to store the latency of each individual system call
            HashMap<Integer, Integer> latencyBreakdown =
                                    new HashMap<Integer, Integer>();

            // the list where to store the args of each individual system call
            HashMap<Integer, String> eventsArguments =
                                    new HashMap<Integer, String>();

            int systemCallsCount = 0;

            // for each system call values received in the event
            // ex :  83381\tres=1250 data=*3..$7..message..\n
            for (String val : value.split("\\n")) {

                // process the values format
                String[] valueStrings = val.split("\\t");

                Double latency = Double.parseDouble(valueStrings[0]);

                String args;
                if (valueStrings.length == 2) {
                    args = "> " + valueStrings[1];
                } else if (valueStrings.length == 3) {
                    args = "> " + valueStrings[1] + " < " + valueStrings[2];
                } else {
                    args = ".";
                }

                int roundedLat = roundLatency(latency, latencyRoundup);

                if (latencyBreakdown.get(roundedLat) == null) {
                    // hashmaps initialisation if not alreay done
                    latencyBreakdown.put(roundedLat, 1);
                    eventsArguments.put(roundedLat, args);
                } else {
                    // increment the existing values
                    int incrementedVal = latencyBreakdown.get(roundedLat) + 1;
                    latencyBreakdown.put(roundedLat, incrementedVal);

                    // and combine the events arguments
                    String previousArgs = eventsArguments.get(roundedLat);
                    eventsArguments.put(roundedLat, previousArgs + "\n" + args);
                }

                systemCallsCount++;
            }

            // and we add the new processed event to the list
            processedData.add(new Event(userName, processName, syscallName,
                    systemCallsCount, latencyBreakdown, eventsArguments));
        }

        return processedData;

    }

    private int roundLatency(final Double latency, final float latencyRoundup) {
        // aggregate the events latency in buckets determined
        // by the latencyRoundup parameter
        Double exp = Math.pow(10, 9 - latencyRoundup);
        Double tmp = Math.floor(latency / exp);
        int roundedVal = (int) (tmp * exp);
        return roundedVal;
    }

}
