/*
 * Created on 25/Nov/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public interface ICurricularCourseGroup extends IDomainObject {
    public AreaType getAreaType();

    public Integer getMaximumCredits();

    public IBranch getBranch();

    public Integer getMinimumCredits();

    public List getCurricularCourses();

    public List getScientificAreas();

    public Integer getMinimumNumberOfOptionalCourses();

    public Integer getMaximumNumberOfOptionalCourses();

    public String getName();

    public void setAreaType(AreaType areaType);

    public void setMaximumNumberOfOptionalCourses(Integer maximumNumberOfOptionalCourses);

    public void setMinimumNumberOfOptionalCourses(Integer minimumNumberOfOptionalCourses);

    public void setBranch(IBranch branch);

    public void setMinimumCredits(Integer minimumCredits);

    public void setCurricularCourses(List curricularCourses);

    public void setScientificAreas(List scientificAreas);

    public void setMaximumCredits(Integer maximumCredits);

    public void setName(String name);
}