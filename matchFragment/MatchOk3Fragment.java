package com.hbms.matchFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbms.hbmssupport.R;

/**
 * Created by gertmorak on 01.12.16.
 */



public class MatchOk3Fragment extends Fragment {


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.match_ok_3_fragement, container, false);
        try {
            new MatchOKLayout().setLayout(getActivity(), view);
            return view;
        }
        catch (Exception ex){
            Log.println(Log.INFO, "SOAP", "MatchOk3Fragment " + Log.getStackTraceString(ex));
            return view;
        }

    }

}
