package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;

public class ResidenceEventWrapper implements Wrapper {
    private ResidenceEvent event;

    public ResidenceEventWrapper(ResidenceEvent event) {
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
	return "-";
    }

    @Override
    public String getDegreeName() {
	return "-";
    }

    @Override
    public String getDegreeType() {
	return "-";
    }

    @Override
    public String getPhdProgramName() {
	return "-";
    }

    @Override
    public String getEnrolledECTS() {
	return "-";
    }

    @Override
    public String getRegime() {
	return "-";
    }

    @Override
    public String getEnrolmentModel() {
	return "-";
    }

    @Override
    public String getResidenceYear() {
	return event.getResidenceMonth().getYear().getYear().toString();
    }

    @Override
    public String getResidenceMonth() {
	return event.getResidenceMonth().getMonth().getName();
    }

    @Override
    public String getStudiesType() {
	return "-";
    }

}
