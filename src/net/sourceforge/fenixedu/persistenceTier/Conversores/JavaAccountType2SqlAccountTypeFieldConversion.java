package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EventType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaAccountType2SqlAccountTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof EventType) ? ((AccountType) source).name() : null;  
    }
    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? AccountType.valueOf((String) source) : null;
    }
}
