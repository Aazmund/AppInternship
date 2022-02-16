package com.example.appinternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView dateView;
    private RequestQueue requestQueue;
    private ListView listView;
    private final List<Currency> currencies = new ArrayList<>();
    private ArrayList<String> currenciesBuffer = new ArrayList<>();
    private String dateBuffer = "";
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateView = findViewById(R.id.textView3);
        listView = findViewById(R.id.listView);

        requestQueue = Volley.newRequestQueue(this);

        myTask = new MyTask();
        myTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Обновить");
        menu.add("Конвертор валют");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getTitle().toString()){
            case "Обновить":
                jsonParse();
                break;
            case "Конвертор валют":
                Intent intent = new Intent(this, ConverterActivity.class);
                StringBuilder currency_data = new StringBuilder();
                for(Currency obj : currencies){
                    currency_data.append(obj.getName());
                    currency_data.append("@");
                    currency_data.append(obj.getValue());
                    currency_data.append("@");
                }
                intent.putExtra("KEY_CURRENCY", currency_data.toString());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        for(Currency currency : currencies){
            currenciesBuffer.add("SAVED_OBJ");
            currenciesBuffer.add(currency.getId());
            currenciesBuffer.add(currency.getNumCode());
            currenciesBuffer.add(currency.getCharCode());
            currenciesBuffer.add(currency.getNominal());
            currenciesBuffer.add(currency.getName());
            currenciesBuffer.add(currency.getValue());
            currenciesBuffer.add(currency.getPreValue());
        }

        dateBuffer = dateView.getText().toString();

        outState.putStringArrayList("KEY_CURRENCIES_BUFFER", currenciesBuffer);
        outState.putString("KEY_DATE_BUFFER", dateBuffer);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currencies.clear();
        super.onRestoreInstanceState(savedInstanceState);
        dateView.setText(savedInstanceState.getString("KEY_DATE_BUFFER"));
        currenciesBuffer = savedInstanceState.getStringArrayList("KEY_CURRENCIES_BUFFER");

        for (int i = 0; i < currenciesBuffer.size(); i++) {
            if (currenciesBuffer.get(i).equals("SAVED_OBJ")){
                Currency currency = new Currency(currenciesBuffer.get(i+1), currenciesBuffer.get(i+2),currenciesBuffer.get(i+3),currenciesBuffer.get(i+4),currenciesBuffer.get(i+5),currenciesBuffer.get(i+6),currenciesBuffer.get(i+7));
                currencies.add(currency);
            }
        }
        CurrencyAdapter adapter = new CurrencyAdapter(this, currencies);
        currenciesBuffer.clear();
        listView.setAdapter(adapter);
        myTask = new MyTask();
        myTask.execute();
    }

    private void jsonParse(){
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";

        @SuppressLint("SetTextI18n")
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject obj = response.getJSONObject("Valute");
                        JSONArray nameArray = obj.names();
                        if (nameArray != null) {
                            for(int i = 0; i < nameArray.length(); i++){
                                JSONObject valute = obj.getJSONObject(String.valueOf(nameArray.get(i)));

                                Currency currency = new Currency(valute.getString("ID"), valute.getString("NumCode"),
                                        valute.getString("CharCode"), valute.getString("Nominal"),
                                                valute.getString("Name"), valute.getString("Value"),
                                                        valute.getString("Previous"));

                                currencies.add(currency);
                            }
                            CurrencyAdapter adapter = new CurrencyAdapter(this, currencies);
                            listView.setAdapter(adapter);

                            DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
                            String currentDate = dateFormat.format(new Date());
                            currentDate += ", ";
                            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            currentDate += dateFormat.format(new Date());

                            dateView.setText("Последнее обновление - " + currentDate);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                jsonParse();
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void writeFile(){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("BUFFER.txt", MODE_PRIVATE)));
            bw.write(dateView.getText().toString());
            for(Currency obj: currencies){
                bw.write("SAVED_OBJ");
                bw.write(obj.getId());
                bw.write(obj.getNumCode());
                bw.write(obj.getCharCode());
                bw.write(obj.getNominal());
                bw.write(obj.getName());
                bw.write(obj.getValue());
                bw.write(obj.getPreValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("BUFFER.txt")));
            ArrayList<String> str = new ArrayList<>();
            while ((br.readLine()) != null) {
                str.add(br.readLine());
            }
            dateView.setText(str.get(0));
            for (int i = 1; i < str.size(); i++) {
                if (str.get(i).equals("SAVED_OBJ")){
                    Currency currency = new Currency(str.get(i+1), str.get(i+2),str.get(i+3),str.get(i+4),str.get(i+5),str.get(i+6),str.get(i+7));
                    currencies.add(currency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}