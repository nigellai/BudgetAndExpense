package com.example.budgetandexpense.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Category;
import com.example.budgetandexpense.network.APIManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.budgetandexpense.network.APIConstant.API_TAG;
import static com.example.budgetandexpense.network.APIConstant.WRONG_RATE;

public class EditCategoryActivity extends AppCompatActivity implements Callback, AdapterView.OnItemSelectedListener {
    private ToggleButton categoryToggleButton;
    private EditText editTextName;
    private EditText editTextBudget;
    private Button addButton;
    private Button cancelButton;
    private TextView usdHint;
    private TextView transfferedNZD;
    private Spinner categoryColourSpinner;
    private double usd_to_nzd_transfer = WRONG_RATE;
    private boolean use_usd = false;
    private double transfered_to_nzd = 0;
    private ArrayList<Integer> colorSelections;
    private int selectedColour;

    private void backPressed() {
        if ( getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        onBackPressed();
    }

    private void setUpViews() {
        categoryToggleButton = (ToggleButton)findViewById(R.id.category_currency_toggle);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextBudget = (EditText)findViewById(R.id.editTextBudget);
        editTextBudget.setEnabled(false);
        addButton = (Button)findViewById(R.id.buttonAdd);
        cancelButton = (Button)findViewById(R.id.buttonCancel);
        usdHint = (TextView)findViewById(R.id.cateogory_usd_hint);
        usdHint.setVisibility(categoryToggleButton.isChecked() ? View.VISIBLE : View.INVISIBLE);
        transfferedNZD = (TextView)findViewById(R.id.category_transffered_NZD);
        categoryColourSpinner = (Spinner)findViewById(R.id.category_color_spinner);
        categoryColourSpinner.setOnItemSelectedListener(this);
    }

    private void setUpListeners() {

        categoryToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                use_usd = isChecked;
                usdHint.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        editTextBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(use_usd) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    transfered_to_nzd = Double.parseDouble(editTextBudget.getText().toString()) * usd_to_nzd_transfer;
                    transfered_to_nzd = Double.parseDouble(df.format(transfered_to_nzd));
                    transfferedNZD.setText("$NZD" + transfered_to_nzd);
                } else {
                    transfered_to_nzd = Double.parseDouble(editTextBudget.getText().toString());
                    transfered_to_nzd = Double.parseDouble(editTextBudget.getText().toString());
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String categoryName = editTextName.getText().toString();
                Category.createCategory(categoryName, selectedColour, transfered_to_nzd);
                backPressed();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backPressed();
            }
        });
    }

    private void init() {
        APIManager.getDataAsync(this);
        ArrayList<String> colorList = new ArrayList<String>();
        colorSelections = new ArrayList<Integer>();
        colorList.add("RED");
        colorSelections.add(Color.RED);
        colorList.add("BLUE");
        colorSelections.add(Color.BLUE);
        colorList.add("GRAY");
        colorSelections.add(Color.GRAY);
        colorList.add("GREEN");
        colorSelections.add(Color.GREEN);
        colorList.add("YELLOW");
        colorSelections.add(Color.YELLOW);
        colorList.add("BLACK");
        colorSelections.add(Color.BLACK);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, colorList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryColourSpinner.setAdapter(aa);
    }

    private void retrievingPassedData() {
        Intent intent = getIntent();
        String retrievedName = intent.getStringExtra("name") ;
        Double retrievedBudget = intent.getDoubleExtra("budget", 0);
        Integer retrievedColor = intent.getIntExtra("color", 0);

        if(retrievedName !=null) {
            editTextName.setText(retrievedName);
            editTextBudget.setText(retrievedBudget.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        setUpViews();
        setUpListeners();
        init();
        retrievingPassedData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedColour = colorSelections.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(API_TAG, e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()){
            usd_to_nzd_transfer = APIManager.getRate(response);
            if (usd_to_nzd_transfer == WRONG_RATE){
                Log.e(API_TAG, "rate parsed wrong " + WRONG_RATE);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextBudget.setEnabled(true);
                    }
                });
            }
        }

    }
}
