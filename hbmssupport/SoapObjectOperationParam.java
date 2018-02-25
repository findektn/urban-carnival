package com.hbms.hbmssupport;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;


/**
 * Created by gertmorak on 01.12.16.
 */

public class SoapObjectOperationParam
{

    public static SoapObject soapObjectOperationParam=null;
    public static String soapObj=null;

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.println(Log.INFO,"SOAP", message.substring(start, end));
        }
    }
}
