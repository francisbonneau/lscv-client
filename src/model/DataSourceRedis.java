package model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controller.RenderLoop;

public class DataSourceRedis extends Observable implements Runnable  {
	
	private Jedis jedis;
	public String host;
	public String ip;
	public int port;
	
	private RenderLoop rl;
	private EventProcessor eventProcessor;
	
	public DataSourceRedis(RenderLoop renderLoop, String host) {
		
		// Connect to the Redis instance
		this.host = host;
		this.ip = host.split(":")[0];
		this.port = Integer.parseInt(host.split(":")[1]);
		this.jedis = new Jedis(ip, port);	
		
		// add the data aggregator as observer
		//this.sourceAgg = sourceAgg;
		this.rl = renderLoop;
		//this.addObserver(sourceAgg);
		
		// instanciate a new data processor
		this.eventProcessor = new EventProcessor();
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
				System.out.println("Successfully connected to " + host);
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
				float roundup = rl.params.latencyRoundup;
				ArrayList<Event> processedData = eventProcessor.processData(data, roundup);

				// notify the observers				
				setChanged();
				notifyObservers(processedData);				
			}
		};
		
//		try {
			jedis.subscribe(jpubSub, "data");			
//		} catch (Exception e) {
//			System.out.println("Error encountered when trying to connect to " + host);
//			System.out.println(e.getMessage());
//		}			
		
	}
 		
}
