/*
 * Created on 17/Jun/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import java.util.List;

/**
 * @author dcs-rjao
 *
 * 17/Jun/2003
 */
public interface ICurricularCourseEquivalence extends IDomainObject{
	public abstract ICurricularCourse getCurricularCourse();
	public List getEquivalenceRestrictions();
	public abstract void setCurricularCourse(ICurricularCourse course);
	public void setEquivalenceRestrictions(List list);

}