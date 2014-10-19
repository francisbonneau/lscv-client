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
 */
public class DataAggregator implements Observer {

    private MainLoop rl;

    private ArrayList<String> dataSourcesNames;

    private HashMap<String, DataSourceRedis> dataSourceByHost;

    private HashMap<String, Queue<ArrayList<Event>>> dataBySource;

    private ArrayList<Thread> dataSourcesThreads;


    public DataAggregator(final MainLoop renderLoop) {
        this.rl = renderLoop;

        dataSourcesNames = new ArrayList<>();
        dataSourceByHost = new HashMap<String, DataSourceRedis>();

        dataBySource = new HashMap<String, Queue<ArrayList<Event>>>();
        dataSourcesThreads = new ArrayList<Thread>();
    }

    public final void addDataSource(final String hostname) {

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
    public final void update(final Observable arg0, final Object arg1) {
        // we get the source of the new incoming data
        DataSourceRedis source = (DataSourceRedis) arg0;
        @SuppressWarnings("unchecked")
        ArrayList<Event> newData = (ArrayList<Event>) arg1;
        // and we store the new data according to it's source host
        dataBySource.get(source.host).add(newData);

    }

    public final Queue<ArrayList<Event>> getDataForHost(final String host) {
        return dataBySource.get(host);
    }

    public final ArrayList<String> getDataSources() {
        return dataSourcesNames;
    }

    public final void applyFilterToDataSource(final String host, final String filter) {
        dataSourceByHost.get(host).setFilter(filter);
    }

    public final String getDataSourceFilter(final String host) {
        return dataSourceByHost.get(host).getFilter();
    }

}
