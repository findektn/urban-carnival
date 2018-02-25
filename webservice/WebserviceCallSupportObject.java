package com.hbms.webservice;

/**
 * Created by gmorak on 03.07.16.
 */


import android.util.Log;

import com.hbms.hbmssupport.SoapObjectOperationParam;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class WebserviceCallSupportObject {


    public WebserviceCallSupportObject() {
    }


    public SoapObject Call(String host) {

        final String SOAP_ACTION = host + "GetOpertionitemAnroid";
        final String OPERATION_NAME = "GetOpertionitemAnroid";
        final String WSDL_TARGET_NAMESPACE = host;
        final String SOAP_ADDRESS = host + "HBMSService.asmx";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = false;

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        httpTransport.debug = true;

        SoapObject response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = (SoapObject) envelope.getResponse();
            SoapObjectOperationParam.soapObj=prettyFormat(httpTransport.responseDump,"3");

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.println(Log.INFO, "SOAP", " 3 SoapObjekt Exception  :  " + ex);
        }




        return response;
    }



    public static String prettyFormat(String input, String indent) {
        Source xmlInput = new StreamSource(new StringReader(input));
        StringWriter stringWriter = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent);
            transformer.transform(xmlInput, new StreamResult(stringWriter));
            String pretty = stringWriter.toString();
            pretty = pretty.replace("\r\n", "\n");
            return pretty;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
