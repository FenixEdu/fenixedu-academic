package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.IStateWithOperations;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public abstract class CandidacyOperation extends Operation {

    public static Comparator<CandidacyOperation> COMPARATOR_BY_TYPE = new Comparator<CandidacyOperation>() {
        @Override
        public int compare(CandidacyOperation leftCandidacyOperation, CandidacyOperation rightCandidacyOperation) {
            return leftCandidacyOperation.getType().compareTo(rightCandidacyOperation.getType());

        }
    };

    private Candidacy candidacy;

    protected CandidacyOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes);
        setCandidacy(candidacy);
    }

    public Candidacy getCandidacy() {
        return this.candidacy;
    }

    private void setCandidacy(Candidacy candidacy) {
        this.candidacy = candidacy;
    }

    @Override
    public IStateWithOperations getState() {
        return getCandidacy().getActiveCandidacySituation();
    }

    @Override
    public int compareTo(Operation operation) {
        return ((CandidacyOperation) operation).getType().compareTo(getType());
    }

    public abstract CandidacyOperationType getType();

}