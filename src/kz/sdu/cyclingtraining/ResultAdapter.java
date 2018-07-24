package kz.sdu.cyclingtraining;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<Result> {

	private Context context;
	private ArrayList<Result> list;

	public ResultAdapter(Context context, ArrayList<Result> list) {
		super(context, R.layout.result_list_item_view, list);
		this.context = context;
		this.list = list;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.result_list_item_view, null);
		TextView data = (TextView) view
				.findViewById(R.id.result_list_item_view_data);
		TextView duration = (TextView) view
				.findViewById(R.id.result_list_item_view_duration);
		TextView distance = (TextView) view
				.findViewById(R.id.result_list_item_view_distance);
		TextView speed = (TextView) view
				.findViewById(R.id.result_list_item_view_speed);
		Button map = (Button) view.findViewById(R.id.result_list_item_view_map);
		map.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(context, MapActivity.class);
				ArrayList<LatLng> locList = list.get(position).getList();
				intent.putExtra("size", locList.size());
				for (int i = 0; i < locList.size(); i++) {
					intent.putExtra("lat" + i, locList.get(i).latitude);
					intent.putExtra("lon" + i, locList.get(i).longitude);
				}
				context.startActivity(intent);
			}
		});

		data.setText(list.get(position).getDT());
		duration.setText(list.get(position).getDuration());
		distance.setText(list.get(position).getDistance());
		speed.setText(list.get(position).getSpeed());
		return view;
	}
}
