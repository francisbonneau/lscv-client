package model;

import java.util.HashMap;

public class Event {
	
	public String username;
	public String processName;
	public String syscall;	
	public int syscallNumber;
	
	// This hash contains the list of all the system call latencies and 
	// the number of system call for each latencies (used for aggregation)
	// ex :  K (syscall latency)   V ( number of syscalls with that latency)
	//              500            10
	//              550             5
	//              600             3
	//              ...            ..	
	public HashMap<Integer, Integer> latencyBreakdown;	
	
	// This hash keep the arguments associated with each syscall, however
	// if there is multiples syscall with a same rounded latency ex 500, the 
	// arguments for each system call are combined with the \n separator
	// ex :  K (syscall latency)   V ( arguments)
	//  	         600           args syscall 1 \n args syscall 2 \n args 3
	//				 ...           ...	
	public HashMap<Integer, String> arguments;
	
	public Event(String username, String processName, String syscallName, 
			int syscallNumber, HashMap<Integer, Integer> latencyBreakdown,
			HashMap<Integer, String> arguments ) {
		
		this.username = username;
		this.processName = processName;
		this.syscall = syscallName;
		this.syscallNumber = syscallNumber;
		this.latencyBreakdown = latencyBreakdown;
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "Events (" + syscallNumber +  ") " + 
				"[username=" + username + 
				", processName=" + processName + 
				", syscall=" + syscall + "]";
	}
	
}
