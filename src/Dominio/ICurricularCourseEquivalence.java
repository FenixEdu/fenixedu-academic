/*
 * Created on 17/Jun/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author dcs-rjao
 *
 * 17/Jun/2003
 */
public interface ICurricularCourseEquivalence {
	public abstract ICurricularCourse getCurricularCourse();
	public abstract ICurricularCourse getEquivalentCurricularCourse();
	public abstract void setCurricularCourse(ICurricularCourse course);
	public abstract void setEquivalentCurricularCourse(ICurricularCourse course);
}