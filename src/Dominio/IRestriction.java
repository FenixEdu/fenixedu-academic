/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author jpvl
 */
public interface IRestriction {
	/**
	 * @return
	 */
	public abstract Precedence getPrecedence();
	/**
	 * @param precedence
	 */
	public abstract void setPrecedence(Precedence precedence);
	

	/**
	 * This method evaluates if this restriction using one list of curricularCourses.
	 * @param curricularCourses List of curricular courses.
	 * @return boolean returns true if restriction is true.
	 */
	public abstract boolean evaluate(EnrolmentContext enrolmentContext);
}