package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.transactions.TransactionType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class JavaTransactionType2SqlTransactionTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof TransactionType) {
            TransactionType transactionType = (TransactionType) source;
            return new Integer(transactionType.getValue());
        }

        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return TransactionType.getEnum(src.intValue());
        }

        return source;
    }

}