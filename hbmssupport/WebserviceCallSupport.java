package com.hbms.hbmssupport;

/**
 * Created by gmorak on 03.07.16.
 */


import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WebserviceCallSupport {


    public WebserviceCallSupport() {
    }


    public SoapObject Call(String host) {
        final String SOAP_ACTION = host + "GetOpertionitem";
        final String OPERATION_NAME = "GetOpertionitem";
        final String WSDL_TARGET_NAMESPACE = host;
        final String SOAP_ADDRESS = host + "HBMSService.asmx";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        request.addProperty("operationId", "01");
        envelope.implicitTypes = false;

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        SoapObject response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);

            response = (SoapObject) envelope.getResponse();
            Log.println(Log.INFO, "Finde", " SoapObjekt ok :  " + response);

        } catch (Exception ex) {
            ex.printStackTrace();
            SoapObject error = new SoapObject(WSDL_TARGET_NAMESPACE, "anyType");
            error.addProperty("matchStatus", "ErrorSoapObj");
            response = error;
            Log.println(Log.INFO, "Finde", " 3 SoapObjekt Exception  :  " + ex);
        }

        return response;
    }
}
