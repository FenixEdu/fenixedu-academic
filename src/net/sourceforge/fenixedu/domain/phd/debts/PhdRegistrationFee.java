package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import pt.ist.fenixWebFramework.services.Service;

public class PhdRegistrationFee extends PhdRegistrationFee_Base {

    private PhdRegistrationFee() {
	super();
    }

    public PhdRegistrationFee(final PhdIndividualProgramProcess process) {
	this();
	init(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE), process.getPerson(),
		process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdIndividualProgramProcess process) {
	super.init(administrativeOffice, EventType.PHD_REGISTRATION_FEE, person);
	checkProcess(process);
	super.setProcess(process);
    }

    private void checkProcess(PhdIndividualProgramProcess process) {
	check(process, "error.PhdRegistrationFee.process.cannot.be.null");

	if (process.hasRegistrationFee()) {
	    throw new DomainException("error.PhdRegistrationFee.process.already.has.registration.fee");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Override
    protected PhdProgram getPhdProgram() {
	return getProcess().getPhdProgram();
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasPhdRegistrationFeePenaltyExemption() {
	return getPhdRegistrationFeePenaltyExemption() != null;
    }

    public PhdRegistrationFeePenaltyExemption getPhdRegistrationFeePenaltyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof PhdRegistrationFeePenaltyExemption) {
		return (PhdRegistrationFeePenaltyExemption) exemption;
	    }
	}

	return null;
    }

    @Service
    static public PhdRegistrationFee create(final PhdIndividualProgramProcess process) {
	return new PhdRegistrationFee(process);
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	return getProcess();
    }
}
