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

import controller.RenderLoop;

public class DataSourceRedis extends Observable implements Runnable  {

    public String host;
    public String ip;
    public int port;

    private String filter;
    HashMap<String, String> systemInfo;

    private RenderLoop rl;
    private EventProcessor eventProcessor;

    public DataSourceRedis(RenderLoop renderLoop, String host) {

        // Connect to the Redis instance
        this.host = host;
        this.ip = host.split(":")[0];
        this.port = Integer.parseInt(host.split(":")[1]);

        this.rl = renderLoop;
        this.filter = "";

        tryFirstConnexion();

        this.systemInfo = new HashMap<>();
        fetchSystemInfo();

        // instanciate a new data processor
        this.eventProcessor = new EventProcessor();
    }

    private void tryFirstConnexion() {
        try {
            new Jedis(ip, port);
            System.out.println("Successfully connected to " + host);
        } catch (Exception e) {
            System.out.println("Could not connect to " + host);
        }

    }

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
    }

    public String getFilter() {
        Jedis jedis = new Jedis(ip, port);
        this.filter = jedis.get("filter");
        return filter;
    }

    public void setFilter(String newFilter) {
        filter = newFilter;

        new Jedis(ip, port).set("filter", filter);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

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
                        rl.getParams().latencyRoundup);

                // notify the observers
                setChanged();
                notifyObservers(processedData);
            }
        };

        // since once its in streaming mode it cannot do other operations
//		try {
            new Jedis(ip, port).subscribe(jpubSub, "data");
//		} catch (Exception e) {
//			System.out.println("Error encountered when trying to connect to " + host);
//			System.out.println(e.getMessage());
//		}

    }

}
