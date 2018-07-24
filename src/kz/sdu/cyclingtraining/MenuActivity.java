package kz.sdu.cyclingtraining;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends Activity {

	private String[] menu;
	private DrawerLayout drawerLayout;
	private ListView listView;
	private ArrayAdapter<String> adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);

		menu = new String[] { "Main", "Map", "Results" };
		drawerLayout = (DrawerLayout) findViewById(R.id.menu_activity_drawer_layout);
		listView = (ListView) findViewById(R.id.menu_activity_menu);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu);
		listView.setAdapter(adapter);

		Bundle args = new Bundle();
		args.putString("Menu", menu[0]);
		FragmentManager manager = getFragmentManager();
		Fragment fragment = new MainFragment();
		manager.beginTransaction()
				.replace(R.id.menu_activity_content, fragment).commit();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
				drawerLayout.closeDrawers();
				Bundle args = new Bundle();
				args.putString("Menu", menu[pos]);
				FragmentManager manager = getFragmentManager();
				if (pos == 0) {
					Fragment fragment = new MainFragment();
					manager.beginTransaction()
							.replace(R.id.menu_activity_content, fragment)
							.commit();
				} else if (pos == 1) {
					Fragment fragment = new MapsFragment();
					manager.beginTransaction()
							.replace(R.id.menu_activity_content, fragment)
							.commit();
				} else if (pos == 2) {
					Fragment fragment = new ResultFragment();
					manager.beginTransaction()
							.replace(R.id.menu_activity_content, fragment)
							.commit();
				}
			}
		});
	}
}
