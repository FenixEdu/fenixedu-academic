package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.AccountType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaAccountType2SqlAccountTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof AccountType) ? ((AccountType) source).name() : null;  
    }
    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? AccountType.valueOf((String) source) : null;
    }
}
