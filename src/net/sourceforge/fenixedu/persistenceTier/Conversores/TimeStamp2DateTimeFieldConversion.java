package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * @author mrsp
 * @author shezad
 */
import java.sql.Timestamp;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.DateTime;

public class TimeStamp2DateTimeFieldConversion implements FieldConversion {


    public Object javaToSql(Object source) {
        if (source instanceof DateTime) {
            return new Timestamp(((DateTime) source).getMillis());
        }
        return source;
    }


    public Object sqlToJava(Object source) {
        if(source == null || source.equals("")){
            return null;
        } 
        if (source instanceof Timestamp) {
            return new DateTime(((Timestamp) source).getTime());
        }
        return source;
    }

}