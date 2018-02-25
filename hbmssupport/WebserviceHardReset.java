package com.hbms.hbmssupport;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by gmorak on 05.09.16.
 */
public class WebserviceHardReset {


    public WebserviceHardReset() {
    }

    public void Call(String host) {
        final String SOAP_ACTION = host + "hardresetWebservice";

        final String OPERATION_NAME = "hardresetWebservice";
        final String WSDL_TARGET_NAMESPACE = host;

        final String SOAP_ADDRESS = host + "HBMSService.asmx";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        Log.println(Log.INFO, "Output", " Hard Reset Host:  " + host);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = false;

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        SoapPrimitive response;
        String result;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            // response = (SoapPrimitive) envelope.getResponse();


        } catch (Exception exception) {
            Log.println(Log.INFO, "Output", "WebServiceHardReset Fehler !!!!!!!!!!!!!!!!!!!!!!!!!!" + exception);


        }

    }
}
