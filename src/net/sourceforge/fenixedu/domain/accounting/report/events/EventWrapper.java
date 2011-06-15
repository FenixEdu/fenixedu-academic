package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.specializationDegree.SpecializationDegreeRegistrationEvent;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EventWrapper implements Wrapper {
    private Event event;

    public EventWrapper(Event event) {
	this.event = event;
    }

    @Override
    public String getStudentNumber() {
	if (event.getPerson().hasStudent()) {
	    return event.getPerson().getStudent().getNumber().toString();
	}

	return "-";
    }

    @Override
    public String getStudentName() {
	return event.getPerson().getName();
    }

    @Override
    public String getRegistrationStartDate() {
	return "-";
    }

    @Override
    public String getExecutionYear() {
	ExecutionYear executionYear = readExecutionYear();
	return executionYear.getName();
    }

    private ExecutionYear readExecutionYear() {
	ExecutionYear executionYear = null;
	if (event.isDfaRegistrationEvent()) {
	    executionYear = ((DfaRegistrationEvent) event).getExecutionYear();
	} else if (event.isEnrolmentOutOfPeriod()) {
	    executionYear = ((EnrolmentOutOfPeriodEvent) event).getExecutionPeriod().getExecutionYear();
	} else if (event.isAnnual()) {
	    executionYear = ((AnnualEvent) event).getExecutionYear();
	} else {
	    executionYear = ExecutionYear.readByDateTime(event.getWhenOccured());
	}

	return executionYear;
    }

    @Override
    public String getDegreeName() {
	if (hasRegistration()) {
	    return getRegistration().getDegree().getNameI18N().getContent(Language.pt);
	}

	return "-";
    }

    @Override
    public String getDegreeType() {
	if (hasRegistration()) {
	    return getRegistration().getDegreeType().getLocalizedName();
	}

	return "-";
    }

    @Override
    public String getPhdProgramName() {
	return "-";
    }

    @Override
    public String getEnrolledECTS() {
	if (hasRegistration()) {
	    return new BigDecimal(getRegistration().getLastStudentCurricularPlan().getEnrolmentsEctsCredits(readExecutionYear()))
		    .toString();
	}

	return "-";
    }

    @Override
    public String getRegime() {
	if (hasRegistration()) {
	    return getRegistration().getRegimeType(readExecutionYear()).getLocalizedName();
	}

	return "-";
    }

    @Override
    public String getEnrolmentModel() {
	if (!hasRegistration()) {
	    return "-";
	}

	EnrolmentModel enrolmentModelForExecutionYear = getRegistration().getEnrolmentModelForExecutionYear(readExecutionYear());
	if (enrolmentModelForExecutionYear != null) {
	    return enrolmentModelForExecutionYear.getLocalizedName();
	}

	return "-";
    }

    @Override
    public String getResidenceYear() {
	return "-";
    }

    @Override
    public String getResidenceMonth() {
	return "-";
    }

    @Override
    public String getStudiesType() {
	if (hasRegistration()) {
	    return "Curso";
	}

	return "-";
    }

    private Registration getRegistration() {
	if (event.isDfaRegistrationEvent()) {
	    return ((DfaRegistrationEvent) event).getRegistration();
	} else if (event.isEnrolmentOutOfPeriod()) {
	    return ((EnrolmentOutOfPeriodEvent) event).getStudentCurricularPlan().getRegistration();
	} else if (event.isSpecializationDegreeRegistrationEvent()) {
	    return ((SpecializationDegreeRegistrationEvent) event).getRegistration();
	}

	return null;
    }

    private boolean hasRegistration() {
	return getRegistration() != null;
    }

    @Override
    public String getTotalDiscount() {
	return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return !readExecutionYear().isBefore(executionYear);
    }

}
