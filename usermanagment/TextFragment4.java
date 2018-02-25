package com.hbms.usermanagment;

/**
 * Created by gertmorak on 19.10.16.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hbms.hbmssupport.R;


public class TextFragment4 extends Fragment {
    TextView text,vers;
    Button button;

    @Override

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_fragment4, container, false);





        return view;

    }

}