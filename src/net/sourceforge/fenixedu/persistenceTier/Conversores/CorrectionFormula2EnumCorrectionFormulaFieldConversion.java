/*
 * Created on 26/Apr/2004
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Susana Fernandes
 */
public class CorrectionFormula2EnumCorrectionFormulaFieldConversion implements FieldConversion {

    public Object javaToSql(Object arg0) throws ConversionException {
        if (arg0 instanceof CorrectionFormula) {
            CorrectionFormula cf = (CorrectionFormula) arg0;
            return cf.getFormula();
        }

        return arg0;

    }

    public Object sqlToJava(Object arg0) throws ConversionException {
        if (arg0 instanceof Integer) {
            Integer formula = (Integer) arg0;
            return new CorrectionFormula(formula);
        }

        return arg0;

    }

}