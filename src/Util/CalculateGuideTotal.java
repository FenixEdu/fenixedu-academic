package Util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Iterator;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;

public class CalculateGuideTotal {
	
	public CalculateGuideTotal() {};

	public static Double calculate(InfoGuide infoGuide){
		Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
		Double total = new Double(0);
		while (iterator.hasNext()){
			InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
			total = new Double(total.floatValue() + infoGuideEntry.getPrice().floatValue() * infoGuideEntry.getQuantity().floatValue());

		}
		
//		NumberFormat numberFormat = NumberFormat.getInstance();
//		StringBuffer stringBuffer = new StringBuffer();
//		FieldPosition fieldPosition = new FieldPosition(0);
//		
//		numberFormat.setGroupingUsed(false);
//		numberFormat.setMaximumFractionDigits(2);
//		
//		numberFormat.format(total, stringBuffer, fieldPosition);
//		int commaPosition = stringBuffer.indexOf(",");
//		
//		if (commaPosition != -1) {
//			stringBuffer = stringBuffer.replace(commaPosition, commaPosition + 1, ".");
//			total = new Double(stringBuffer.toString());
//		}
//		return total;
		
		return CalculateGuideTotal.formatNumber(total);

	}
  
  	public static Double formatNumber(Double numberToFormat){
		NumberFormat numberFormat = NumberFormat.getInstance();
		StringBuffer stringBuffer = new StringBuffer();
		FieldPosition fieldPosition = new FieldPosition(0);
		
		numberFormat.setGroupingUsed(false);
		numberFormat.setMaximumFractionDigits(2);
		
		numberFormat.format(numberToFormat, stringBuffer, fieldPosition);
		int commaPosition = stringBuffer.indexOf(",");
		
		if (commaPosition != -1) {
			stringBuffer = stringBuffer.replace(commaPosition, commaPosition + 1, ".");
			numberToFormat = new Double(stringBuffer.toString());
		}
		return numberToFormat;
  		
  	}
  
}