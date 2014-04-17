package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class AcademicServiceRequestCreateBean extends RegistrationSelectExecutionYearBean {

    private DateTime requestDate = new DateTime();

    private Boolean urgentRequest = Boolean.FALSE;

    private Boolean freeProcessed = Boolean.FALSE;

    private Locale language = Locale.getDefault();

    public AcademicServiceRequestCreateBean(Registration registration) {
        super(registration);
    }

    final public DateTime getRequestDate() {
        return requestDate;
    }

    final public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }

    final public Boolean getUrgentRequest() {
        return urgentRequest;
    }

    final public void setUrgentRequest(Boolean urgentRequest) {
        this.urgentRequest = urgentRequest;
    }

    final public Boolean getFreeProcessed() {
        return freeProcessed;
    }

    final public void setFreeProcessed(Boolean freeProcessed) {
        this.freeProcessed = freeProcessed;
    }

    final public Locale getLanguage() {
        return language;
    }

    final public void setLanguage(final Locale language) {
        this.language = language;
    }

}
