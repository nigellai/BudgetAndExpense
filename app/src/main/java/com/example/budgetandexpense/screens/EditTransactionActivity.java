package com.example.budgetandexpense.screens;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Category;
import com.example.budgetandexpense.model.Transaction;
import com.example.budgetandexpense.network.APIManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.budgetandexpense.network.APIConstant.API_TAG;
import static com.example.budgetandexpense.network.APIConstant.WRONG_RATE;

public class EditTransactionActivity extends AppCompatActivity implements Callback, AdapterView.OnItemSelectedListener  {
    private ToggleButton currencyToggleButton;
    private Button transactionDatePickerButton;
    private Spinner transactionCategorySpinner;
    private EditText transactionValueEditText;
    private Button transactionAddButton;
    private Button transactionCancelButton;
    private TextView usdHint;
    private TextView transfferedNZD;
    private double usd_to_nzd_transfer = WRONG_RATE;
    private boolean use_usd = false;
    private double transfered_to_nzd = 0;
    private Date transactionDate;
    private ArrayList<String> categoryNameList;
    private String transactionCateogryName;

    private void backPressed() {
        if ( getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        onBackPressed();
    }

    private void setUpViews() {
       currencyToggleButton = (ToggleButton)findViewById(R.id.currency_toggle);
       transactionDatePickerButton = (Button)findViewById(R.id.transactionDatePicker);
       transactionCategorySpinner = (Spinner)findViewById(R.id.category_spinner);
       transactionCategorySpinner.setOnItemSelectedListener(this);
       transactionCategorySpinner.setEnabled(false);
       transactionValueEditText = (EditText)findViewById(R.id.transaction_value);
       transactionValueEditText.setEnabled(false);
       transactionAddButton = (Button)findViewById(R.id.buttonTransactionAdd);
       transactionCancelButton = (Button)findViewById(R.id.buttonTransactionCancel);
       usdHint = (TextView)findViewById(R.id.usd_hint);
       usdHint.setVisibility(currencyToggleButton.isChecked() ? View.VISIBLE : View.INVISIBLE);
       transfferedNZD = (TextView)findViewById(R.id.transaction_transffered_NZD);
       transactionAddButton = (Button)findViewById(R.id.buttonTransactionAdd);
       transactionCancelButton = (Button)findViewById(R.id.buttonTransactionCancel);
    }

    private void setUpListeners() {
        currencyToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                use_usd = isChecked;
                usdHint.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        transactionValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(use_usd) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    transfered_to_nzd = Double.parseDouble(transactionValueEditText.getText().toString()) * usd_to_nzd_transfer;
                    transfered_to_nzd = Double.parseDouble(df.format(transfered_to_nzd));
                    transfferedNZD.setText("$NZD" + transfered_to_nzd);
                } else {
                    transfered_to_nzd = Double.parseDouble(transactionValueEditText.getText().toString());
                }
            }
        });

        transactionDatePickerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTransactionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String date = year + "/" + (month + 1) + "/" + day;
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                                    transactionDate = formatter.parse(date);
                                } catch (Exception e) {
                                    transactionDate = new Date();
                                }
                            }
                        }, 2019, 0, 0);

                datePickerDialog.show();
            }
        });

        transactionAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(transactionDate != null && transfered_to_nzd != 0 && transactionCateogryName !=null) {
                    Transaction.createTrasaction(transactionDate, transfered_to_nzd, transactionCateogryName);
                    backPressed();
                }
            }
        });

        transactionCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });


    }

    private void init() {
        APIManager.getDataAsync(this);

        List<Category> categoryList = Category.getAllCategories();
        categoryNameList = new ArrayList<String>();

        for(Category category : categoryList) {
            categoryNameList.add(category.name);
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categoryNameList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionCategorySpinner.setAdapter(aa);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        setUpViews();
        setUpListeners();
        init();
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
                        transactionCategorySpinner.setEnabled(true);
                        transactionValueEditText.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        transactionCateogryName = categoryNameList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
