package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentWrapper extends EnrolmentWrapper_Base {
    
    private IEnrolment enrolment;
    
    public  EnrolmentWrapper(IEnrolment enrolment) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setObjectId(enrolment.getIdInternal());
        setClassName(enrolment.getClass().getName());
    }
    
    public IEnrolment getIEnrolment() {
	if(enrolment == null) {
	    try {
		Class classType = Class.forName(getClassName());
		enrolment = (IEnrolment) RootDomainObject.getInstance().readDomainObjectByOID(classType, getObjectId());
	    } catch (ClassNotFoundException e) {
		throw new DomainException("reference.notFound.class", e, getClassName());
	    }
	}	
	return enrolment;
    }
    
    public void delete() {
	removeCredits();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
