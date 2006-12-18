package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import net.sourceforge.fenixedu.util.StringNormalizer;
import net.sourceforge.fenixedu.util.tests.Response;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TestResponse2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof Response) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLEncoder encoder = new XMLEncoder(out);
            encoder.writeObject((Response) source);
            encoder.close();
            return out.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String xmlResponse = source.toString();
            try {
                xmlResponse = new String(source.toString().getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
            }
            XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlResponse.getBytes()));
            Response respose = null;
            try {
                respose = (Response) decoder.readObject();
            } catch (ArrayIndexOutOfBoundsException e) {
                try {
                    decoder = new XMLDecoder(new ByteArrayInputStream(StringNormalizer.normalize(xmlResponse).getBytes()));
                    respose = (Response) decoder.readObject();
                } catch (ArrayIndexOutOfBoundsException e2) {
                    decoder = new XMLDecoder(new ByteArrayInputStream(StringNormalizer.normalizeAndRemoveMinorChars(xmlResponse).getBytes()));
                    respose = (Response) decoder.readObject();
                }
            }
            decoder.close();
            return respose;
        }
        return source;
    }
}
