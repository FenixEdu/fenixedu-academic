package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class DocumentRequestBean extends AcademicServiceRequestBean {
    
    private Integer numberOfPages;

    public DocumentRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee, final String justification, final Integer numberOfPages) {
	super(academicServiceRequestSituationType, employee, justification);
	setNumberOfPages(numberOfPages);
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
