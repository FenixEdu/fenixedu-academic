package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalCourseLoadRequest extends ExternalCourseLoadRequest_Base {

    protected ExternalCourseLoadRequest() {
        super();
    }

    public ExternalCourseLoadRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setNumberOfCourseLoads(bean.getNumberOfCourseLoads());
        super.setInstitution(bean.getInstitution());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getNumberOfCourseLoads() == null || bean.getNumberOfCourseLoads().intValue() == 0) {
            throw new DomainException("error.ExternalCourseLoadRequest.invalid.numberOfCourseLoads");
        }
        if (bean.getInstitution() == null) {
            throw new DomainException("error.ExternalCourseLoadRequest.invalid.institution");
        }
    }

    @Override
    public List<Enrolment> getEnrolments() {
        return Collections.unmodifiableList(super.getEnrolments());
    }

    @Override
    public Set<Enrolment> getEnrolmentsSet() {
        return Collections.unmodifiableSet(super.getEnrolmentsSet());
    }

    @Override
    public Iterator<Enrolment> getEnrolmentsIterator() {
        return getEnrolmentsSet().iterator();
    }

    @Override
    public void addEnrolments(Enrolment enrolments) {
        throw new DomainException("error.ExternalCourseLoadRequest.cannot.add.enrolments");
    }

    @Override
    public void removeEnrolments(Enrolment enrolments) {
        throw new DomainException("error.ExternalCourseLoadRequest.cannot.remove.enrolments");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.EXTERNAL_COURSE_LOAD;
    }

    @Override
    public EventType getEventType() {
        return EventType.EXTERNAL_COURSE_LOAD_REQUEST;
    }

    @Override
    protected void disconnect() {
        super.setInstitution(null);
        super.disconnect();
    }

}
