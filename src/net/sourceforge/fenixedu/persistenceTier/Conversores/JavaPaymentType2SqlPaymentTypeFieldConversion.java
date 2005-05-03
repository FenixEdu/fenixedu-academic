package net.sourceforge.fenixedu.persistenceTier.Conversores;


import net.sourceforge.fenixedu.domain.transactions.PaymentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaPaymentType2SqlPaymentTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof PaymentType) {
            PaymentType s = (PaymentType) source;
            return s.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return PaymentType.valueOf(src);
        }
        return source;

    }

}