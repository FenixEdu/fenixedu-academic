package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class AdmittedCandidacySituation extends AdmittedCandidacySituation_Base {

    public AdmittedCandidacySituation(Candidacy candidacy) {
	this(candidacy, AccessControl.getPerson());
    }

    public AdmittedCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.ADMITTED;
    }

    @Override
    public boolean getCanRegister() {
	return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return (getCandidacy() instanceof DegreeCandidacy || getCandidacy() instanceof IMDCandidacy);
    }

}
