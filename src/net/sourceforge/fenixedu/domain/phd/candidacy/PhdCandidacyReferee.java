package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.UUID;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCandidacyRefereeAlert;

public class PhdCandidacyReferee extends PhdCandidacyReferee_Base {

    private PhdCandidacyReferee() {
	super();
    }

    public PhdCandidacyReferee(final PhdProgramCandidacyProcess process, final PhdCandidacyRefereeBean bean) {
	this();

	check(process, "error.PhdCandidacyReferee.invalid.process");
	check(bean.getName(), "error.PhdCandidacyReferee.invalid.name");
	check(bean.getEmail(), "error.PhdCandidacyReferee.invalid.email");

	setPhdProgramCandidacyProcess(process);
	setName(bean.getName());
	setEmail(bean.getEmail());
	setInstitution(bean.getInstitution());
	setValue(UUID.randomUUID().toString());

	new PhdCandidacyRefereeAlert(this);
    }

    @Override
    public boolean hasCandidacyProcess() {
	return hasPhdProgramCandidacyProcess();
    }

    public boolean isLetterAvailable() {
	return hasLetter();
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
	return getPhdProgramCandidacyProcess().getIndividualProgramProcess();
    }
}
