/*
 * Created on 22/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Calendar;
import java.util.Comparator;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 22/Jul/2003 fenix-head DataBeans.comparators
 *  
 */
public class CalendarDateComparator implements Comparator {

    /**
     *  
     */
    public CalendarDateComparator() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        Calendar calendar0 = (Calendar) arg0;
        Calendar calendar1 = (Calendar) arg1;
        if (calendar0.get(Calendar.YEAR) < calendar1.get(Calendar.YEAR)) {
            return -1;
        }
        if (calendar0.get(Calendar.YEAR) > calendar1.get(Calendar.YEAR)) {
            return 1;
        }
        if (calendar0.get(Calendar.MONTH) < calendar1.get(Calendar.MONTH)) {
            return -1;
        }
        if (calendar0.get(Calendar.MONTH) > calendar1.get(Calendar.MONTH)) {
            return 1;
        }
        if (calendar0.get(Calendar.DAY_OF_MONTH) < calendar1.get(Calendar.DAY_OF_MONTH)) {
            return -1;
        }
        if (calendar0.get(Calendar.DAY_OF_MONTH) > calendar1.get(Calendar.DAY_OF_MONTH)) {
            return 1;
        }
        return 0;
    }

}