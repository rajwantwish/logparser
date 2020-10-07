package com.test.logparser.json.model;

public class Output {
    public String id;
    public long duration;
    public String Type;
    public String Host;
    public boolean Alert;

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
    }
    public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
    }
    public String getType() {
		return Type;
	}
	public void setType(String type) {
		this.Type = type;
	}
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		this.Host = host;
    }
    public boolean getAlert() {
        return Alert;
    }
    public void setAlert(boolean alert) {
        this.Alert = alert;
    }

    @Override
	public String toString(){
		return getId() + ", "+String.valueOf(getDuration())+", "+getType()+", "+getHost()+", "+String.valueOf(getAlert());
	}

}
