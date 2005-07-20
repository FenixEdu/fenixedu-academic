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
        this.setOjbConcreteClass(this.getClass().getName());
    }
	
	public void delete() throws DomainException {
		if (!hasAnyCurricularCourses()) {
			removeBranch();
			getScientificAreas().clear();
			deleteDomainObject();
		} else {
			throw new DomainException(this.getClass().getName(), "ola mundo");			
		}
	}
	
	public void edit(String name, IBranch branch, Integer minimumValue, Integer maximumValue){}
	
	public void edit(String name, IBranch branch, Integer minimumValue, Integer maximumValue, AreaType areaType){}
}