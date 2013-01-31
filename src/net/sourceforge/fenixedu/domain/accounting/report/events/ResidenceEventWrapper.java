package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class ResidenceEventWrapper implements Wrapper {
	private final ResidenceEvent event;

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
	public String getStudentEmail() {
		return event.getPerson().getDefaultEmailAddressValue();
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

	@Override
	public String getTotalDiscount() {
		return event.getTotalDiscount().toPlainString();
	}

	@Override
	public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ExecutionYear getForExecutionYear() {
		return null;
	}

	@Override
	public AdministrativeOffice getRelatedAcademicOffice() {
		return null;
	}

}
