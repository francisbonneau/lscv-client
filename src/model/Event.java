package model;

import java.util.HashMap;

public class Event {
	
	public String username;
	public String processName;
	public String syscall;
	
	public HashMap<Integer, Integer> latencyBreakdown;	
	public HashMap<Integer, String> arguments;
	
	public Event(String user, String process, String syscall) {
		this.username = user;
		this.processName = process;
		this.syscall = syscall;
	}
	
}
