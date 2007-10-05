package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

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
	    
	    String xmlResponse = null;
	    try {
		xmlResponse = new String(xmlResponse2.getBytes("latin1"), "UTF-8");
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	    }
	    
	    Response response = getResponse(xmlResponse);

	    return response;
	}
	return source;
    }

    private Response getResponse(String xmlResponse) {
	XMLDecoder decoder = null;
	try {
	    decoder = new XMLDecoder(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
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