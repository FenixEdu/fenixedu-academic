/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.utils;

import org.apache.commons.collections.Predicate;

import Util.EnrollmentState;

import Dominio.IEnrollment;

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
				if (object instanceof IEnrollment)
				{
					IEnrollment en = (IEnrollment)object;
					if (en.getEnrollmentState().equals(EnrollmentState.APROVED))
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
				if (object instanceof IEnrollment)
				{
					IEnrollment en = (IEnrollment)object;
					if (en.getEnrollmentState().equals(EnrollmentState.ENROLLED) ||
						en.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLED))
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
				if (object instanceof IEnrollment)
				{
					return true;
				}
				
				return false;
			}
		};
	}
	
	/* devolve um Predicate que nao aceita nenhum IEnrollment */
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
