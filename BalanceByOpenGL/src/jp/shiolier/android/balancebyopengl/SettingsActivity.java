package jp.shiolier.android.balancebyopengl;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {
	private PrefFragment prefFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_setting);

		prefFragment = new PrefFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefFragment).commit();
	}

	public static class PrefFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}

