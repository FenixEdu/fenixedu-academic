package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import java.util.Locale;

public class SDCandidacy extends SDCandidacy_Base {

    public SDCandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);
    }

    @Override
    public String getDescription() {
        return ResourceBundle.getBundle("resources.CandidateResources", I18N.getLocale()).getString("label.sdCandidacy")
                + " - " + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return new HashSet<Operation>();
    }

    @Override
    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return null;
    }

}
