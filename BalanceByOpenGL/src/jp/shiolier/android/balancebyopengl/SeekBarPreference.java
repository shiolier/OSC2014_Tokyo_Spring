package jp.shiolier.android.balancebyopengl;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Preference設定用SeekBarクラス
 * 参考
 * 		http://typea.info/blg/glob/2010/10/android-android-hacks.html
 *
 */

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {
	private static final int MAX_PROGRESS = 255;
	private static final int DEFAULT_PROGRESS = 200; 
	private int currentProgress;
	private int oldProgress;


	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.pref_seekbar);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, DEFAULT_PROGRESS);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// 既に保存されている場合はその値を使い、保存されていない場合はデフォルト値を使う
		if (restorePersistedValue) {
			currentProgress = getPersistedInt(currentProgress);
		} else {
			currentProgress = (Integer)defaultValue;
			persistInt(currentProgress);
		}
		oldProgress = currentProgress;
	}

	@Override
	protected void onBindView(View view) {
		SeekBar seekbar = (SeekBar)view.findViewById(R.id.pref_seekbar);
		if (seekbar != null) {
			seekbar.setProgress(currentProgress);
			seekbar.setMax(MAX_PROGRESS);
			seekbar.setOnSeekBarChangeListener(this);
		}
		super.onBindView(view);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int progress = seekBar.getProgress();

		currentProgress = (callChangeListener(progress)) ? progress : oldProgress;

		persistInt(currentProgress);
		oldProgress = currentProgress;
	}
}
