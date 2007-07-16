package net.sourceforge.fenixedu.util;

import org.joda.time.DateTime;

public enum Month {
    
    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);

    private int numberOfMonth;
    
    private Month(int num) {
	numberOfMonth = num;
    }
    
    public String getName() {
	return name();
    }

    public int getNumberOfMonth() {
	return numberOfMonth; 
    } 
    
    public static Month fromDateTime(DateTime time) {
    	return Month.values()[time.getMonthOfYear() - 1];
    }
}
