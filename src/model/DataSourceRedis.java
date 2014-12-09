package model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The DataSourceRedis is a thread whose job is to connect to a remote Redis
 * instance, and subscribe to a specific pub-sub channel. Incoming data will be
 * parsed by the EventProcessor and send to the DataAggregator via update()
 * @author Francis Bonneau
 */
public class DataSourceRedis extends Observable implements Runnable  {

	// The data aggregator where to send the received data
	private DataAggregator dataAgg;

	// The event processor, used to parse the incoming data into event objets
    private EventProcessor eventProcessor;

    public String uri;	// the remote server uri, ex: 127.0.0.1:6973
    public String ip;	// the remote server IP addr, ex: 127.0.0.1
    public int port;	// the remote server port, ex: 6973

    private String activeFilter; // the sysdig filter to apply to the remote server

    // Information about the remote server, ex cpu type, cpu count, memory, etc.
    HashMap<String, String> systemInfo;

    // Constructor
    public DataSourceRedis(DataAggregator dataAgg, String host) {

    	// Split the URI into parts
        this.uri = host;
        this.ip = host.split(":")[0];
        this.port = Integer.parseInt(host.split(":")[1]);

        this.dataAgg = dataAgg;
        this.activeFilter = "";
    }

    // Initiate a conexion to the remote Redis instance, and fetch info about
    // the remote server if the connexion is sucessfull
    public boolean initiateRemoteConnexion() {

        if (testConnexion()) {

        	this.systemInfo = new HashMap<>();
            fetchSystemInfo();

            // instanciate a new data processor
            this.eventProcessor = new EventProcessor();

            return true;
        } else {
        	return false;
        }
    }

    // Test the connexion to the remote Redis instance
    private boolean testConnexion() {
        try {
            Jedis j = new Jedis(ip, port);
            System.out.println("Successfully connected to " + uri);
            j.close();
            return true;
        } catch (Exception e) {
            System.out.println("Could not connect to " + uri);
            return false;
        }

    }

    // Get information about the remote server, the information is
    // simply stored by the server app into specific Redis keys
    private void fetchSystemInfo() {

        Jedis jedis = new Jedis(ip, port);
        String hostname = jedis.get("hostname");
        systemInfo.put("hostname", hostname);
        System.out.print("hostname: " + hostname);

        String uptime = jedis.get("uptime");
        systemInfo.put("uptime", uptime);
        System.out.print("uptime: " + uptime);

        String cpuInfo = jedis.get("cpu_info");
        systemInfo.put("cpu_info", cpuInfo);
        System.out.print("cpu info: " + cpuInfo);

        String cpuCount = jedis.get("cpu_count");
        systemInfo.put("cpu_count", cpuCount);
        System.out.print("cpu count: " + cpuCount);

        String memory = jedis.get("memory");
        systemInfo.put("memory", memory);
        System.out.print("memory: " + memory);

        jedis.close();
    }

    // Get the currently active Sysdig filter on the remote server
    public String getFilter() {
        Jedis jedis = new Jedis(ip, port);
        this.activeFilter = jedis.get("filter");
        jedis.close();
        return activeFilter;
    }

    // Set the remote server active Sysdig filter
    public void setFilter(String newFilter) {
        Jedis jedis = new Jedis(ip, port);
        jedis.set("filter", newFilter);
        activeFilter = newFilter;
        jedis.close();
    }

    // Thread loop
    @Override
    public void run() {

    	Jedis j;

    	// Anonymous class to handle the different scenarios of the Redis
    	// publish-subscribe protocol
        JedisPubSub jpubSub = new JedisPubSub() {

            @Override
            public void onUnsubscribe(String arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSubscribe(String arg0, int arg1) {
                System.out.println("Ready to receive incoming data");
            }

            @Override
            public void onPUnsubscribe(String arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPSubscribe(String arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPMessage(String arg0, String arg1, String arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onMessage(String arg0, String arg1) {
                // decode the json data
                Type mapType = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> data = new Gson().fromJson(arg1, mapType);

                // process the data
                ArrayList<Event> processedData = eventProcessor.processData(data,
                        dataAgg.getParams().latencyRoundup);

                // notify the observers
                setChanged();
                notifyObservers(processedData);
            }
        };

        // TODO better errors handling
		try {
            j = new Jedis(ip, port);
            j.subscribe(jpubSub, "data");
		} catch (Exception e) {
			System.out.println("Error encountered when trying to connect to " + uri);
			System.out.println(e.getMessage());
		}

    }

}
