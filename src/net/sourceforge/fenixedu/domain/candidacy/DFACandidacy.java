package net.sourceforge.fenixedu.domain.candidacy;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

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
            throw new DomainException("person already has candidacy for this execution degree");
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
        return ResourceBundle.getBundle("resources.CandidateResources").getString("label.dfaCandidacy")
                + " - " + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

}
