package kz.sdu.cyclingtraining;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ResultFragment extends Fragment {

	private DatabaseHelper db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DatabaseHelper(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.result_fragment, null);

		ListView list = (ListView) view.findViewById(R.id.result_list);
		ArrayList<Result> result = db.getAllResults();
		ResultAdapter adapter = new ResultAdapter(getActivity(), result);
		list.setAdapter(adapter);

		return view;
	}
}
