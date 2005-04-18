package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public abstract class CurricularCourseGroup extends CurricularCourseGroup_Base {

    protected IBranch branch;

    protected List curricularCourses;

    protected List scientificAreas;

    /**
     *  
     */
    public CurricularCourseGroup() {
        super();
        this.setOjbConcreteClass(this.getClass().getName());
    }

    public IBranch getBranch() {
        return branch;
    }

    public void setBranch(IBranch branch) {
        this.branch = branch;
    }

    public List getCurricularCourses() {
        return curricularCourses;
    }

    public void setCurricularCourses(List curricularCourses) {
        this.curricularCourses = curricularCourses;
    }

    public List getScientificAreas() {
        return scientificAreas;
    }

    public void setScientificAreas(List scientificAreas) {
        this.scientificAreas = scientificAreas;
    }
}