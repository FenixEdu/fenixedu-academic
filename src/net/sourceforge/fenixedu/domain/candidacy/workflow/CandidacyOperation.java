package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.IStateWithOperations;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public abstract class CandidacyOperation extends Operation {

    public static Comparator<CandidacyOperation> COMPARATOR_BY_TYPE = new Comparator<CandidacyOperation>() {
	public int compare(CandidacyOperation leftCandidacyOperation,
		CandidacyOperation rightCandidacyOperation) {
	    return leftCandidacyOperation.getType().compareTo(rightCandidacyOperation.getType());

	}
    };

    private DomainReference<Candidacy> candidacy;

    protected CandidacyOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
	super(roleTypes);
	setCandidacy(candidacy);
    }

    public Candidacy getCandidacy() {
	return (this.candidacy != null) ? this.candidacy.getObject() : null;
    }

    private void setCandidacy(Candidacy candidacy) {
	this.candidacy = (candidacy != null) ? new DomainReference<Candidacy>(candidacy) : null;
    }

    @Override
    public IStateWithOperations getState() {
	return getCandidacy().getActiveCandidacySituation();
    }

    public int compareTo(Operation operation) {
	return ((CandidacyOperation) operation).getType().compareTo(getType());
    }

    public abstract CandidacyOperationType getType();

}