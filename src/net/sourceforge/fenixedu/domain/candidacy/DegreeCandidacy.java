package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.workflow.FillPersonalDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintRegistrationDeclarationOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintScheduleOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintSystemAccessDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.RegistrationOperation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class DegreeCandidacy extends DegreeCandidacy_Base {

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree) {
	super();
	init(person, executionDegree);
    }

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree, final Person creator,
	    final Double entryGrade, final String contigent, final String ingression, EntryPhase entryPhase) {
	super();
	init(person, executionDegree, creator, entryGrade, contigent, ingression, entryPhase);
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.CandidateResources", LanguageUtils.getLocale()).getString(
		"label.degreeCandidacy")
		+ " - "
		+ getExecutionDegree().getDegreeCurricularPlan().getName()
		+ " - "
		+ getExecutionDegree().getExecutionYear().getYear();
    }

//    // Static Methods
//    public static Set<DegreeCandidacy> readAllCandidacies() {
//	final Set<DegreeCandidacy> degreeCandidacies = new HashSet<DegreeCandidacy>();
//	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
//	    if (candidacy instanceof DegreeCandidacy) {
//		degreeCandidacies.add((DegreeCandidacy) candidacy);
//	    }
//	}
//	return degreeCandidacies;
//    }
//
//    public static Set<DegreeCandidacy> readAllCandidaciesInStandBy() {
//	final Set<DegreeCandidacy> degreeCandidacies = new HashSet<DegreeCandidacy>();
//	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
//	    if (candidacy instanceof DegreeCandidacy) {
//		final DegreeCandidacy degreeCandidacy = (DegreeCandidacy) candidacy;
//		if (degreeCandidacy.getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.STAND_BY)
//		    degreeCandidacies.add((DegreeCandidacy) degreeCandidacy);
//	    }
//	}
//	return degreeCandidacies;
//    }
//
//    public static Set<DegreeCandidacy> readAllDegreeCandidaciesBetweenNumbers(final int fromNumber, final int toNumber) {
//	final Set<DegreeCandidacy> studentsCandidacies = new HashSet<DegreeCandidacy>();
//	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
//	    if (candidacy instanceof DegreeCandidacy) {
//		DegreeCandidacy studentCandidacy = (DegreeCandidacy) candidacy;
//		final int candidacyNumber = studentCandidacy.getNumber();
//		if (candidacyNumber >= fromNumber && candidacyNumber <= toNumber)
//		    studentsCandidacies.add(studentCandidacy);
//	    }
//	}
//	return studentsCandidacies;
//    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
	final Set<Operation> operations = new HashSet<Operation>();
	switch (candidacySituation.getCandidacySituationType()) {
	case STAND_BY:
	    operations.add(new FillPersonalDataOperation(Collections.singleton(RoleType.CANDIDATE), this));
	    break;
	case ADMITTED:
	    operations.add(new RegistrationOperation(Collections.singleton(RoleType.CANDIDATE), this));
	    break;
	case REGISTERED:
	    operations.add(new PrintScheduleOperation(Collections.singleton(RoleType.STUDENT), this));
	    operations.add(new PrintRegistrationDeclarationOperation(Collections.singleton(RoleType.STUDENT), this));
	    operations.add(new PrintSystemAccessDataOperation(Collections.singleton(RoleType.STUDENT), this));
	    break;
	}
	return operations;
    }

    @Override
    void moveToNextState(final CandidacyOperationType operationType, Person person) {
	switch (getActiveCandidacySituation().getCandidacySituationType()) {

	case STAND_BY:
	    if (operationType == CandidacyOperationType.FILL_PERSONAL_DATA) {
		new AdmittedCandidacySituation(this, person);
	    }
	    break;

	case ADMITTED:
	    if (operationType == CandidacyOperationType.REGISTRATION) {
		new RegisteredCandidacySituation(this, person);
	    }
	    break;

	}
    }

    @Override
    public boolean isConcluded() {
	return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
		.getCandidacySituationType() == CandidacySituationType.CANCELLED);
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