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
 * storing the data in separate queues until the data is consumed
 */
public class DataAggregator implements Observer {
	
	private RenderLoop rl;
	
	private ArrayList<String> dataSourcesNames;
	
	private HashMap<String, DataSourceRedis> dataSourceByHost;
	
	private HashMap<String, Queue<ArrayList<Event>>> dataBySource;
	
	private ArrayList<Thread> dataSourcesThreads;
	
	
	public DataAggregator(RenderLoop renderLoop) {		
		this.rl = renderLoop;
		
		dataSourcesNames = new ArrayList<>();
		dataSourceByHost = new HashMap<String, DataSourceRedis>();
		
		dataBySource = new HashMap<String, Queue<ArrayList<Event>>>();	
		dataSourcesThreads = new ArrayList<Thread>();
	}

	public void addDataSource(String hostname) {
		
		dataSourcesNames.add(hostname);
		
		// allocate a new queue to store incoming data until processing
		Queue<ArrayList<Event>> data = new LinkedList<ArrayList<Event>>();
		dataBySource.put(hostname, data);

		// Instanciate the new data Source
		DataSourceRedis newDataSource = new DataSourceRedis(rl, hostname);
		newDataSource.addObserver(this);		
		dataSourceByHost.put(hostname, newDataSource);

		// Create a new thread in charge of running the data source
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
	
	public ArrayList<String> getDataSources() {
		return dataSourcesNames;
	}
	
	public void applyFilterToDataSource(String host, String filter) {
		dataSourceByHost.get(host).setFilter(filter);		
	}
	
	public String getDataSourceFilter(String host) {
		return dataSourceByHost.get(host).getFilter();
	}
	
}
