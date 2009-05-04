package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituation;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PHDProgramCandidacy extends PHDProgramCandidacy_Base {

    public PHDProgramCandidacy(Person person, ExecutionDegree executionDegree) {
	super();
	init(person, executionDegree);

    }

    public PHDProgramCandidacy(Person person) {
	super();
	init(person);
    }

    @Override
    public String getDescription() {
	return ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale()).getString(
		"label.phdProgramCandidacy")
		+ " - "
		+ getExecutionDegree().getDegreeCurricularPlan().getName()
		+ " - "
		+ getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
	return Collections.emptySet();
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
	return Collections.emptyMap();
    }

}
