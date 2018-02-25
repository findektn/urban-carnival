package com.hbms.usermanagment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hbms.hbmssupport.R;


/**
 * Created by gertmorak on 04.12.16.
 */

public class UserManagementFragment extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_mgmt, container, false);
        try {
            new UserManaggementLayout().setLayout(getActivity(), view);
            return view;
        }
        catch( Exception ex){
            Log.println(Log.INFO, "Error", "Soap count " + Log.getStackTraceString(ex));

            return view;
        }

    }

}