package Dominio;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import Util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionPeriodToApply extends Restriction implements IRestrictionPeriodToApply
{
	protected PeriodToApplyRestriction periodToApplyRestriction;
	
	public RestrictionPeriodToApply()
	{
	}

	/**
	 * @return Returns the periodToApplyRestriction.
	 */
	public PeriodToApplyRestriction getPeriodToApplyRestriction()
	{
		return periodToApplyRestriction;
	}

	/**
	 * @param periodToApplyRestriction The periodToApplyRestriction to set.
	 */
	public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction)
	{
		this.periodToApplyRestriction = periodToApplyRestriction;
	}

	public boolean equals(Object obj)
	{
		boolean result = super.equals(obj);
		if ((result) && (obj instanceof IRestrictionPeriodToApply))
		{
			IRestrictionPeriodToApply restrictionPeriodToApply = (IRestrictionPeriodToApply) obj;
			result =
				restrictionPeriodToApply.getPeriodToApplyRestriction().equals(this.getPeriodToApplyRestriction())
					&& this.getClass().getName().equals(restrictionPeriodToApply.getClass().getName());
		} else if (result)
		{
			result = false;
		}
		return result;
	}

	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
		stringBuffer.append(this.getPeriodToApplyRestriction()).append("\n");
		return stringBuffer.toString();
	}


	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		IExecutionPeriod executionPeriod = studentEnrolmentContext.getExecutionPeriod();
		boolean result = false;

		if (this.getPeriodToApplyRestriction().equals(PeriodToApplyRestriction.BOTH_SEMESTERS))
		{
			result = true;
		} else if (executionPeriod.getSemester().equals(this.getPeriodToApplyRestriction().getPeriod()))
		{
			result = true;
		}

		return result;
	}
}