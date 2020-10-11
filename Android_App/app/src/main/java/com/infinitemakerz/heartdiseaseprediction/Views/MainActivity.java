package com.infinitemakerz.heartdiseaseprediction.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.infinitemakerz.heartdiseaseprediction.Network.ApiInterface;
import com.infinitemakerz.heartdiseaseprediction.Network.ApiClient;
import com.infinitemakerz.heartdiseaseprediction.Model.Pojo;
import com.infinitemakerz.heartdiseaseprediction.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //todo: Declare AutoCompleteTextView , Buttons and Spinners(Dropdowns)
    AutoCompleteTextView bloodsugar, bloodpressure, cholestrol, maxheartrate, stdepression, maxvessels, age;
    Spinner spinner, spinner2, spinner3, spinner4, spinner5;
    Button button_diagnose;

    //todo : String arrays holds spinner values
    String[] chestPain = {"Chest Pain", "typical angina", "atypical angina", "non-anginal pain", "asymptomatic"};
    String[] electroCardiographic = {"Electro Cardiographic", "normal", "ST-T wave abnormality", "left ventricular hypertrophy"};
    String[] exerciseAngina = {"Exercise Angina", "yes", "no"};
    String[] stSegment = {"ST Segment", "upsloping", "flat", "downsloping"};
    String[] thalassemia = {"Thalassemia", "normal", "fixed defect", "reversable defect"};
    String chestPainValue = "None";
    String electroCardiographicValue = "None";
    String exerciseAnginaValue = "None";
    String stSegmentValue = "None";
    String thalassemiaValue = "None";

    ProgressDialog progressDialog;

    //todo: gender radio buttons
    RadioGroup radioGroup;
    RadioButton radioButton;
    ApiInterface apiInterface;

    //todo: ArrayList holds data to be sent to the API
    List<String> vizdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        findIds();
        setAdapters();

        //todo : on button diagnose click
        button_diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Processing!...");
                progressDialog.show();
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(id);

                /**
                 * Todo : check if all edittext and spinners are not empty
                 */
                if ((bloodpressure.getText().toString().length() != 0) & (cholestrol.getText().toString().length() != 0) & (bloodsugar.getText().toString().length() != 0) & (maxheartrate.getText().toString().length() != 0) & (stdepression.getText().toString().length() != 0) & (maxvessels.getText().toString().length() != 0) & (!chestPainValue.equalsIgnoreCase("Chest Pain") & !chestPainValue.equalsIgnoreCase("None")) & (!electroCardiographicValue.equalsIgnoreCase("Electro Cardiographic") & !electroCardiographicValue.equalsIgnoreCase("None")) & (!exerciseAnginaValue.equalsIgnoreCase("Exercise Angina") & !exerciseAnginaValue.equalsIgnoreCase("None")) & (!stSegmentValue.equalsIgnoreCase("ST Segment") & !stSegmentValue.equalsIgnoreCase("None")) & (!thalassemiaValue.equalsIgnoreCase("Thalassemia") & !thalassemiaValue.equalsIgnoreCase("None"))) {

                    //todo: API call function 'hitAPICall()' is called here
                    hitAPICall(age.getText().toString(), radioButton.getText().toString(),
                            chestPainValue, bloodpressure.getText().toString(),
                            cholestrol.getText().toString(),
                            bloodsugar.getText().toString(),
                            electroCardiographicValue, maxheartrate.getText().toString(),
                            exerciseAnginaValue, stdepression.getText().toString(),
                            stSegmentValue, maxvessels.getText().toString(), thalassemiaValue);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     *todo: function 'findIDs' finds ID in R.  and binds.
     */
    private void findIds(){
        age = findViewById(R.id.age);
        bloodpressure = findViewById(R.id.bloodpressure);
        cholestrol = findViewById(R.id.cholestrol);
        bloodsugar = findViewById(R.id.bloodsugar);
        radioGroup = findViewById(R.id.radioGroup);
        maxheartrate = findViewById(R.id.maxheartrate);
        stdepression = findViewById(R.id.stdepression);
        maxvessels = findViewById(R.id.maxvessels);
        age = findViewById(R.id.age);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        spinner5 = findViewById(R.id.spinner5);
        button_diagnose = findViewById(R.id.Diagnose_btn);

    }

    /**
     *todo:  'setAdapters' function sets adapters for the ArrayList and  Spinners.
     */
    private void setAdapters(){
        vizdata = new ArrayList<>();
        ArrayAdapter<String> arrayAdaptercp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chestPain);
        arrayAdaptercp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdaptercp);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterec = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, electroCardiographic);
        arrayAdapterec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapterec);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exerciseAngina);
        arrayAdapterea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(arrayAdapterea);
        spinner3.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterst = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stSegment);
        arrayAdapterst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(arrayAdapterst);
        spinner4.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, thalassemia);
        arrayAdapterth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(arrayAdapterth);
        spinner5.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner:
                chestPainValue = chestPain[i];
                break;
            case R.id.spinner2:
                electroCardiographicValue = electroCardiographic[i];
                break;
            case R.id.spinner3:
                exerciseAnginaValue = exerciseAngina[i];
                break;
            case R.id.spinner4:
                stSegmentValue = stSegment[i];
                break;
            case R.id.spinner5:
                thalassemiaValue = thalassemia[i];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Description : function  'hitAPICal' receives below mentioned params
     *              puts them into an ArrayList and
     *              makes the network API call using retrofit.
     *
     * @param age
     * @param gender
     * @param chestPainType
     * @param bloodPressure
     * @param cholestrol
     * @param bloodSugar
     * @param ecg
     * @param maxHeartRate
     * @param inducedAngina
     * @param stDepression
     * @param stSlope
     * @param majorVessels
     * @param thalassemia
     */
    public void hitAPICall(String age, String gender, String chestPainType, String bloodPressure, String cholestrol, String bloodSugar, String ecg, String maxHeartRate, String inducedAngina, String stDepression, String stSlope, String majorVessels, String thalassemia) {
        String genV = "None";
        if (gender.equalsIgnoreCase("Male"))
            genV = "1";
        else if (gender.equalsIgnoreCase("Female"))
            genV = "0";
        String chestPainV = "None";
        if (chestPainType.equalsIgnoreCase("typical angina"))
            chestPainV = "1";
        else if (chestPainType.equalsIgnoreCase("atypical angina"))
            chestPainV = "2";
        else if (chestPainType.equalsIgnoreCase("non-anginal pain"))
            chestPainV = "3";
        else if (chestPainType.equalsIgnoreCase("asymptomatic"))
            chestPainV = "4";
        String restECGV = "None";
        if (ecg.equalsIgnoreCase("normal"))
            restECGV = "0";
        else if (ecg.equalsIgnoreCase("ST-T wave abnormality"))
            restECGV = "2";
        else if (ecg.equalsIgnoreCase("left ventricular hypertrophy"))
            restECGV = "3";
        String eindV = "None";
        if (inducedAngina.equalsIgnoreCase("YES"))
            eindV = "1";
        else if (inducedAngina.equalsIgnoreCase("NO"))
            eindV = "0";
        String stslopeV = "None";
        if (stSlope.equalsIgnoreCase("upsloping"))
            stslopeV = "1";
        else if (stSlope.equalsIgnoreCase("flat"))
            stslopeV = "2";
        else if (stSlope.equalsIgnoreCase("downsloping"))
            stslopeV = "3";
        String thV = "None";
        if (thalassemia.equalsIgnoreCase("normal"))
            thV = "1";
        else if (thalassemia.equalsIgnoreCase("fixed defect"))
            thV = "2";
        else if (thalassemia.equalsIgnoreCase("reversable defect"))
            thV = "3";
        vizdata.add(age);
        vizdata.add(genV);
        vizdata.add(chestPainV);
        vizdata.add(bloodPressure);
        vizdata.add(cholestrol);
        vizdata.add(bloodSugar);
        vizdata.add(restECGV);
        vizdata.add(maxHeartRate);
        vizdata.add(eindV);
        vizdata.add(stDepression);
        vizdata.add(stslopeV);
        vizdata.add(majorVessels);
        vizdata.add(thV);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pojo> call = apiInterface.getAPIResponse(vizdata);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(@NonNull Call<Pojo> call, @NonNull Response<Pojo> response) {
                Pojo pojo = response.body();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Heart Prediction");
                alertDialog.setMessage("Predicted Result is " + pojo.getIsHeart());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Pojo> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Oops try again!...", Toast.LENGTH_SHORT).show();
            }
        });
    }



}

