package kz.sdu.cyclingtraining;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends Activity {

	private GoogleMap map;
	private ArrayList<LatLng> markerPoints;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		markerPoints = new ArrayList<LatLng>();

		Intent intent = getIntent();
		int size = intent.getIntExtra("size", 0);
		Log.d("SIZE", size + "");
		for (int i = 0; i < size; i++) {
			double lat = intent.getDoubleExtra("lat" + i, 0);
			double lon = intent.getDoubleExtra("lon" + i, 0);
			markerPoints.add(new LatLng(lat, lon));
		}

		try {
			if (map == null) {
				map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.result_map)).getMap();
			}

			MarkerOptions start = new MarkerOptions().position(
					markerPoints.get(0)).title("Start");
			map.addMarker(start);

			CameraPosition camera = new CameraPosition.Builder()
					.target(markerPoints.get(0)).zoom(15).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

			MarkerOptions finish = new MarkerOptions().position(
					markerPoints.get(markerPoints.size() - 1)).title("Finish");
			map.addMarker(finish);

			PolylineOptions options = new PolylineOptions();
			options.addAll(markerPoints);
			options.width(5).color(Color.BLUE);
			map.addPolyline(options);
		} catch (Exception e) {
		}
	}
}
