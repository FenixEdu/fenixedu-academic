/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.tests.CorrectionAvailability;

/**
 * @author Susana Fernandes
 */
public class CorrectionAvailability2EnumCorrectionAvailabilityFieldConversion
	implements FieldConversion {

	/**
	 *
	 */

	public Object javaToSql(Object arg0) throws ConversionException {
		if (arg0 instanceof CorrectionAvailability) {
			CorrectionAvailability ca = (CorrectionAvailability) arg0;
			return ca.getAvailability();
		} 
			return arg0;
		
	}

	/**
	 *
	 */

	public Object sqlToJava(Object arg0) throws ConversionException {
		if (arg0 instanceof Integer) {
			Integer availability = (Integer) arg0;
			return new CorrectionAvailability(availability);
		} 
			return arg0;
		
	}

}