package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class DocumentRequestBean extends AcademicServiceRequestBean {

    private Integer numberOfPages;

    public DocumentRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
            final Person responsible, final String justification, final Integer numberOfPages) {
        super(academicServiceRequestSituationType, responsible, justification);
        setNumberOfPages(numberOfPages);
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
