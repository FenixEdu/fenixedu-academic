package Dominio;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * 
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public interface IRestriction extends IDomainObject
{
	public abstract IPrecedence getPrecedence();

	public abstract void setPrecedence(IPrecedence precedence);

	/**
	 * This method evaluates if this restriction using one list of curricularCourses.
	 * 
	 * @param curricularCourses - List of curricular courses.
	 * @return boolean returns true if restriction is true.
	 */
	public abstract boolean evaluate(StudentEnrolmentContext studentEnrolmentContext);
}