package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.enrollment.EnrollmentCondition;

/**
 * @author David Santos in Jun 15, 2004
 */

public class JavaEnrollmentCondition2SqlEnrollmentConditionFieldConversion implements FieldConversion
{
	public Object javaToSql(Object source) throws ConversionException
	{
		if (source instanceof EnrollmentCondition)
		{
			EnrollmentCondition enrollmentCondition = (EnrollmentCondition) source;
			switch (enrollmentCondition.getType())
			{
				case EnrollmentCondition.FINAL_INT:
					return EnrollmentCondition.FINAL_STR;
				case EnrollmentCondition.TEMPORARY_INT:
					return EnrollmentCondition.TEMPORARY_STR;
				default :
					throw new IllegalArgumentException(this.getClass().getName() + ": Unkown EnrollmentCondition!");
			}
		}
		return source;
	}

	public Object sqlToJava(Object source) throws ConversionException
	{
		if (source instanceof String)
		{
			String sourceStr = (String) source;

			if (EnrollmentCondition.FINAL_STR.equals(sourceStr))
			{
				return EnrollmentCondition.FINAL;
			} else if (EnrollmentCondition.TEMPORARY_STR.equals(sourceStr))
			{
				return EnrollmentCondition.TEMPORARY;
			} else
			{
				throw new IllegalArgumentException(this.getClass().getName() + ": Unkown type!(" + source + ")");
			}
		}
		return source;
	}
}