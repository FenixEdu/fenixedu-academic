/*
 * Created on 25/Nov/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio;

import java.util.List;

import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public interface ICurricularCourseGroup extends IDomainObject
{
	public AreaType getAreaType();
	public Integer getMaximumCredits();
	public IBranch getBranch();
	public Integer getMinimumCredits();
	public List getCurricularCourses();
	public List getScientificAreas();
	
	public void setAreaType(AreaType areaType);
	public void setMaximumCredits(Integer maximumCredits);
	public void setBranch(IBranch branch);
	public void setMinimumCredits(Integer minimumCredits);
	public void setCurricularCourses(List curricularCourses);
	public void setScientificAreas(List scientificAreas);
}
