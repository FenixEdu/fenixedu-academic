package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.BranchType;

/**
 * @author David Santos
 * Jan 13, 2004
 */

public class JavaBranchType2SqlBranchTypeFieldConversion implements FieldConversion
{
	public Object javaToSql(Object source) throws ConversionException {
		if (source instanceof BranchType)
		{
			BranchType branchType = (BranchType) source;
			switch (branchType.getType())
			{
				case BranchType.COMMON_BRANCH_INT:
					return BranchType.COMMON_BRANCH_STR; 
				case BranchType.SECUNDARY_BRANCH_INT:
					return BranchType.SECUNDARY_BRANCH_STR;
				case BranchType.SPECIALIZATION_BRANCH_INT:
					return BranchType.SPECIALIZATION_BRANCH_STR;
				default:
					throw new IllegalArgumentException(this.getClass().getName()+": Unkown BranchType!");
			}
		}
		return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
		if (source instanceof String){
			String sourceStr = (String) source;

			if (BranchType.COMMON_BRANCH_STR.equals(sourceStr))
			{
				return BranchType.COMMON_BRANCH;				
			} else if (BranchType.SECUNDARY_BRANCH_STR.equals(sourceStr))
			{
				return BranchType.SECUNDARY_BRANCH;
			} else if (BranchType.SPECIALIZATION_BRANCH_STR.equals(sourceStr))
			{
				return BranchType.SPECIALIZATION_BRANCH;
			} else
			{
				throw new IllegalArgumentException(this.getClass().getName()+": Unkown type!("+source+")");
			}
		}
		return source;
	}
}