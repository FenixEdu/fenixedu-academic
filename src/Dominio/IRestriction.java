/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public interface IRestriction extends IDomainObject{

	public abstract IPrecedence getPrecedence();

	public abstract void setPrecedence(IPrecedence precedence);

	/**
	 * This method evaluates if this restriction using one list of curricularCourses.
	 * @param curricularCourses List of curricular courses.
	 * @return boolean returns true if restriction is true.
	 */
	public abstract boolean evaluate(EnrolmentContext enrolmentContext);
}