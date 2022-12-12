package edu.skku.cs.personalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Contract.ContractForInitialView {
    private InitialPresenter presenter;
    private Button hitter_btn, pitcher_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new InitialPresenter(this, new InitialModel());

        hitter_btn = findViewById(R.id.hitter_btn);
        pitcher_btn = findViewById(R.id.pitcher_btn);

        hitter_btn.setOnClickListener(view -> {
            presenter.onHitterButtonTouched();
        });
        pitcher_btn.setOnClickListener(view -> {
            presenter.onPitcherButtonTouched();
        });
    }

    @Override
    public void displayView(boolean mode) {
        Intent intent;
        if (mode == false) intent = new Intent(MainActivity.this, HitterRosterActivity.class);
        else intent = new Intent(MainActivity.this, PitcherRosterActivity.class);
        startActivity(intent);
    }
}