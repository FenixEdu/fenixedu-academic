package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class SDCandidacy extends SDCandidacy_Base {
    
    public  SDCandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);
    }
    
    @Override
    public String getDescription() {
        return ResourceBundle.getBundle("resources.CandidateResources", LanguageUtils.getLocale()).getString("label.sdCandidacy")
                + " - " + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return new HashSet<Operation>();
    }

    @Override
    void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public boolean isConcluded() {
        return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
                .getCandidacySituationType() == CandidacySituationType.CANCELED);
    }
    
}
