package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

abstract public class EnrolmentWrapper extends EnrolmentWrapper_Base {

    protected EnrolmentWrapper() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final Credits credits) {
        String[] args = {};
        if (credits == null) {
            throw new DomainException("error.EnrolmentWrapper.credits.cannot.be.null", args);
        }
        super.setCredits(credits);
    }

    @Override
    public void setCredits(Credits credits) {
        throw new DomainException("error.EnrolmentWrapper.cannot.modify.credits");
    }

    public void delete() {
        super.setCredits(null);
        setRootDomainObject(null);
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
    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

}
