package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.transactions.TransactionType;

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
            return transactionType.name();
        }

        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {
            String src = (String) source;
            return TransactionType.valueOf(src);
        }

        return source;
    }

}