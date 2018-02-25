package com.hbms.webservice;

/**
 * Created by gmorak on 03.07.16.
 */


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Assert;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WebserviceCallParams {


    public WebserviceCallParams() {
    }


    public SoapObject Call(String host) {
        final String SOAP_ACTION = host + "getParam";
        final String OPERATION_NAME = "getParam";
        final String WSDL_TARGET_NAMESPACE = host;
        final String SOAP_ADDRESS = host + "HBMSService.asmx";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = false;

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        SoapObject response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);

            response = (SoapObject) envelope.getResponse();

        } catch (Exception exception) {
            exception.printStackTrace();
            Log.println(Log.INFO, "SOAP", " WebserviceCallParams SoapObject:  " + exception);
        }




        return response;
    }
}
