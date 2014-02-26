package jp.shiolier.android.balancebyopengl;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankingActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// アイコン表示を有効にする
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		setContentView(R.layout.activity_ranking);

		// アイコン表示
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon_ranking);

		PlayDataSQLiteHelper helper = new PlayDataSQLiteHelper(this);
		ArrayList<PlayData> ranking = helper.getDescData();
		RankingAdapter adapter = new RankingAdapter(this, R.layout.list_ranking_row, ranking);
		setListAdapter(adapter);

		helper.close();
	}

	private class RankingAdapter extends ArrayAdapter<PlayData> {
		private ArrayList<PlayData> ranking;
		private LayoutInflater inflater;

		public RankingAdapter(Context context, int resourceId, ArrayList<PlayData> ranking) {
			super(context, resourceId, ranking);
			this.ranking = ranking;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return ranking.size();
		}

		@Override
		public PlayData getItem(int position) {
			return ranking.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_ranking_row, null);
			}

			PlayData data = getItem(position);

			int rank = position + 1;

			TextView txtRank = (TextView)convertView.findViewById(R.id.txt_rank);
			txtRank.setText(Integer.toString(rank));

			TextView txtName = (TextView)convertView.findViewById(R.id.txt_name);
			txtName.setText(data.getName());

			TextView txtPoint = (TextView)convertView.findViewById(R.id.txt_point);
			txtPoint.setText(Integer.toString(data.getPoint()));

			if (rank == 1) {
				convertView.setBackgroundColor(Color.rgb(255, 204, 51));
			} else if (rank == 2) {
				convertView.setBackgroundColor(Color.rgb(198, 198, 198));
			} else if (rank == 3) {
				convertView.setBackgroundColor(Color.rgb(153, 102, 51));
			} else {
				convertView.setBackgroundColor(Color.WHITE);
			}

			return convertView;
		}
	}

}
