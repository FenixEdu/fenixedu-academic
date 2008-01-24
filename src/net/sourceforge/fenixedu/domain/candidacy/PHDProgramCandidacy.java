package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class PHDProgramCandidacy extends PHDProgramCandidacy_Base {

    private static Map<String, Set<String>> stateMap;

    static {
	stateMap = new HashMap<String, Set<String>>();
	
	Set<String> standBy = new HashSet<String>();
	standBy.add(CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
	standBy.add(CandidacySituationType.CANCELLED.toString());
	stateMap.put(CandidacySituationType.STAND_BY.toString(),standBy);

	Set<String> standByConfirmedData = new HashSet<String>();
	standByConfirmedData.add(CandidacySituationType.ADMITTED.toString());
	standByConfirmedData.add(CandidacySituationType.CANCELLED.toString());
	stateMap.put(CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString(), standByConfirmedData);
	
	Set<String> admitted = new HashSet<String>();
	admitted.add(CandidacySituationType.REGISTERED.toString());
	admitted.add(CandidacySituationType.CANCELLED.toString());
	stateMap.put(CandidacySituationType.ADMITTED.toString(), admitted);
	
	stateMap.put(CandidacySituationType.REGISTERED.toString(), new HashSet<String>());

    }

    public PHDProgramCandidacy(Person person, ExecutionDegree executionDegree) {
	super();
	init(person, executionDegree);
	new StandByCandidacySituation(this);

	//final AdministrativeOffice administrativeOffice = AdministrativeOffice
	//	.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
	//new PhDCandidacyEvent(administrativeOffice, person, this);
    }

    @Override
    public String getDescription() {
	return ResourceBundle.getBundle("resources.CandidateResources", LanguageUtils.getLocale())
		.getString("label.phdProgramCandidacy")
		+ " - "
		+ getExecutionDegree().getDegreeCurricularPlan().getName()
		+ " - "
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
    public String getDefaultState() {
	switch (getActiveCandidacySituation().getCandidacySituationType()) {
	case STAND_BY: 
	    return CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString();
	case STAND_BY_CONFIRMED_DATA:
	    return CandidacySituationType.ADMITTED.toString();
	default:
	    return null;
	}
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
	return stateMap;
    }

}
