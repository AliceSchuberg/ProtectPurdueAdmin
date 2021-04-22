package com.cnit425.protectpurdueadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    private String uid;
    private Boolean vaccinated;
    private Integer vaccineCount;
    private ArrayList<Dose> doseList;

    private ChildEventListener vaccinationListener;
    private ValueEventListener locationValueListener;
    private Dose_LL_Adapter listViewAdapter;

    private DatabaseReference vaccinationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        uid = getIntent().getStringExtra("uid");
        String email = getIntent().getStringExtra("email");
        ((TextView)findViewById(R.id.txtEmail)).setText(email);

        doseList = new ArrayList<>();
        listViewAdapter = new Dose_LL_Adapter(this,doseList);

        vaccinationRef = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Vaccination");

        //define childValueListener for user/<uid>/Vaccination which contains "Vaccinated" \ "VaccineCount" \ "Dose<i>"
        vaccinationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                onChildChanged(snapshot, previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (Objects.equals(snapshot.getKey(), "Vaccinated")){
                    //get vaccinated data and set it to text
                    vaccinated = snapshot.getValue(Boolean.class);
                    ((RadioButton)findViewById(R.id.radVaccinated)).setChecked(vaccinated);
                }else if(Objects.equals(snapshot.getKey(), "VaccineCount")){
                    //get vaccineCount data and set it to text
                    vaccineCount = snapshot.getValue(Integer.class);
                    ((TextView)findViewById(R.id.txtVaccineCount)).setText(vaccineCount);
                }else{
                    //get Dose node data and convert it to a Dose object, add to arrayList to update the adapter
                    Dose newDose = snapshot.getValue(Dose.class);
                    newDose.setDose_num(snapshot.getKey());
                    doseList.add(newDose);
                    //notify the adapter the dataList has changed
                    listViewAdapter.notifyDataSetChanged();
                    //run on another thread to retrieve specific info for location
                    new Thread(() -> {
                        String location_Serial = newDose.getLocation();
                        //string check
                        if(location_Serial == null || !location_Serial.startsWith("location")){
                            Toast.makeText(getApplicationContext(),
                                    "location_serial_error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FirebaseDatabase.getInstance().getReference("location").child(location_Serial)
                                .addValueEventListener(locationValueListener);
                    }).start();
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        //defined ValueListener for location/<location_Serial> which contains "name" \ "address"
        locationValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrieve the location_name & address from location_serial node
                String location_name = snapshot.child("name").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                //find the dose info object, and fill in the specific location name and address
                for (Dose dose: doseList){
                    if(dose.getLocation().equals(snapshot.getKey())){
                        dose.setLocation_name(location_name);
                        dose.setAddress(address);
                    }
                }
                //notify the adapter the dataList has changed
                listViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference("user").child(uid).child("Vaccination")
                .addChildEventListener(vaccinationListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(vaccinationRef!=null && vaccinationListener != null){
            vaccinationRef.removeEventListener(vaccinationListener);
        }
        if (locationValueListener!= null){
            for (Dose dose: doseList){
                String location = dose.getLocation();
                FirebaseDatabase.getInstance().getReference("location").child(location)
                        .removeEventListener(locationValueListener);
            }
        }
        super.onPause();
    }

    public void btnConfirmOnClick(View view){
        RadioButton radVaccinated = findViewById(R.id.radVaccinated);
        TextView txtVaccineCount = findViewById(R.id.txtVaccineCount);
        vaccinationRef.child("Vaccinated").setValue(radVaccinated.isChecked());
        vaccinationRef.child("VaccineCount").setValue(Integer.parseInt((String) txtVaccineCount.getText()));

        for (Dose dose :doseList){
            String dose_serial = dose.getDose_num();
            vaccinationRef.child(dose_serial).child("date").setValue(dose.getDate());
            vaccinationRef.child(dose_serial).child("time").setValue(dose.getTime());
            vaccinationRef.child(dose_serial).child("completed").setValue(dose.getCompleted());
            vaccinationRef.child(dose_serial).child("vaccineType").setValue(dose.getVaccineType());
            vaccinationRef.child(dose_serial).child("vaccineSerial").setValue(dose.getVaccineSerial());
            vaccinationRef.child(dose_serial).child("vaccineExpDate").setValue(dose.getVaccineExpDate());
        }
    }
}