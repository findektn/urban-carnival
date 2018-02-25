package com.hbms.usermanagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbms.hbmssupport.R;

import java.util.ArrayList;

/**
 * Created by gertmorak on 19.10.16.
 */

class DrawerListAdapter_mgmt extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem_mgmt> mNavItemMgmts;

    public DrawerListAdapter_mgmt(Context context, ArrayList<NavItem_mgmt> navItemMgmts) {
        mContext = context;
        mNavItemMgmts = navItemMgmts;
    }

    @Override
    public int getCount() {
        return mNavItemMgmts.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItemMgmts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item_mgmt, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItemMgmts.get(position).mTitle );
        subtitleView.setText( mNavItemMgmts.get(position).mSubtitle );
        iconView.setImageResource(mNavItemMgmts.get(position).mIcon);

        return view;
    }
}