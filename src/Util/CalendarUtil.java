/*
 * Created on 3/Dez/2003
 *
 */
package Util;

import java.util.Calendar;

/**
 * @author Ana e Ricardo
 *
 */
public class CalendarUtil
{	
    
    public CalendarUtil(){
	}

	/**
	 * @param date1, date2
	 * @return true if date2 is after or the same as date1
	 */
	public boolean dateAfter(Calendar date1, Calendar date2)
	{
		if(date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR))
			return false;
		else if(date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR))
			return true;
		else
		{
			if(date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH))
				return false;
			else if(date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH))
				return true;
			else
			{
				if(date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH))
					return false;
				else if(date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH))
					return true;
				else
					return true;
			}
		}
	}
	
	/**
	 * @param date1, date2
	 * @return true if date2 is before or the same as date1
	 */
	public boolean dateBefore(Calendar date1, Calendar date2)
	{
		if(date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR))
			return false;
		else if(date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR))
			return true;
		else
		{
			if(date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH))
				return false;
			else if(date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH))
				return true;
			else
			{
				if(date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH))
					return false;
				else if(date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH))
					return true;
				else
					return true;
			}
		}	    
	}
	
	/**
	 * @param time1, time2
	 * @return true if time2 is after or the same as time1
	 */
	public boolean timeAfter(Calendar time1, Calendar time2)
	{
		if(time1.get(Calendar.HOUR_OF_DAY) > time2.get(Calendar.HOUR_OF_DAY))
		    return false;
		else if(time1.get(Calendar.HOUR_OF_DAY) < time2.get(Calendar.HOUR_OF_DAY))
		    return true;
		else
		{
			if(time1.get(Calendar.MINUTE) > time2.get(Calendar.MINUTE))
			    return false;
			else if(time1.get(Calendar.MINUTE) < time2.get(Calendar.MINUTE))
			    return true;
			else
			{
				if(time1.get(Calendar.SECOND) > time2.get(Calendar.SECOND))
				    return false;
				else if(time1.get(Calendar.SECOND) < time2.get(Calendar.SECOND))
				    return true;
				else
				    return true;
			}
		}
	}
	
	/**
	 * @param time1, time2
	 * @return true if time2 is before or the same as time1
	 */
	public boolean timeBefore(Calendar time1, Calendar time2)
	{
		if(time1.get(Calendar.HOUR_OF_DAY) < time2.get(Calendar.HOUR_OF_DAY))
			return false;
		else if(time1.get(Calendar.HOUR_OF_DAY) > time2.get(Calendar.HOUR_OF_DAY))
			return true;
		else
		{
			if(time1.get(Calendar.MINUTE) < time2.get(Calendar.MINUTE))
				return false;
			else if(time1.get(Calendar.MINUTE) > time2.get(Calendar.MINUTE))
				return true;
			else
			{
				if(time1.get(Calendar.SECOND) < time2.get(Calendar.SECOND))
					return false;
				else if(time1.get(Calendar.SECOND) > time2.get(Calendar.SECOND))
					return true;
				else
					return true;
			}
		}		
	}
}
