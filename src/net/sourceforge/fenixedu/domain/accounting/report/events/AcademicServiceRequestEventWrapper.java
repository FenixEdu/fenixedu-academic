package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AcademicServiceRequestEventWrapper implements Wrapper {

    private AcademicServiceRequestEvent event;
    private AcademicServiceRequest request;

    public AcademicServiceRequestEventWrapper(AcademicServiceRequestEvent event) {
	this.event = event;
	this.request = event.getAcademicServiceRequest();
    }

    public String getStudentNumber() {
	if (request.getPerson().hasStudent()) {
	    return request.getPerson().getStudent().getNumber().toString();
	}

	return "-";
    }

    public String getStudentName() {
	return request.getPerson().getName();
    }

    @Override
    public String getStudentEmail() {
	return event.getPerson().getDefaultEmailAddressValue();
    }

    public String getRegistrationStartDate() {
	if (request.isRequestForRegistration()) {
	    return ((RegistrationAcademicServiceRequest) request).getRegistration().getStartDate().toString("dd/MM/yyyy");
	}

	return "-";
    }

    public String getExecutionYear() {
	return getForExecutionYear().getName();
    }

    public String getDegreeName() {
	if (request.isRequestForRegistration()) {
	    return ((RegistrationAcademicServiceRequest) request).getRegistration().getDegree().getNameI18N()
		    .getContent(Language.pt);
	}

	return "-";
    }

    public String getDegreeType() {
	if (request.isRequestForRegistration()) {
	    return ((RegistrationAcademicServiceRequest) request).getRegistration().getDegreeType().getLocalizedName();
	}

	return "-";
    }

    public String getPhdProgramName() {
	if (request.isRequestForPhd()) {
	    return ((PhdAcademicServiceRequest) request).getPhdIndividualProgramProcess().getPhdProgram().getName()
		    .getContent(Language.pt);
	}

	return "-";
    }

    public String getEnrolledECTS() {
	if (request.isRequestForRegistration()) {
	    Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
	    ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

	    return new BigDecimal(registration.getEnrolmentsEcts(executionYear)).toString();
	}

	return "-";
    }

    public String getRegime() {
	if (request.isRequestForRegistration()) {
	    Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
	    ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

	    return registration.getRegimeType(executionYear).getLocalizedName();
	}

	return "-";
    }

    public String getEnrolmentModel() {
	if (request.isRequestForRegistration()) {
	    Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
	    ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

	    if (registration.getEnrolmentModelForExecutionYear(executionYear) != null) {
		return registration.getEnrolmentModelForExecutionYear(executionYear).getLocalizedName();
	    }

	}

	return "-";
    }

    public String getResidenceYear() {
	return "-";
    }

    public String getResidenceMonth() {
	return "-";
    }

    public String getStudiesType() {
	if (request.isRequestForRegistration()) {
	    return Wrapper.REGISTRATION_STUDIES;
	} else if (request.isRequestForPhd()) {
	    return Wrapper.PHD_PROGRAM_STUDIES;
	}

	return "-";
    }

    @Override
    public String getTotalDiscount() {
	return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return !ExecutionYear.readByDateTime(request.getCreationDate()).isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
	return ExecutionYear.readByDateTime(request.getCreationDate());
    }

    @Override
    public AdministrativeOfficeType getRelatedAcademicOfficeType() {
	if (request.isRequestForRegistration()) {
	    Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();

	    DegreeType degreeType = registration.getDegreeType();

	    switch (degreeType) {
	    case DEGREE:
	    case BOLONHA_DEGREE:
	    case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    case BOLONHA_MASTER_DEGREE:
		return AdministrativeOfficeType.DEGREE;
	    case MASTER_DEGREE:
	    case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
		return AdministrativeOfficeType.MASTER_DEGREE;
	    }
	}

	if (request.isRequestForPhd()) {
	    return AdministrativeOfficeType.MASTER_DEGREE;
	}

	return null;
    }

}
