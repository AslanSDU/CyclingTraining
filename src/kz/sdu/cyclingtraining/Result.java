package kz.sdu.cyclingtraining;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class Result {

	private int id;
	private String dt;
	private String duration;
	private String distance;
	private String speed;
	private ArrayList<LatLng> list;

	public Result(int id, String dt, String duration, String distance,
			String speed, ArrayList<LatLng> list) {
		this.id = id;
		this.dt = dt;
		this.duration = duration;
		this.distance = distance;
		this.speed = speed;
		this.list = list;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDT() {
		return dt;
	}

	public void setDT(String dt) {
		this.dt = dt;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public ArrayList<LatLng> getList() {
		return list;
	}

	public void setList(ArrayList<LatLng> list) {
		this.list = list;
	}
}
