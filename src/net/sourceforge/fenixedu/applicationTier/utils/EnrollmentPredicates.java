/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.apache.commons.collections.Predicate;

/**
 * @author Andre Fernandes / Joao Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EnrollmentPredicates
{
	/* devolve um Predicate que aceita IEnrollments que tenham o estado como APROVED (sic) */
	public static Predicate getApprovedPredicate()
	{
		return new Predicate()
		{
			public boolean evaluate(Object object)
			{
				if (object instanceof Enrolment)
				{
					Enrolment en = (Enrolment)object;
					if (en.getEnrollmentState().equals(EnrollmentState.APROVED) ||
					    en.getEnrollmentState().equals(EnrollmentState.ENROLLED) ||
					    en.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLLED))
						return true;
				}
				
				return false;
			}
		};
	}
	
	/* devolve um Predicate que aceita IEnrollments que tenham o estado como ENROLLED */
	public static Predicate getEnrolledPredicate()
	{
		return new Predicate()
		{
			public boolean evaluate(Object object)
			{
				if (object instanceof Enrolment)
				{
					Enrolment en = (Enrolment)object;
					if (en.getEnrollmentState().equals(EnrollmentState.ENROLLED) ||
						en.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLLED))
						return true;
				}
				
				return false;
			}
		};
	}
	
	/* devolve um Predicate que aceita quaisquer IEnrollments */
	public static Predicate getAllPredicate()
	{
		return new Predicate()
		{
			public boolean evaluate(Object object)
			{
				if (object instanceof Enrolment)
				{
					return true;
				}
				
				return false;
			}
		};
	}
	
	/* devolve um Predicate que nao aceita nenhum Enrolment */
	public static Predicate getNonePredicate()
	{
		return new Predicate()
		{
			public boolean evaluate(Object object)
			{
				return false;
			}
		};
	}
	
/*	public static Predicate getPredicate(Object o)
	{
	    
	    
	}
	*/
}
