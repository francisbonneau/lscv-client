package data;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import processing.RenderLoop;

public class DataAggregator implements Observer {
	
	private RenderLoop renderLoop;
	
	public DataAggregator(RenderLoop renderLoop) {
		
		this.renderLoop = renderLoop;
				
		for (String host : renderLoop.params.hosts) { 
			System.out.println("starting redis thread for host " + host);
			Thread t = new Thread(new RedisDataSource(host, this));
	        t.start();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		RedisDataSource source = (RedisDataSource) arg0;
		System.out.println("update from " + source.ip + " received");
				
		Map<String, String> data = (Map<String, String>) arg1;
		processData(data);
		
	}
	
	public void processData(Map<String, String> data) {
		
		Iterator<String> it = data.values().iterator();
		Iterator<String> it2 = data.keySet().iterator();
		
		while(it.hasNext()) { 
			
			// example data : 
			// key					value (event latency in nanoseconds)
			// franck.compiz.ioctl	5156,124157,12492,30018,2099,6217,23443,1814,

			String key = it.next();
			String value = it2.next();
			
			
			
		
			data.put(key, value);
			
		}
		
		
		
	}

}
