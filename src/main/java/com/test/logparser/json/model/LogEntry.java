package com.test.logparser.json.model;
import java.util.Comparator;

public class LogEntry implements Comparator<LogEntry>{

    private String id; // the unique event identifier
    private String state; // whether the event was started or finished (can have values "STARTED" or "FINISHED"
    private long timestamp; // the timestamp of the event in milliseconds
    private String type; // type of log
    private String host; // hostname

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getTimeStamp() {
		return timestamp;
	}
	public void setTimeStamp(long timestamp) {
		this.timestamp = timestamp;
	}
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString(){
		return getId() + ", "+getState()+", "+String.valueOf(getTimeStamp())+", "+getType()+", "+getHost();
	}

	@Override
	public int compare(LogEntry o1, LogEntry o2) {
		return o1.getId().compareTo(o2.getId());
	}
}
