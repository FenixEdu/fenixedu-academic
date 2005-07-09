package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public abstract class CurricularCourseGroup extends CurricularCourseGroup_Base {

    public CurricularCourseGroup() {
        super();
        this.setOjbConcreteClass(this.getClass().getName());
    }
	
	public void deleteCurricularCourseGroup() throws DomainException {
		if (getCurricularCourses() == null || getCurricularCourses().isEmpty()) {
			setBranch(null);
			getScientificAreas().clear();
		} else {
			throw new DomainException(this.getClass().getName(), "ola mundo");			
		}
	}
}