
package Util;

import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NumberUtils {
	
	/**
	 * 
	 * @param numberToFormat
	 * @param decimalPlacement
	 * @return the number with the desired decimal placements
	 */
	public static Double formatNumber(Double numberToFormat, int decimalPlacement){

		NumberFormat numberFormat = NumberFormat.getInstance();
		StringBuffer stringBuffer = new StringBuffer();
		FieldPosition fieldPosition = new FieldPosition(0);
		
		numberFormat.setGroupingUsed(false);
		numberFormat.setMaximumFractionDigits(decimalPlacement);
		
		numberFormat.format(numberToFormat, stringBuffer, fieldPosition);
		
		int commaPosition = stringBuffer.indexOf(",");
		
		if (commaPosition != -1) {
			stringBuffer = stringBuffer.replace(commaPosition, commaPosition + 1, ".");
		}

		numberToFormat = new Double(stringBuffer.toString());		
		return numberToFormat;
	}

}
