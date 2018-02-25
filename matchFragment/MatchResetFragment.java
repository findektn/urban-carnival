package com.hbms.matchFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbms.hbmssupport.R;

/**
 * Created by gertmorak on 01.12.16.
 */



public class MatchResetFragment extends Fragment {


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.match_reset_fragment, container, false);
        return view;

    }

}
