 package com.krs.linkapp;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krs.api.EndPointDataService;
import com.krs.api.RetrofitClientInstance;
import com.krs.model.PageLimit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class MainActivity extends AppCompatActivity {

    TextView tvState, tvState1;
    Button btnPing;
    String[] urls = {"http://www.quantcast.com/", "http://www.statbrain.com/", "http://www.cubestat.com/", "http://whois.tools4noobs.com/info/", "http://www.quantcast.com/", "http://www.statbrain.com/", "http://www.cubestat.com/", "http://whois.tools4noobs.com/info/", "http://www.quantcast.com/", "http://www.statbrain.com/", "http://www.cubestat.com/", "http://whois.tools4noobs.com/info/"};
    String domain = "ndomain";
    String keyword = "nkey";
    ArrayList<String> successUrls;
    private EditText etUrl, etKeywords;
    private Spinner spPageLimit;
    private int limitCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvState = findViewById(R.id.tvState);
        tvState1 = findViewById(R.id.tvState1);
        btnPing = findViewById(R.id.btnPing);
        etUrl = findViewById(R.id.etUrl);
        etKeywords = findViewById(R.id.etUrl);
        spPageLimit = findViewById(R.id.spPageLimit);
        successUrls = new ArrayList<>();

        List<PageLimit> pageLimitList = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            PageLimit data = new PageLimit(i + " Submission", i);
            pageLimitList.add(data);
        }
        ArrayAdapter<PageLimit> adapter = new ArrayAdapter<PageLimit>(this, android.R.layout.simple_spinner_item, pageLimitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPageLimit.setAdapter(adapter);
        spPageLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PageLimit data = (PageLimit) parent.getSelectedItem();
                limitCount = data.getCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUrl.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter url", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etKeywords.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter keyword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (limitCount == -1) {
                    Toast.makeText(MainActivity.this, "Please select a pageLimit", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvState.setVisibility(View.VISIBLE);
                tvState1.setVisibility(View.INVISIBLE);
                successUrls = new ArrayList<>();
                for (int pageLimt = 0; pageLimt < limitCount; pageLimt++) {
                    getLink(urls[pageLimt], etUrl.getText().toString().trim(), etKeywords.getText().toString());
                }

            }
        });
    }

//    public void getSelectedUser(View v) {
//        User user = (User) spinner.getSelectedItem();
//        displayUserData(user);
//    }
//    private void displayUserData(User user) {
//        String name = user.getName();
//        int age = user.getAge();
//        String mail = user.getMail();
//        String userData = "Name: " + name + "\nAge: " + age + "\nMail: " + mail;
//        Toast.makeText(this, userData, Toast.LENGTH_LONG).show();
//    }


    public void getLink(final String url, final String domain, String keyword) {
        try {
            EndPointDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(EndPointDataService.class);
            String durl = url + domain;
            Call<ResponseBody> call = service.getLink(keyword, durl);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            String res = response.body().string();
                            String[] words = res.split(" ");

                            for (String word : words) {
                                if (word.equalsIgnoreCase("minutes")) {
                                    if (successUrls.size() == 0) {
                                        tvState1.setVisibility(View.INVISIBLE);
                                        tvState.setText("Try again later");
                                    }
                                    break;
                                } else if (word.equalsIgnoreCase("complete!</h2>\n<p")) {
                                    successUrls.add(url);
                                    tvState1.setVisibility(View.VISIBLE);
                                    String lable = "";
                                    for (String surl : successUrls) {
                                        lable = lable + "\n" + surl;
                                    }
                                    tvState.setText(lable);
                                    break;
                                } else {
                                    if (successUrls.size() == 0) {
                                        tvState1.setVisibility(View.INVISIBLE);
                                        tvState.setText("Pinging Failed");
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            if (successUrls.size() == 0) {
                                tvState1.setVisibility(View.INVISIBLE);
                                tvState.setText("Pinging Failed");
                            }
                        }
                    } else {
                        if (successUrls.size() == 0) {
                            tvState1.setVisibility(View.INVISIBLE);
                            tvState.setText("Pinging Failed");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (successUrls.size() == 0) {
                        tvState1.setVisibility(View.INVISIBLE);
                        tvState.setText("Pinging Failed");
                    }
                }
            });

        } catch (Exception e) {
            if (successUrls.size() == 0) {
                tvState1.setVisibility(View.INVISIBLE);
                tvState.setText("Pinging Failed");
            }
            e.printStackTrace();
        }

    }
}
