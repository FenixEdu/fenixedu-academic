package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

abstract public class EnrolmentWrapper extends EnrolmentWrapper_Base {
    
    protected EnrolmentWrapper() {
	super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    protected void init(final Credits credits) {
	if (credits == null) {
	    throw new DomainException("error.EnrolmentWrapper.credits.cannot.be.null");
	}
	super.setCredits(credits);
    }
    
    @Override
    public void setCredits(Credits credits) {
        throw new DomainException("error.EnrolmentWrapper.cannot.modify.credits");
    }

    public void delete() {
	super.setCredits(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
    abstract public IEnrolment getIEnrolment();
    
    
    static public EnrolmentWrapper create(final Credits credits, final IEnrolment enrolment) {
	
	if (enrolment.isExternalEnrolment()) {
	    return new ExternalEnrolmentWrapper(credits, (ExternalEnrolment) enrolment);
	    
	} else if (enrolment.isEnrolment()) {
	    return new InternalEnrolmentWrapper(credits, (Enrolment) enrolment);
	    
	} else {
	    throw new DomainException("error.EnrolmentWrapper.unknown.enrolment");
	}
    }
}
