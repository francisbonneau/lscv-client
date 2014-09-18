package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import processing.RenderLoop;

public class SourceAggregator implements Observer {
	
	private RenderLoop renderLoop;
	
	public Queue<ArrayList<Event>> data;
	
	public SourceAggregator(RenderLoop renderLoop) {
		
		this.renderLoop = renderLoop;
				
		for (String host : renderLoop.params.hosts) { 
			System.out.println("starting redis thread for host " + host);
			Thread t = new Thread(new RedisDataSource(host, this));
	        t.start();
	        
	        // TODO create a queue for each host to put data
		}
		
		data = new LinkedList<ArrayList<Event>>();		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		RedisDataSource source = (RedisDataSource) arg0;
		
		@SuppressWarnings("unchecked")
		ArrayList<Event> newData = (ArrayList<Event>) arg1;
		data.add(newData);
	}
	
	public int getLatencyRoundup() { 
		return renderLoop.params.latencyRoundup;
	}

}
