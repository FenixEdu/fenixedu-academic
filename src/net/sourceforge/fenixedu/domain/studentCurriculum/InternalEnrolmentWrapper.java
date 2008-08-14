package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InternalEnrolmentWrapper extends InternalEnrolmentWrapper_Base {

    protected InternalEnrolmentWrapper() {
	super();
    }

    protected InternalEnrolmentWrapper(final Credits credits, final Enrolment enrolment) {
	this();
	super.init(credits);
	init(enrolment);
    }

    private void init(final Enrolment enrolment) {
	if (enrolment == null) {
	    throw new DomainException("error.EnrolmentWrapper.enrolment.cannot.be.null");
	}
	super.setEnrolment(enrolment);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
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
