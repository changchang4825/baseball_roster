package edu.skku.cs.personalproject;

import static android.app.Activity.RESULT_OK;
import static edu.skku.cs.personalproject.HitterRosterActivity.EXT_POS;
import static edu.skku.cs.personalproject.SearchActivity.EXT_TEAM;
import static edu.skku.cs.personalproject.SearchActivity.EXT_YEAR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity implements Contract.ContractForPlayerListView {
    private PlayerListPresenter presenter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_player_list);

        Intent intent = getIntent();
        String team = intent.getStringExtra(EXT_TEAM);
        String year = intent.getStringExtra(EXT_YEAR);
        String position = intent.getStringExtra(EXT_POS);

        presenter = new PlayerListPresenter(this, new PlayerListModel(team, year, position));
        listView = findViewById(R.id.listView);

        presenter.onWindowLoaded();
    }

    @Override
    public void displayListView(boolean isHitter, ArrayList<PlayerData> playerList) {
        PlayerListViewAdapter adapter = new PlayerListViewAdapter(getApplicationContext(), playerList, isHitter, this);
        PlayerListActivity.this.runOnUiThread(() -> listView.setAdapter(adapter));
    }
}

class PlayerListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PlayerData> playerList;
    private boolean isHitter;
    private Activity PlayerListActivity;

    public PlayerListViewAdapter(Context mContext, ArrayList<PlayerData> playerList, boolean isHitter, Activity PlayerListActivity) {
        this.mContext = mContext;
        this.playerList = playerList;
        this.isHitter = isHitter;
        this.PlayerListActivity = PlayerListActivity;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int i) {
        return playerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.player_item, viewGroup, false);
        }

        PlayerData player = playerList.get(i);
        Uri uri = Uri.parse("https://img.mlbstatic.com/mlb-photos/image/upload/d_people:generic:headshot:67:current.png/w_213,q_auto:best/v1/people/" + player.getId() + "/headshot/67/current");
        SimpleDraweeView simpleDraweeView = view.findViewById(R.id.b1);
        simpleDraweeView.setImageURI(uri);

        TextView textView = view.findViewById(R.id.desc);
        String desc = "Name: " + player.getName();
        if (isHitter == true) desc += "\nAt Bats: " + player.getAtBats() + "\nHits: " + player.getHits() + "\nHome Runs: " + player.getHomeRuns() + "\nAVG: " + player.getAvg() + "\nOPS: " + player.getOps();
        else desc += "\nWins: " + player.getWins() + "\nLosses: " + player.getLosses() + "\nERA: " + player.getEra() + "\nSaves: " + player.getSaves() + "\nHolds: " + player.getHolds();
        textView.setText(desc);

        ConstraintLayout constraintLayout = view.findViewById(R.id.container);
        constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent ();
            intent.putExtra("id", player.getId());
            PlayerListActivity.setResult(RESULT_OK, intent);
            PlayerListActivity.finish();
        });

        return view;
    }
}
