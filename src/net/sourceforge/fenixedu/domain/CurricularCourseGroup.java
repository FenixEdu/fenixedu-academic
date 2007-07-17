package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public abstract class CurricularCourseGroup extends CurricularCourseGroup_Base {

    public CurricularCourseGroup() {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() throws DomainException {
	if (!hasAnyCurricularCourses()) {
	    removeBranch();
	    getScientificAreas().clear();
	    removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    throw new DomainException("error.curricular.course.group.has.associated.curricular.courses");
	}
    }

    public void edit(String name, Branch branch, Integer minimumValue, Integer maximumValue) {
    }

    public void edit(String name, Branch branch, Integer minimumValue, Integer maximumValue, AreaType areaType) {
    }

    public abstract Integer getMaximumCredits();
    public abstract Integer getMinimumCredits();
    public abstract Integer getMinimumNumberOfOptionalCourses();
    public abstract Integer getMaximumNumberOfOptionalCourses();

    public abstract void setMaximumCredits(Integer maximumCredits);
    public abstract void setMinimumCredits(Integer minimumCredits);
    public abstract void setMinimumNumberOfOptionalCourses(Integer minimumNumberOfOptionalCourses);
    public abstract void setMaximumNumberOfOptionalCourses(Integer maximumNumberOfOptionalCourses);
    
    public abstract net.sourceforge.fenixedu.tools.enrollment.AreaType getAreaType();
    public abstract void setAreaType(net.sourceforge.fenixedu.tools.enrollment.AreaType areaType);
	
}