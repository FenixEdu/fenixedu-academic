package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

import org.joda.time.LocalDate;

public class SearchDiplomasBySituationParametersBean extends AcademicServiceRequestBean implements Serializable {

    LocalDate searchBegin;

    LocalDate searchEnd;

    public SearchDiplomasBySituationParametersBean(final Employee employee) {
	super(employee, (String) null);
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
	return getAdministrativeOffice().searchRegistrationAcademicServiceRequests(getSearchBegin(), getSearchEnd(),
		getAcademicServiceRequestSituationType(), getCampus());
    }

}
