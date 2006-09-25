package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
            XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(((String) source).getBytes()));
            Response respose = (Response) decoder.readObject();
            decoder.close();
            return respose;
        }
        return source;
    }

}
