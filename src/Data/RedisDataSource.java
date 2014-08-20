package Data;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Observable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RedisDataSource extends Observable implements Runnable  {
	
	private Jedis jedis;
	public String host;
	public String ip;
	public int port;
	
	public RedisDataSource(String host, DataAggregator da) {
		
		// Connect to the Redis instance
		this.host = host;
		this.ip = host.split(":")[0];
		this.port = Integer.parseInt(host.split(":")[1]);
		this.jedis = new Jedis(ip, port);
		System.out.println("connected to redis on " + host);	
		
		// add the data aggregator as observer
		this.addObserver(da);		
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
				// TODO Auto-generated method stub			
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
				
				// notify the observers				
				setChanged();
				notifyObservers(data);
				
			}
		};
		
		jedis.subscribe(jpubSub, "data");	
		
	}
 		
}
