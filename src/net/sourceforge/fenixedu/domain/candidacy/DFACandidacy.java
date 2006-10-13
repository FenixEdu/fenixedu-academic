package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class DFACandidacy extends DFACandidacy_Base {

    public DFACandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);
        
        new PreCandidacySituation(this);

        addCandidacyDocuments(new CandidacyDocument("curriculum.vitae"));
        addCandidacyDocuments(new CandidacyDocument("habilitation.certificate"));
        addCandidacyDocuments(new CandidacyDocument("second.habilitation.certificate"));
        addCandidacyDocuments(new CandidacyDocument("interest.letter"));

    }

    @Override
    public String getDescription() {
        return ResourceBundle.getBundle("resources.CandidateResources", LanguageUtils.getLocale()).getString("label.dfaCandidacy")
                + " - " + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return new HashSet<Operation>();
    }

    @Override
    void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isConcluded() {
        return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
                .getCandidacySituationType() == CandidacySituationType.CANCELLED);
    }
}