package Data;

import java.util.Observable;
import java.util.Observer;

public class DataAggregator implements Observer {
	
	public DataAggregator(String[] hosts) {		
		
		for (String host : hosts) { 
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
	}

}
