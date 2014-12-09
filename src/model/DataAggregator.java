package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import controller.MainLoop;

/**
 * The DataAggregator is updated every time a new DataSource Thread receive
 * new incoming data, the dataAggregator then acts as a buffer, temporarily
 * storing the data in separate queues until the data is consumed
 * @author Francis Bonneau
 */
public class DataAggregator implements Observer {

    private MainLoop rl; // Reference to the main loop

    // The list of the remote servers sending data
    private ArrayList<String> dataSourcesNames;

    // The list of the DataSource objets linked to the remote servers
    //	each DataSource is a separate thread who only handle incoming data
    private HashMap<String, DataSourceRedis> dataSourceByHost;
    private ArrayList<Thread> dataSourcesThreads;

    // A queue that keep the actual data of the remote servers, more data
    // is added to it by the DataSources objets using the update method.
    // Data is removed from the list as the clients (Emitters) consume it.
    private HashMap<String, Queue<ArrayList<Event>>> dataBySource;

    // Initialize the data aggregator
    public DataAggregator(MainLoop renderLoop) {
        this.rl = renderLoop;

        dataSourcesNames = new ArrayList<>();
        dataSourceByHost = new HashMap<String, DataSourceRedis>();

        dataBySource = new HashMap<String, Queue<ArrayList<Event>>>();
        dataSourcesThreads = new ArrayList<Thread>();
    }

    /**
     * Add a new data source to the data aggregator
     * @param hostname the hostname of the remote server to add
     */
    public void addDataSource(final String hostname) {

    	// Instanciate the new data Source
    	DataSourceRedis newDataSource = new DataSourceRedis(this, hostname);

    	// Try to connect to the remote server first
    	boolean connexionSuccedded = newDataSource.initiateRemoteConnexion();

        if (connexionSuccedded) {

            newDataSource.addObserver(this);
            dataSourceByHost.put(hostname, newDataSource);

            dataSourcesNames.add(hostname);

            // allocate a new queue to store incoming data until processing
            Queue<ArrayList<Event>> data = new LinkedList<ArrayList<Event>>();
            dataBySource.put(hostname, data);

            // Create a new thread in charge of running the data source
            Thread t = new Thread(newDataSource);
            t.setName(newDataSource.uri);
            t.start();
            dataSourcesThreads.add(t);
        }
    }

    /**
     * Remove a data source from the list
     * @param hostname the remote server hostname to remove
     */
    public void removeDataSource (final String hostname) {
    	// TODO
    }

    // Update the data aggregator with new data
    @Override
    public void update(final Observable arg0, final Object arg1) {

    	// we get the source of the new incoming data
        DataSourceRedis source = (DataSourceRedis) arg0;

        @SuppressWarnings("unchecked")
        ArrayList<Event> newData = (ArrayList<Event>) arg1;

        // and we store the new data according to it's source host
        dataBySource.get(source.uri).add(newData);
    }

    // Return the application global parameters
    public Params getParams() {
    	return rl.getParams();
    }

    // Get the current data for the specified server
    public final Queue<ArrayList<Event>> getDataForHost(final String hostname) {
        return dataBySource.get(hostname);
    }

    // Return the list of all the servers (data sources) hostnames
    public final ArrayList<String> getDataSources() {
        return dataSourcesNames;
    }

	// Send a message to the remote server to apply the specified filter
	public final void applyFilterToDataSource(final String host,
														final String filter) {
		dataSourceByHost.get(host).setFilter(filter);
	}

	// Return the filter currently applied to the specified host
    public final String getDataSourceFilter(final String host) {
        return dataSourceByHost.get(host).getFilter();
    }

}
