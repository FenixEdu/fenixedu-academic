package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.PeriodToApplyRestriction;

/**
 * @author David Santos Feb 6, 2004
 */

public class JavaPeriodToApplyRestriction2SqlPeriodToApplyRestrictionFieldConversion
        implements FieldConversion {
    public Object javaToSql(Object source) {
        if (source instanceof PeriodToApplyRestriction) {
            PeriodToApplyRestriction s = (PeriodToApplyRestriction) source;
            return new Integer(s.getValue());
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        PeriodToApplyRestriction periodToApplyRestriction = null;
        
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            
            periodToApplyRestriction = PeriodToApplyRestriction.getEnum(src.intValue());
            if (periodToApplyRestriction == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal PeriodToApplyRestriction!(" + source + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal PeriodToApplyRestriction!("
                    + source + ")");
        }
        return periodToApplyRestriction;

    }
}