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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hbms.hbmssupport.R;
import com.hbms.hbmssupport.SoapObjectParam;

import org.ksoap2.serialization.SoapObject;


public class TextFragment2 extends Fragment {
    TextView text, vers;
    Button button;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        View view = inflater.inflate(R.layout.text_fragment2, container, false);

        try {

            final SoapObject soapObject = SoapObjectParam.soapObjParam;

            SoapObject s1 = (SoapObject) soapObject.getProperty("person");
            SoapObject s2 = (SoapObject) s1.getProperty("phyLimitation");


            setTextFragment(view, s2, R.id.sparmL, "sparmL");
            setTextFragment(view, s2, R.id.sparmF, "sparmF");
            setTextFragment(view, s2, R.id.hparmS, "hparmS");
            setTextFragment(view, s2, R.id.bparmB, "bparmB");

            SetRadioButton(view, s2, R.id.sparmGueS, s2.getProperty("sparmGueS").toString());
            SetRadioButton(view, s2, R.id.sparmGauS, s2.getProperty("sparmGauS").toString());
            SetRadioButton(view, s2, R.id.hparmH, s2.getProperty("hparmH").toString());
            SetRadioButton(view, s2, R.id.hparmHP, s2.getProperty("hparmHP").toString());
            SetRadioButton(view, s2, R.id.hparmT, s2.getProperty("hparmT").toString());
            SetRadioButton(view, s2, R.id.bparmR, s2.getProperty("bparmR").toString());
            SetRadioButton(view, s2, R.id.bparamRL, s2.getProperty("bparmRL").toString());


            String morelimit = "";
            SoapObject MoreLimits = (SoapObject) s2.getProperty("moreLimit");
            for (int i = 0; i < MoreLimits.getPropertyCount(); i++) {

                SoapObject s = (SoapObject) MoreLimits.getProperty(i);
                morelimit = morelimit + s.getProperty("name") + " ";
            }

            ((TextView) view.findViewById(R.id.moreLimit)).setText(morelimit);

        } catch (Exception ex) {
            ErrorMessage(ex, "SetUserData");
        }
        return view;

    }

    public void SetRadioButton(View view, SoapObject s2, int radiobutton, String value) {

        try {

            ((RadioButton) view.findViewById(radiobutton)).setChecked(Boolean.parseBoolean(value));

        } catch (Exception ex) {

            ErrorMessage(ex, "SetRadioButtonLimitation");
        }
    }

    public void setTextFragment(View view, SoapObject s1, int idname, String jsonName) {

        ((TextView) view.findViewById(idname)).setText(s1.getProperty(jsonName).toString());
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