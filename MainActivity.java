package com.cnit425.protectpurdueadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnScanOnClick(View view){
        new IntentIntegrator(this).initiateScan();
    }

    // Get scan result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check null before continuing
        if(result == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if(result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            return;
        }

        //if !null, convert to JSON object
        try {
            JSONObject mJSON = new JSONObject(result.getContents());
            //check if the JSON object is generated from this app
            if(!mJSON.has("ProtectPurdueType")){
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
                return;
            }
            //use the email info to check if the user has been vaccinated
            String email = mJSON.getString("email");
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("user");
            mRef.orderByChild("Email").equalTo(email).get().addOnCompleteListener(task -> {
                String uid;
                //check if task successful -> true: get if the person_scanned has been vaccinated
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    return;
                }
                for(DataSnapshot shot: Objects.requireNonNull(task.getResult()).getChildren()){
                    uid = shot.getKey();
                    Intent mIntent = new Intent(this,UserProfile.class);
                    mIntent.putExtra("email",email);
                    mIntent.putExtra("uid",uid);
                    startActivity(mIntent);
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}