package Util;

import java.io.Serializable;
import java.util.Iterator;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;

public class CalculateGuideTotal implements Serializable {
	
	public CalculateGuideTotal() {}

	public static Double calculate(InfoGuide infoGuide){
		Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
		Double total = new Double(0);
		while (iterator.hasNext()){
			InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
			total = new Double(total.floatValue() + infoGuideEntry.getPrice().floatValue() * infoGuideEntry.getQuantity().floatValue());

		}

		return NumberUtils.formatNumber(total, 2);
	}

  
}