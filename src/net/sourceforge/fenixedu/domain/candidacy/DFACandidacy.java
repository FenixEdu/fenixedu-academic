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

public class DFACandidacy extends DFACandidacy_Base {

    public DFACandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        if (executionDegree == null) {
            throw new DomainException("execution degree cannot be null");
        }
        if (person == null) {
            throw new DomainException("person cannot be null");
        }
        if (person.getDFACandidacyByExecutionDegree(executionDegree) != null) {
            throw new DomainException("error.candidacy.already.created");
        }
        setExecutionDegree(executionDegree);
        setPerson(person);
        new PreCandidacySituation(this);
        setPrecedentDegreeInformation(new PrecedentDegreeInformation());

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
                .getCandidacySituationType() == CandidacySituationType.CANCELED);
    }
}