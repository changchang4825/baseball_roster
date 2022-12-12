package edu.skku.cs.personalproject;

import static edu.skku.cs.personalproject.HitterRosterActivity.EXT_POS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity implements Contract.ContractForSearchView {
    public static final String EXT_TEAM = "team";
    public static final String EXT_YEAR = "year";
    private SearchPresenter presenter;
    private Button btn;

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String position = intent.getStringExtra(EXT_POS);
        btn = findViewById(R.id.search);

        presenter = new SearchPresenter(this, new SearchModel(-1, -1, position));

        Spinner team_selector = findViewById(R.id.team_selector);
        Spinner year_selector = findViewById(R.id.year_selector);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, presenter.requestTeams());
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, presenter.requestYears());
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team_selector.setAdapter(arrayAdapter1);
        year_selector.setAdapter(arrayAdapter2);

        team_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onSpinnerSelected(false, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                presenter.onSpinnerSelected(false, -1);
            }
        });
        year_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onSpinnerSelected(true, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                presenter.onSpinnerSelected(true, -1);
            }
        });
        btn.setOnClickListener(view -> {
            presenter.onSearchButtonTouched();
        });
    }

    @Override
    public void generateIntent(String[] values) {
        Intent intent = new Intent(SearchActivity.this, PlayerListActivity.class);
        intent.putExtra(EXT_TEAM, values[0]);
        intent.putExtra(EXT_YEAR, values[1]);
        intent.putExtra(EXT_POS, values[2]);
        ActivityResultLauncher.launch(intent);
    }

    @Override
    public void displayWarningMessage() {
        SearchActivity.this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Select team and year!!!", Toast.LENGTH_SHORT).show());
    }
}
