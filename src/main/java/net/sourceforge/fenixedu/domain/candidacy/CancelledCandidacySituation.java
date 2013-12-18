package net.sourceforge.fenixedu.domain.candidacy;

import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CancelledCandidacySituation extends CancelledCandidacySituation_Base {

    public CancelledCandidacySituation(Candidacy candidacy, Person person) {
        super();
        super.init(candidacy, person);

        if (getCandidacy() instanceof DFACandidacy) {
            ((DFACandidacy) getCandidacy()).cancelEvents();
        }

    }

    public CancelledCandidacySituation(Candidacy candidacy) {
        this(candidacy, (Authenticate.getUser() != null) ? AccessControl.getPerson() : null);
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.CANCELLED;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }

    @Override
    public boolean getCanGeneratePass() {
        return false;
    }

}
