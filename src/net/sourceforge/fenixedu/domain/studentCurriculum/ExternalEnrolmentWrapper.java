package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.IEnrolment;

public class ExternalEnrolmentWrapper extends ExternalEnrolmentWrapper_Base {
    
    private ExternalEnrolmentWrapper() {
        super();
    }
    
    protected ExternalEnrolmentWrapper(final Credits credits, final ExternalEnrolment externalEnrolment) {
	this();
	super.init(credits);
	init(externalEnrolment);
    }

    private void init(final ExternalEnrolment externalEnrolment) {
	if (externalEnrolment == null) {
	    throw new RuntimeException("error.EnrolmentWrapper.enrolment.cannot.be.null");
	}
	super.setEnrolment(externalEnrolment);
    }
    
    @Override
    public void setEnrolment(ExternalEnrolment enrolment) {
	throw new RuntimeException("error.EnrolmentWrapper.cannot.modify.enrolment");
    }

    @Override
    public IEnrolment getIEnrolment() {
	return getEnrolment();
    }
    
    @Override
    public void delete() {
        super.setEnrolment(null);
        super.delete();
    }
}
