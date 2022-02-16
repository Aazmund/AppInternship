package com.example.appinternship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ConverterActivity extends AppCompatActivity {

    private ArrayList<String> spinnerList = new ArrayList<>();
    private ArrayList<String> valueList = new ArrayList<>();
    private Button button;
    private TextView valueView;
    private TextView resultView;
    private int position = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        Bundle arguments = getIntent().getExtras();

        if(!arguments.isEmpty()){
            String[] savedData = arguments.getString("KEY_CURRENCY").split("@");
            for (int i = 0; i < savedData.length - 1; i++){
                spinnerList.add(savedData[i]);
                valueList.add(savedData[i+1]);
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = findViewById(R.id.spinner);
            spinner.setAdapter(adapter);
            spinner.setPrompt("Валюта");
            spinner.setSelection(0);

            button = findViewById(R.id.button);
            valueView = findViewById(R.id.editTextNumberDecimal);
            resultView = findViewById(R.id.textView2);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    position = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            button.setOnClickListener(view -> {
                float result = Float.parseFloat(valueView.getText().toString()) / Float.parseFloat(valueList.get(position));
                resultView.setText(Float.toString(result));
            });

        }else{
            Toast.makeText(this, "Валюты не доступны", Toast.LENGTH_SHORT).show();
        }
    }
}