package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.PaymentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaPaymentType2SqlPaymentTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof PaymentType) {
            PaymentType s = (PaymentType) source;
            return s.getType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new PaymentType(src);
        }
        return source;

    }

}