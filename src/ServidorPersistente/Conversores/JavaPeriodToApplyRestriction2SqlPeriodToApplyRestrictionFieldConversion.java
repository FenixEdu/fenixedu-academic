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
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new Integer(src.intValue());
        }
        return source;

    }
}