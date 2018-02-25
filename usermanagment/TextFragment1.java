package com.hbms.usermanagment;

/**
 * Created by gertmorak on 19.10.16.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hbms.hbmssupport.R;
import com.hbms.hbmssupport.SoapObjectParam;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;


public class TextFragment1 extends Fragment {


    ArrayList<String> itemsName = new ArrayList<String>();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_fragment1, container, false);
        try {
            final SoapObject soapObject = SoapObjectParam.soapObjParam;

            //habitat=anyType{house=false; apartment=true; floorNr=E; lift=true; garden=false; balcony=true; animal=anyType{Animal=anyType{species=Cat; petname=Tom; }; Animal=anyType{species=Dog; petname=Wuffi; }; Animal=anyType{species=Bird; petname=Sweety der Vogel; }; }; };

            SoapObject s1 = (SoapObject) soapObject.getProperty("person");
            SoapObject s2 = (SoapObject) s1.getProperty("habitat");

            setTextFragment(view, s2, R.id.floorNrView, "floorNr");
            SetRadioButton(view, s2, R.id.houseRadioButton, s2.getProperty("house").toString());
            SetRadioButton(view, s2, R.id.apartmentradioButton, s2.getProperty("apartment").toString());
            SetCheckButton(view, s2, R.id.liftCheckBox, s2.getProperty("lift").toString());
            SetCheckButton(view, s2, R.id.gardenCheckBox, s2.getProperty("garden").toString());
            SetCheckButton(view, s2, R.id.balconyCheckBox, s2.getProperty("balcony").toString());


            SoapObject animals = (SoapObject) s2.getProperty("animal");
            for (int i = 0; i < animals.getPropertyCount(); i++) {
                SetAnimals(view, (SoapObject) animals.getProperty(i));
            }


            ((GridView) view.findViewById(R.id.simpleGridView)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.border_text_layout, itemsName));

        } catch (Exception ex) {
            ErrorMessage(ex, "SetUserDataFragment1");
        }

        return view;

    }

    public void SetAnimals(View view, SoapObject s1) {

        itemsName.add(s1.getProperty("species") + "");
        itemsName.add(s1.getProperty("petname") + "");

    }

    public void setTextFragment(View view, SoapObject s1, int idname, String jsonName) {
        ((TextView) view.findViewById(idname)).setText(s1.getProperty(jsonName).toString());

    }


    public void SetCheckButton(View view, SoapObject s2, int radiobutton, String value) {

        ((CheckBox) view.findViewById(radiobutton)).setChecked(Boolean.parseBoolean(value));
    }


    public void SetRadioButton(View view, SoapObject s2, int radiobutton, String value) {


        ((RadioButton) view.findViewById(radiobutton)).setChecked(Boolean.parseBoolean(value));

    }

    public void ErrorMessage(Exception ex, String methodeName) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error: " + methodeName);
            builder.setMessage(Log.getStackTraceString(ex) + "---\n");
            builder.setPositiveButton("OK", null);
            builder.show();

        } catch (Exception e) {
        }
    }

}