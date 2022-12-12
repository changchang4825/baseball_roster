package edu.skku.cs.personalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class PitcherRosterActivity extends AppCompatActivity implements Contract.ContractForHitterView {
    public static final String EXT_POS = "position";
    private PitcherPresenter presenter;
    private SimpleDraweeView p1, p2, p3, p4, p5;
    private TextView textView;
    private Button add_btn, reset_btn;
    private RecyclerView recyclerView;

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    presenter.onIntentResultReturned(intent.getIntExtra("id", 0));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_pitcher_roster);

        presenter = new PitcherPresenter(this, new PitcherModel());

        LinearLayoutManager layoutManager = new LinearLayoutManager(PitcherRosterActivity.this, LinearLayoutManager.HORIZONTAL, false);

        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        textView = findViewById(R.id.candidates_num2);
        add_btn = findViewById(R.id.add_btn2);
        reset_btn = findViewById(R.id.reset_btn2);
        recyclerView = findViewById(R.id.pitcher_listView);
        recyclerView.setLayoutManager(layoutManager);

        presenter.onWindowLoaded();

        p1.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("P1");
        });
        p2.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("P2");
        });
        p3.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("P3");
        });
        p4.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("P4");
        });
        p5.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("P5");
        });
        add_btn.setOnClickListener(view -> {
            presenter.onCandidateButtonTouched("P", -1);
        });
        reset_btn.setOnClickListener(view -> {
            presenter.onResetButtonTouched();
        });
    }

    @Override
    public void generateIntent(String position) {
        Intent intent = new Intent(PitcherRosterActivity.this, SearchActivity.class);
        intent.putExtra(EXT_POS, position);
        ActivityResultLauncher.launch(intent);
    }

    @Override
    public void displayPlayer(int id, String position) {
        Uri uri = Uri.parse("https://img.mlbstatic.com/mlb-photos/image/upload/d_people:generic:headshot:67:current.png/w_213,q_auto:best/v1/people/" + id + "/headshot/67/current");
        if (position.equals("P1")) p1.setImageURI(uri);
        else if (position.equals("P2")) p2.setImageURI(uri);
        else if (position.equals("P3")) p3.setImageURI(uri);
        else if (position.equals("P4")) p4.setImageURI(uri);
        else if (position.equals("P5")) p5.setImageURI(uri);
    }

    @Override
    public void displayCandidate(int num, ArrayList<Integer> idList) {
        Adapter2 adapter = new Adapter2(idList, presenter);
        PitcherRosterActivity.this.runOnUiThread(() -> {
            textView.setText("( " + num + "/7 )");
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void resetView() {
        p1.setImageURI("");
        p2.setImageURI("");
        p3.setImageURI("");
        p4.setImageURI("");
        p5.setImageURI("");
        displayCandidate(0, new ArrayList<>());
    }

    @Override
    public void displayFail() {
        PitcherRosterActivity.this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Already in roster!", Toast.LENGTH_SHORT).show());
    }
}

class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private ArrayList<Integer> idList;
    private PitcherPresenter presenter;

    public Adapter2(ArrayList<Integer> idList, PitcherPresenter presenter) {
        this.idList = idList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.candidate_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage(idList.get(position));
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView simpleDraweeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            simpleDraweeView = itemView.findViewById(R.id.candidate_photo);
        }

        public void setImage(int id) {
            simpleDraweeView.setOnClickListener(view -> {
                presenter.onCandidateButtonTouched("P", id);
            });
            Uri uri = Uri.parse("https://img.mlbstatic.com/mlb-photos/image/upload/d_people:generic:headshot:67:current.png/w_213,q_auto:best/v1/people/" + id + "/headshot/67/current");
            simpleDraweeView.setImageURI(uri);
        }
    }
}