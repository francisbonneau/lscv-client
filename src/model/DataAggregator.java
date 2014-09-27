package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import controller.RenderLoop;

/**
 * The DataAggregator is updated every time a new DataSource Thread receive
 * new incoming data, the dataAggregator then acts as a buffer, temporarily
 * storing the data by its host until the data is consumed
 */
public class DataAggregator implements Observer {
	
	private RenderLoop rl;	
	private HashMap<String, Queue<ArrayList<Event>>> dataBySource;	
	private ArrayList<Thread> dataSourcesThreads;
	
	public DataAggregator(RenderLoop renderLoop) {		
		this.rl = renderLoop;
		
		dataBySource = new HashMap<String, Queue<ArrayList<Event>>>();	
		dataSourcesThreads = new ArrayList<Thread>();
	}

	public void addDataSource(String hostname) {
		
		// allocate a new queue to store incoming data until processing
		Queue<ArrayList<Event>> data = new LinkedList<ArrayList<Event>>();
		dataBySource.put(hostname, data);

		// Create a new thread in charge of running the data source
		DataSourceRedis newDataSource = new DataSourceRedis(rl, hostname);
		newDataSource.addObserver(this);
		Thread t = new Thread(newDataSource);
		t.setName(newDataSource.host);
		t.start();
		dataSourcesThreads.add(t);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// we get the source of the new incoming data
		DataSourceRedis source = (DataSourceRedis) arg0;		
		@SuppressWarnings("unchecked")
		ArrayList<Event> newData = (ArrayList<Event>) arg1;
		// and we store the new data according to it's source host
		dataBySource.get(source.host).add(newData);
	}
	
	public Queue<ArrayList<Event>> getDataForHost(String host) {
		return dataBySource.get(host);
	}
	
}
