package net.sourceforge.fenixedu.util;

import java.util.Iterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;

public class CalculateGuideTotal extends FenixUtil {

    public CalculateGuideTotal() {
    }

    public static Double calculate(InfoGuide infoGuide) {
        Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
        Double total = new Double(0);
        while (iterator.hasNext()) {
            InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
            total = new Double(total.floatValue() + infoGuideEntry.getPrice().floatValue()
                    * infoGuideEntry.getQuantity().floatValue());

        }

        return NumberUtils.formatNumber(total, 2);
    }

}