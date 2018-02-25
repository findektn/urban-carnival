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
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hbms.hbmssupport.R;
import com.hbms.hbmssupport.SoapObjectParam;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;


public class TextFragment0 extends Fragment {

    ArrayList<String> itemsName = new ArrayList<String>();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_fragment0, container, false);
        try {
            final SoapObject soapObject = SoapObjectParam.soapObjParam;

            SoapObject s1 = (SoapObject) soapObject.getProperty(7);
            setTextFragment(view, s1, R.id.firstNameText, "firstName");
            setTextFragment(view, s1, R.id.secondNameText, "secondName");
            setTextFragment(view, s1, R.id.textViewSize, "size");
            setTextFragment(view, s1, R.id.textViewWeight, "weight");
            setTextFragment(view, s1, R.id.gebDatumText, "gebDate");

            SetRadioButton(view, s1, R.id.radioButton1, R.id.radioButton2, "genderW", "genderM");


            SoapObject medicines1 = (SoapObject) s1.getProperty("medicines");
            for (int i = 0; i < medicines1.getPropertyCount(); i++) {
                SetMedicine(view, (SoapObject) medicines1.getProperty(i));
            }

            ((GridView) view.findViewById(R.id.simpleGridView)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.border_text_layout, itemsName));

        } catch (Exception ex) {
            ErrorMessage(ex, "SetUserDataFragment0");
        }

        return view;
    }


    public void setTextFragment(View view, SoapObject s1, int idname, String jsonName) {

        ((TextView) view.findViewById(idname)).setText(s1.getProperty(jsonName).toString());

    }


    public void SetMedicine(View view, SoapObject s1) {

        itemsName.add(s1.getProperty("medName") + "");
        itemsName.add(s1.getProperty("medUse") + "");

    }


    public void SetRadioButton(View view, SoapObject s1, int radiobuttonW, int radioButtonM, String w, String m) {

        ((RadioButton) view.findViewById(radiobuttonW)).setChecked(Boolean.parseBoolean(s1.getProperty(w) + ""));
        ((RadioButton) view.findViewById(radioButtonM)).setChecked(Boolean.parseBoolean(s1.getProperty(m) + ""));
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