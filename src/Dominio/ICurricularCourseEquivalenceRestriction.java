/*
 * Created on 7/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author dcs-rjao
 *
 * 7/Jul/2003
 */
public interface ICurricularCourseEquivalenceRestriction extends IDomainObject{

	public abstract ICurricularCourseEquivalence getCurricularCourseEquivalence();
	public abstract ICurricularCourse getEquivalentCurricularCourse();
	public abstract void setCurricularCourseEquivalence(ICurricularCourseEquivalence equivalence);
	public abstract void setEquivalentCurricularCourse(ICurricularCourse course);
}