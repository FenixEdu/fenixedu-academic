package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class SearchDiplomasBySituationParametersBean extends AcademicServiceRequestBean {

    LocalDate searchBegin;

    LocalDate searchEnd;

    public SearchDiplomasBySituationParametersBean(final Person responsible) {
        super(responsible, (String) null);
    }

    public LocalDate getSearchBegin() {
        return searchBegin;
    }

    public void setSearchBegin(LocalDate searchBegin) {
        this.searchBegin = searchBegin;
    }

    public LocalDate getSearchEnd() {
        return searchEnd;
    }

    public void setSearchEnd(LocalDate searchEnd) {
        this.searchEnd = searchEnd;
    }

    @Override
    public Collection<AcademicServiceRequest> searchAcademicServiceRequests() {
        return AcademicAuthorizationGroup.getAcademicServiceRequests(AccessControl.getPerson(), serviceRequestYear,
                academicServiceRequestSituationType,
                new Interval(searchBegin.toDateTimeAtStartOfDay(), searchEnd.toDateTimeAtStartOfDay()));
    }

}
