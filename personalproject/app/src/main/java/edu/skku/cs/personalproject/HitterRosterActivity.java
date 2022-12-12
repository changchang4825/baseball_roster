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

public class HitterRosterActivity extends AppCompatActivity implements Contract.ContractForHitterView {
    public static final String EXT_POS = "position";
    private HitterPresenter presenter;
    private SimpleDraweeView b1, b2, b3, ss, c, lf, cf, rf, dh;
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
        setContentView(R.layout.activity_hitter_roster);

        presenter = new HitterPresenter(this, new HitterModel());

        LinearLayoutManager layoutManager = new LinearLayoutManager(HitterRosterActivity.this, LinearLayoutManager.HORIZONTAL, false);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        ss = findViewById(R.id.ss);
        c = findViewById(R.id.c);
        lf = findViewById(R.id.lf);
        cf = findViewById(R.id.cf);
        rf = findViewById(R.id.rf);
        dh = findViewById(R.id.dh);
        textView = findViewById(R.id.candidates_num);
        add_btn = findViewById(R.id.add_btn);
        reset_btn = findViewById(R.id.reset_btn);
        recyclerView = findViewById(R.id.hitter_listView);
        recyclerView.setLayoutManager(layoutManager);

        presenter.onWindowLoaded();

        b1.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("1B");
        });
        b2.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("2B");
        });
        b3.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("3B");
        });
        ss.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("SS");
        });
        c.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("C");
        });
        lf.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("LF");
        });
        cf.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("CF");
        });
        rf.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("RF");
        });
        dh.setOnClickListener(view -> {
            presenter.onPlayerButtonTouched("DH");
        });
        add_btn.setOnClickListener(view -> {
            presenter.onCandidateButtonTouched("CD", -1);
        });
        reset_btn.setOnClickListener(view -> {
            presenter.onResetButtonTouched();
        });
    }

    @Override
    public void generateIntent(String position) {
        Intent intent = new Intent(HitterRosterActivity.this, SearchActivity.class);
        intent.putExtra(EXT_POS, position);
        ActivityResultLauncher.launch(intent);
    }

    @Override
    public void displayPlayer(int id, String position) {
        Uri uri = Uri.parse("https://img.mlbstatic.com/mlb-photos/image/upload/d_people:generic:headshot:67:current.png/w_213,q_auto:best/v1/people/" + id + "/headshot/67/current");
        if (position.equals("1B")) b1.setImageURI(uri);
        else if (position.equals("2B")) b2.setImageURI(uri);
        else if (position.equals("3B")) b3.setImageURI(uri);
        else if (position.equals("SS")) ss.setImageURI(uri);
        else if (position.equals("C")) c.setImageURI(uri);
        else if (position.equals("LF")) lf.setImageURI(uri);
        else if (position.equals("CF")) cf.setImageURI(uri);
        else if (position.equals("RF")) rf.setImageURI(uri);
        else if (position.equals("DH")) dh.setImageURI(uri);
    }

    @Override
    public void displayCandidate(int num, ArrayList<Integer> idList) {
        Adapter adapter = new Adapter(idList, presenter);
        HitterRosterActivity.this.runOnUiThread(() -> {
            textView.setText("( " + num + "/4 )");
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void resetView() {
        b1.setImageURI("");
        b2.setImageURI("");
        b3.setImageURI("");
        ss.setImageURI("");
        c.setImageURI("");
        lf.setImageURI("");
        cf.setImageURI("");
        rf.setImageURI("");
        dh.setImageURI("");
        displayCandidate(0, new ArrayList<>());
    }

    @Override
    public void displayFail() {
        HitterRosterActivity.this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Already in roster!", Toast.LENGTH_SHORT).show());
    }
}

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Integer> idList;
    private HitterPresenter presenter;

    public Adapter(ArrayList<Integer> idList, HitterPresenter presenter) {
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
                presenter.onCandidateButtonTouched("CD", id);
            });
            Uri uri = Uri.parse("https://img.mlbstatic.com/mlb-photos/image/upload/d_people:generic:headshot:67:current.png/w_213,q_auto:best/v1/people/" + id + "/headshot/67/current");
            simpleDraweeView.setImageURI(uri);
        }
    }
}