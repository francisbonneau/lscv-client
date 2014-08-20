package Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;

import static org.msgpack.template.Templates.*;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscribeThread extends Observable implements Runnable  {
	
	Jedis jedis;
	MessagePack msgpack;
	Template<Map<String, String>> mapTmpl;
	
	public RedisSubscribeThread() {
		
		msgpack = new MessagePack();
		mapTmpl = tMap(TString, TString);
				
		// Connect to the Redis instance		
		jedis = new Jedis("10.0.0.60");
		System.out.println("connected to redis");	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		BinaryJedisPubSub binjPubSub = new BinaryJedisPubSub() {
			
			@Override
			public void onUnsubscribe(byte[] arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSubscribe(byte[] arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPUnsubscribe(byte[] arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPSubscribe(byte[] arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPMessage(byte[] arg0, byte[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(byte[] arg0, byte[] arg1) {
				System.out.println("new msg received");	
				System.out.println(new String(arg1));
				
				//byte[] bytes = arg1.toByteArray();
				ByteArrayInputStream in = new ByteArrayInputStream(arg1);
				
//				try {					
//					
//					Map<String, String> dstMap = msgpack.read(arg1, tMap(TString, TString));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				Unpacker unpacker = msgpack.createUnpacker(in);				
				Map<String, String> dstMap;
				try {
					 dstMap = unpacker.read(mapTmpl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				System.out.println("unpacking done");				
			}
		};
		
		String channel = "data";		
		jedis.subscribe(binjPubSub, channel.getBytes());	
		
	}
 		

}
