/*
 * Created on 26/Apr/2004
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class CorrectionFormula2EnumCorrectionFormulaFieldConversion implements FieldConversion
{

    public Object javaToSql(Object arg0) throws ConversionException
    {
        if (arg0 instanceof CorrectionFormula)
        {
            CorrectionFormula cf = (CorrectionFormula) arg0;
            return cf.getFormula();
        }
        else
        {
            return arg0;
        }
    }

    public Object sqlToJava(Object arg0) throws ConversionException
    {
        if (arg0 instanceof Integer)
        {
            Integer formula = (Integer) arg0;
            return new CorrectionFormula(formula);
        }
        else
        {
            return arg0;
        }
    }

}