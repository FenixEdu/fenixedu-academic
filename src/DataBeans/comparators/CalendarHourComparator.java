/*
 * Created on 22/Jul/2003
 *
 * 
 */
package DataBeans.comparators;

import java.util.Calendar;
import java.util.Comparator;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 22/Jul/2003 fenix-head DataBeans.comparators
 *  
 */
public class CalendarHourComparator implements Comparator {

    /**
     *  
     */
    public CalendarHourComparator() {
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
        if (calendar0.get(Calendar.HOUR_OF_DAY) < calendar1.get(Calendar.HOUR_OF_DAY)) {
            return -1;
        }
        if (calendar0.get(Calendar.HOUR_OF_DAY) > calendar1.get(Calendar.HOUR_OF_DAY)) {
            return 1;
        }
        if (calendar0.get(Calendar.MINUTE) < calendar1.get(Calendar.MINUTE)) {
            return -1;
        }
        if (calendar0.get(Calendar.MINUTE) > calendar1.get(Calendar.MINUTE)) {
            return 1;
        }

        return 0;
    }

}