package Util;

import java.util.ArrayList;
import java.util.List;


/**
 * @author David Santos
 * Feb 6, 2004
 */

public class PeriodToApplyRestriction extends FenixUtil
{
	public static final int BOTH_SEMESTERS_INT = 0;
	public static final int FIRST_SEMESTER_INT = 1;
	public static final int SECOND_SEMESTER_INT = 2;

	public static final PeriodToApplyRestriction BOTH_SEMESTERS = new PeriodToApplyRestriction(PeriodToApplyRestriction.BOTH_SEMESTERS_INT); 
	public static final PeriodToApplyRestriction FIRST_SEMESTER = new PeriodToApplyRestriction(PeriodToApplyRestriction.FIRST_SEMESTER_INT);
	public static final PeriodToApplyRestriction SECOND_SEMESTER = new PeriodToApplyRestriction(PeriodToApplyRestriction.SECOND_SEMESTER_INT);

	private Integer period;

	public PeriodToApplyRestriction()
	{
	}

	public PeriodToApplyRestriction(int period)
	{
		this.period = new Integer(period);
	}

	public PeriodToApplyRestriction(Integer period)
	{
		this.period = period;
	}

	/**
	 * @return Returns the period.
	 */
	public Integer getPeriod()
	{
		return period;
	}

	/**
	 * @param period The period to set.
	 */
	public void setPeriod(Integer period)
	{
		this.period = period;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof PeriodToApplyRestriction)
		{
			PeriodToApplyRestriction periodToApplyRestriction = (PeriodToApplyRestriction) obj;
			result = this.getPeriod().equals(periodToApplyRestriction.getPeriod());
		}
		return result;
	}
	
	public List listToChoose() {
	    List list = new ArrayList();
	    
//	    list.add(new ValuedEnum(new String(), BOTH_SEMESTERS_INT));
//	    list.add(new ValuedEnum(new String(), FIRST_SEMESTER_INT));
//	    list.add(new ValuedEnum(new String(), SECOND_SEMESTER_INT));
	    
	    return list;
	}
}