package data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import processing.RenderLoop;

public class DataAggregator implements Observer {
	
	private RenderLoop renderLoop;
	
	public Queue<HashMap<String, HashMap<Integer, Integer>>> data;
	
	public DataAggregator(RenderLoop renderLoop) {
		
		this.renderLoop = renderLoop;
				
		for (String host : renderLoop.params.hosts) { 
			System.out.println("starting redis thread for host " + host);
			Thread t = new Thread(new RedisDataSource(host, this));
	        t.start();
	        
	        // TODO create a queue for each host to put data
		}
		
		data = new LinkedList<HashMap<String, HashMap<Integer, Integer>>>();		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
 
		RedisDataSource source = (RedisDataSource) arg0;
		System.out.println("update from " + source.ip + " received");
				
		@SuppressWarnings("unchecked")
		Map<String, String> data = (Map<String, String>) arg1;
		processData(data);		
	}
	
	public void processData(Map<String, String> dataToProcess) {
		
		float roundup = renderLoop.params.latencyRoundup;
		
		Iterator<String> it = dataToProcess.keySet().iterator();
		Iterator<String> it2 = dataToProcess.values().iterator();
		
		HashMap<String, HashMap<Integer, Integer>> processedData = new
			HashMap<String, HashMap<Integer, Integer>>();
		
		while(it.hasNext()) { 
			
			// example data : 
			// key					value (event latency in nanoseconds)
			// franck.compiz.ioctl	5156,124157,12492,30018,2099,6217,23443,1814,

			String key = it.next();
			String value = it2.next();
			
			// breakdown event latency by process								
			String processName = key.split("\\.")[1];
			
			HashMap<Integer, Integer> latencyBreakdown = new HashMap<Integer, Integer>();
			
			for (String val : value.split(",")) {						
				Double exp = Math.pow(10, 9 - roundup);
				Double tmp = Math.floor(Double.parseDouble(val) / exp);
				int roundedVal = (int) (tmp * exp);

				if (latencyBreakdown.get(roundedVal) == null) {
					latencyBreakdown.put(roundedVal, 1);
				} else {
					int newVal = latencyBreakdown.get(roundedVal) + 1;
					latencyBreakdown.put(roundedVal, newVal);
				}
			}		
			
			processedData.put(processName, latencyBreakdown);			
		}
		
		data.add(processedData);
	}

}
