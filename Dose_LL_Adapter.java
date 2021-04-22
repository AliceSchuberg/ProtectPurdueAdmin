package com.cnit425.protectpurdueadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Dose_LL_Adapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Dose> Dose_List;

    public Dose_LL_Adapter(Context context, ArrayList<Dose> dose_list){
        super(context,R.layout.listview_custom_dose, dose_list);
        this.context = context;
        this.Dose_List = dose_list;
    }

    @SuppressLint({"DefaultLocale", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //inflate the rowView of ListView
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.listview_custom_dose, parent, false);

        //Dose
        Dose dose_info = Dose_List.get(position);

        //find View
        TextView txtDose_Num = rowView.findViewById(R.id.txtVaccineCount);
        TextView txtLocation = rowView.findViewById(R.id.txtLocation_Result);
        TextView txtAddress = rowView.findViewById(R.id.txtAddress_Result);
        TextView txtDate = rowView.findViewById(R.id.txtDate_Result);
        TextView txtTime = rowView.findViewById(R.id.txtTime_Result);
        TextView txtVaccineType = rowView.findViewById(R.id.txtVaccineType);
        TextView txtVaccineSerial = rowView.findViewById(R.id.txtVaccineSerial);
        TextView txtExp = rowView.findViewById(R.id.txtExp);
        RadioButton radCompleted = rowView.findViewById(R.id.radCompleted);

        //setChangeListener so that input can be live-update to the object itself
        txtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                dose_info.setDate(s.toString());
            }
        });
        txtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                dose_info.setTime(s.toString());
            }
        });
        txtVaccineType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                dose_info.setVaccineType(s.toString());
            }
        });
        txtVaccineSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                dose_info.setVaccineSerial(s.toString());
            }
        });
        txtExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                dose_info.setVaccineExpDate(s.toString());
            }
        });
        radCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dose_info.setCompleted(isChecked);
        });

        //set basic data that always exists
        txtDose_Num.setText(dose_info.getDose_num());
        txtDate.setText(dose_info.getDate());
        txtTime.setText(dose_info.getTime());
        radCompleted.setChecked(dose_info.getCompleted());

        //check if location_name & location_address has been filled out
        String location_name = dose_info.getLocation_name();
        String location_address = dose_info.getAddress();
        if (location_name!= null && location_address!=null){
            txtLocation.setText(location_name);
            txtAddress.setText(location_address);
        }

        //check if vaccine info has been previously filled out by a doctor
        String vaccineType = dose_info.getVaccineType();
        String vaccineSerial = dose_info.getVaccineSerial();
        String exp = dose_info.getVaccineExpDate();
        if (vaccineType != null || vaccineSerial!= null ||exp!= null){
            txtVaccineType.setText(vaccineType);
            txtVaccineSerial.setText(vaccineSerial);
            txtExp.setText(exp);
        }
        return rowView;
    }
}
