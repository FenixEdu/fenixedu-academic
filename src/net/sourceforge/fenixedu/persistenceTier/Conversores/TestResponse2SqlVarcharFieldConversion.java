package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import net.sourceforge.fenixedu.util.tests.Response;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TestResponse2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof Response) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    XMLEncoder encoder = new XMLEncoder(out);
	    encoder.writeObject((Response) source);
	    encoder.close();
	    try {
		return out.toString("ISO-8859-1");
	    } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return out.toString();
	    }
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    String xmlResponse2 = (String) source;

	    InputStreamReader inputStreamReader = null;

	    inputStreamReader = new InputStreamReader(new ByteArrayInputStream(xmlResponse2.getBytes()),
		    Charset.forName("utf-8"));

	    StringBuffer stringBuffer = new StringBuffer();
	    Reader in = new BufferedReader(inputStreamReader);

	    int ch;
	    try {
		while ((ch = in.read()) > -1) {
		    stringBuffer.append((char) ch);
		}
	    } catch (IOException e2) {
	    }

	    try {
		in.close();
	    } catch (IOException e1) {

	    }
	    String xmlResponse = stringBuffer.toString();

	    Response response = getResponse(xmlResponse);

	    return response;
	}
	return source;
    }

    private Response getResponse(String xmlResponse) {
	XMLDecoder decoder = null;
	try {
	    decoder = new XMLDecoder(new ByteArrayInputStream(xmlResponse.getBytes("utf-8")));
	} catch (UnsupportedEncodingException e1) {
	}
	Response response = null;
	try {
	    response = (Response) decoder.readObject();
	} catch (ArrayIndexOutOfBoundsException e) {
	    e.printStackTrace();
	}
	decoder.close();
	return response;
    }
}