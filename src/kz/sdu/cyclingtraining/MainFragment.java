package kz.sdu.cyclingtraining;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainFragment extends Fragment implements SensorEventListener,
		LocationListener {

	private Chronometer duration;
	private TextView distance;
	private TextView remaining;
	private TextView speed;
	private TextView doing_now;
	private EditText goal;
	private SensorManager sensorManager;
	private double acc = 0;
	private double x = 0;
	private double y = 0;
	private double z = 0;
	private Typeface fontNormal;
	private Typeface fontBold;
	private int countSpeed = 0;
	private double totalSpeed = 0;

	private LocationManager locationManager;
	private ArrayList<LatLng> locationList;
	private String provider;
	private float total_distance;

	private boolean play;

	private DatabaseHelper db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		play = false;

		fontNormal = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/arial.ttf");
		fontBold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/arialb.ttf");

		locationList = new ArrayList<LatLng>();

		db = new DatabaseHelper(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, null);

		TextView durationText = (TextView) view.findViewById(R.id.durationText);
		durationText.setTypeface(fontNormal);
		duration = (Chronometer) view.findViewById(R.id.duration);
		duration.setTypeface(fontBold);

		TextView distanceText = (TextView) view.findViewById(R.id.distanceText);
		distanceText.setTypeface(fontNormal);
		distance = (TextView) view.findViewById(R.id.distance);
		distance.setTypeface(fontBold);

		TextView remainingText = (TextView) view
				.findViewById(R.id.remainingText);
		remainingText.setTypeface(fontNormal);
		remaining = (TextView) view.findViewById(R.id.remaining);
		remaining.setTypeface(fontNormal);

		TextView speedText = (TextView) view.findViewById(R.id.speedText);
		speedText.setTypeface(fontNormal);
		speed = (TextView) view.findViewById(R.id.speed);
		speed.setTypeface(fontNormal);

		doing_now = (TextView) view.findViewById(R.id.doing_now);
		doing_now.setTypeface(fontNormal);

		TextView goalText = (TextView) view.findViewById(R.id.goalText);
		goalText.setTypeface(fontNormal);
		goal = (EditText) view.findViewById(R.id.goal);
		goal.setTypeface(fontNormal);
		TextView goalKm = (TextView) view.findViewById(R.id.goalKm);
		goalKm.setTypeface(fontNormal);

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			Log.d("LOCATION", provider);
			onLocationChanged(location);
		} else {
			Log.d("LOCATION", "False");
		}
		total_distance = 0;

		final ImageView ppButton = (ImageView) view
				.findViewById(R.id.play_pause_button);
		ppButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!play && goal.getText().toString().length() > 0) {
					ppButton.setImageResource(R.drawable.pause);
					remaining.setText("0 m");
					goal.setEnabled(false);
					play = true;

					duration.setBase(SystemClock.elapsedRealtime());
					duration.start();
				} else {
					ppButton.setImageResource(R.drawable.play);
					goal.setEnabled(true);
					play = false;

					duration.stop();

					String average_speed = String.format("%.3f km/h",
							totalSpeed / countSpeed);

					String dt = "";
					Calendar calendar = Calendar.getInstance();
					dt = addZero(calendar.get(Calendar.DAY_OF_MONTH)) + "."
							+ addZero(calendar.get(Calendar.MONTH) + 1) + "."
							+ calendar.get(Calendar.YEAR);

					db.addResult(new Result(0, dt, duration.getText()
							.toString(), distance.getText().toString(),
							average_speed, locationList));

					SharedPreferences sp = getActivity().getSharedPreferences(
							"ToMap", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					int current = sp.getInt("current", 0);
					editor.putInt("size", locationList.size());
					for (int i = 0; i < locationList.size(); i++) {
						editor.putString("loc" + (current + 1) + "lat" + i,
								locationList.get(i).latitude + "");
						editor.putString("loc" + (current + 1) + "lon" + i,
								locationList.get(i).longitude + "");
					}
					editor.putInt("current", current + 1);
					editor.commit();
				}
			}
		});

		return view;
	}

	private String addZero(int i) {
		String s = "";
		if (i < 10) {
			s = "0";
		}
		s += i;
		return s;
	}

	public void onLocationChanged(Location l) {
		if (play) {
			double lat = l.getLatitude();
			double lon = l.getLongitude();
			if (locationList.size() == 0) {
				locationList.add(new LatLng(lat, lon));
				distance.setText("0 m");
			} else {
				Location one = new Location("One");
				one.setLatitude(locationList.get(locationList.size() - 1).latitude);
				one.setLongitude(locationList.get(locationList.size() - 1).longitude);
				locationList.add(new LatLng(lat, lon));
				Location two = new Location("Two");
				two.setLatitude(lat);
				two.setLongitude(lon);
				float distance_between_two_points = one.distanceTo(two);
				total_distance += distance_between_two_points;
				String distance_in_string = String.format("%.3f",
						total_distance);
				distance.setText(distance_in_string + " m");
				int myGoal = Integer.parseInt(goal.getText().toString());
				String remainingString = String.format("%.2f m", myGoal
						- total_distance);
				remaining.setText(remainingString);
			}
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onResume() {
		super.onResume();
		sensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		Sensor acceletometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, acceletometer,
				SensorManager.SENSOR_DELAY_NORMAL);

		locationManager.requestLocationUpdates(provider, 100, 1, this);
	}

	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);

		locationManager.removeUpdates(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (play) {
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
				return;
			}
			if (acc > 1) {
				double kmh = acc / 3.6;
				speed.setText(String.format("%.2f", kmh) + " km/h");
			} else {
				speed.setText("0 km/h");
			}
			double dX = Math.abs(x - event.values[0]);
			double dY = Math.abs(y - event.values[1]);
			double dZ = Math.abs(z - event.values[2]);

			x = event.values[0];
			y = event.values[1];
			z = event.values[2];

			acc = Math.abs(Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)
					+ Math.pow(dZ, 2)));

			totalSpeed += acc;
			countSpeed++;
		}
	}
}
