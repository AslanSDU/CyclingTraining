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
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapsFragment extends Fragment {

	private GoogleMap map;
	private ArrayList<LatLng> markerPoints;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment, null);

		markerPoints = new ArrayList<LatLng>();

		SharedPreferences sp = getActivity().getSharedPreferences("ToMap",
				Activity.MODE_PRIVATE);
		int current = sp.getInt("current", 0);
		Log.d("In Map", current + "");
		int size = sp.getInt("size", 0);
		for (int i = 0; i < size; i++) {
			String lat = sp.getString("loc" + current + "lat" + i, "");
			String lon = sp.getString("loc" + current + "lon" + i, "");
			markerPoints.add(new LatLng(Double.parseDouble(lat), Double
					.parseDouble(lon)));
		}

		try {
			if (map == null) {
				map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();
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
		return view;
	}
}
