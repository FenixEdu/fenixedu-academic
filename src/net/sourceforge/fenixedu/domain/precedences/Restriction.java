package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;



/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class Restriction extends Restriction_Base {
    
    public Restriction() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
	
	public void delete() {
		removePrecedence();
		removeRootDomainObject();
        super.deleteDomainObject();
	}

	public abstract CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext);
}