package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import controller.RenderLoop;

public class DataAggregator implements Observer {
	
	private RenderLoop rl;
	public Queue<ArrayList<Event>> data;
	
	public ArrayList<Thread> dataSourcesThreads;
	
	public DataAggregator(RenderLoop renderLoop) {
		
		this.rl = renderLoop;		
		data = new LinkedList<ArrayList<Event>>();
		dataSourcesThreads = new ArrayList<Thread>();
	}
	
	// Create a new thread in charge of running the data source
	public void addDataSource(String hostname) {
		DataSourceRedis newDataSource = new DataSourceRedis(rl, hostname);
		newDataSource.addObserver(this);
		Thread t = new Thread(newDataSource);
		t.setName(newDataSource.host);
		t.start();
		dataSourcesThreads.add(t);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		DataSourceRedis source = (DataSourceRedis) arg0;
		
		@SuppressWarnings("unchecked")
		ArrayList<Event> newData = (ArrayList<Event>) arg1;
		data.add(newData);
	}
	
}
