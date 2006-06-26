/*
 * Created on Jun 22, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accounting.EntryType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaEntryType2SqlEntryTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof EntryType) ? ((EntryType) source).name() : null;  
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? EntryType.valueOf((String) source) : null;
    }

}
