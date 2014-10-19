package model;

import java.util.HashMap;

public class Event {

    public String username;
    public String processName;
    public String syscall;
    public int syscallNumber;

    // Hashmap for the properties, ex : username, processname, system call name
    // usefull for direct access
    public HashMap<String, String> attributes;

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

    public Event(String user, String process, String syscall,
            int syscallNumber, HashMap<Integer, Integer> latencyBreakdown,
            HashMap<Integer, String> arguments ) {

        this.username = user;
        this.processName = process;
        this.syscall = syscall;

        this.attributes = new HashMap<>();
        attributes.put("user", user);
        attributes.put("process", process);
        attributes.put("syscall", syscall);

        this.syscallNumber = syscallNumber;
        this.latencyBreakdown = latencyBreakdown;
        this.arguments = arguments;
    }

    @Override
    public final String toString() {
        return "Events (" + syscallNumber +  ") "
               + "[username=" + username
               + ", processName=" + processName
               + ", syscall=" + syscall + "]";
    }

}
