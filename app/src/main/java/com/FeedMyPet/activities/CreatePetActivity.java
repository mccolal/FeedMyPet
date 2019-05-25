package com.FeedMyPet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.FeedMyPet.R;
import com.FeedMyPet.helper.WebRequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePetActivity extends AppCompatActivity {

    WebRequestManager wm = new WebRequestManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        Spinner spinner = findViewById(R.id.spnPetType);
        // TODO get this list from DB
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Cat");
        spinnerArray.add("Dog");
        spinnerArray.add("Bird");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        final Button btnCreate = findViewById(R.id.btnCreatePet);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String result = wm.CreatePet(
                        ((EditText)(findViewById(R.id.txtPetName))).getText().toString(),
                        ((Spinner)(findViewById(R.id.spnPetType))).getSelectedItem().toString(),
                        "1"
                        );

                String PetID = GetIDFromResponse(result);

                Intent myIntent = new Intent(CreatePetActivity.this, FeedPage.class);
                myIntent.putExtra("PetID", PetID); //Optional parameters
                CreatePetActivity.this.startActivity(myIntent);
                finish();


            }
        });



    }

    private String GetIDFromResponse(String response){
        String result = "";
        String pattern = ".*:([0-9]+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response);
        if (m.find( )) {
            result = m.group(1);
        }else {
            System.out.println("NO MATCH");
        }
        //((EditText)(findViewById(R.id.txtPetName))).setText(result);
        return result;
    }
}
