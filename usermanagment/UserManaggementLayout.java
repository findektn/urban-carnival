package com.hbms.usermanagment;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hbms.hbmssupport.R;


import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * Created by gertmorak on 04.12.16.
 */

public class UserManaggementLayout extends Application {
    String HOST;

    View view;
    Activity activity;
    ArrayList<NavItem_mgmt> mNavItemMgmts = new ArrayList<NavItem_mgmt>();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    public void setLayout(final Activity activity1, final View view1) {

        view = view1;
        activity = activity1;
        HOST =  activity.getString(R.string.host);

        TextView spokenText = (TextView) activity.findViewById(R.id.spokenText);
        spokenText.setText("");

        FragmentTransaction fragmentTransaction = activity1.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container1, new TextFragment0(), "HELLO");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        mNavItemMgmts.add(new NavItem_mgmt("über mich", "persönliche Daten", R.drawable.ic_menu_user));
        mNavItemMgmts.add(new NavItem_mgmt("wie wohne ich", "Lebensraum", R.drawable.ic_menu_home));
        mNavItemMgmts.add(new NavItem_mgmt("Einschränkungen", "körperliche Einschränkungen", R.drawable.ic_menu_wheelchair));
        mNavItemMgmts.add(new NavItem_mgmt("Kontaktperson", "Notfallkontakt", R.drawable.ic_menu_phonebook));
        mNavItemMgmts.add(new NavItem_mgmt("Sensoren", "Raumsensoren", R.drawable.ic_menu_share));

        // Populate the Navigtion Drawer with options

        mDrawerList = (ListView) view1.findViewById(R.id.navList);
        DrawerListAdapter_mgmt adapter = new DrawerListAdapter_mgmt(view1.getContext(), mNavItemMgmts);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = activity1.getFragmentManager().beginTransaction();

                    switch (position) {
                        case 0:

                            fragmentTransaction.replace(R.id.fragment_container1, new TextFragment0(), "Fragment1");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            fragmentTransaction.replace(R.id.fragment_container1, new TextFragment1(), "Fragment2");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            Bundle bundle = new Bundle();
                            bundle.putString("latitude", "3");
                            TextFragment2 f2 = new TextFragment2();
                            f2.setArguments(bundle);

                            fragmentTransaction.replace(R.id.fragment_container1, f2, "Fragment3");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 3:
                            fragmentTransaction.replace(R.id.fragment_container1, new TextFragment3(), "Fragment4");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 4:
                            fragmentTransaction.replace(R.id.fragment_container1, new TextFragment4(), "Fragment5");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        default:



                }
            }
        });
    }


}




