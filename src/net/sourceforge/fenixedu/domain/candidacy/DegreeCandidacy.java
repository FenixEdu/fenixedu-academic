package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.workflow.FillPersonalDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintRegistrationDeclarationOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintScheduleOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintSystemAccessDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.RegistrationOperation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

public class DegreeCandidacy extends DegreeCandidacy_Base {

    private DegreeCandidacy() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree,
	    final Person creator, final Double entryGrade,
	    final String contigent, final String ingression, final String istUniversity,
	    EntryPhase entryPhase) {
	this();
	init(person, executionDegree, creator,entryGrade, contigent, ingression,
		istUniversity, entryPhase);
    }

    private void checkParameters(final Person person, final ExecutionDegree executionDegree,
	    final Person creator, Double entryGrade, String contigent,
	    String ingression, String istUniversity, EntryPhase entryPhase) {
	if (executionDegree == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.executionDegree.cannot.be.null");
	}

	if (person == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.person.cannot.be.null");
	}

	if (person.hasDegreeCandidacyForExecutionDegree(executionDegree)) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.candidacy.already.created");
	}

	if (creator == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.creator.cannot.be.null");
	}

	if (entryGrade == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.entryGrade.cannot.be.null");
	}

	if (contigent == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.contigent.cannot.be.null");
	}

	if (ingression == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.ingression.cannot.be.null");
	}

	if (istUniversity == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.istUniversity.cannot.be.null");
	}

	if (entryPhase == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.entryPhase.cannot.be.null");
	}

    }

    protected void init(final Person person, final ExecutionDegree executionDegree,
	    final Person creator, Double entryGrade, String contigent,
	    String ingression, String istUniversity, EntryPhase entryPhase) {
	checkParameters(person, executionDegree, creator, entryGrade, contigent,
		ingression, istUniversity, entryPhase);
	super.setExecutionDegree(executionDegree);
	super.setPerson(person);
	super.setPrecedentDegreeInformation(new PrecedentDegreeInformation());
	super.setEntryGrade(entryGrade);
	super.setContigent(contigent);
	super.setIngression(ingression);
	super.setIstUniversity(istUniversity);
	super.setEntryPhase(entryPhase);

//	if (person.hasRole(RoleType.PERSON)) {
//	    new AdmittedCandidacySituation(this, creator);
//	} else {
	    new StandByCandidacySituation(this, creator);
	//}
    }

    @Override
    public void setExecutionDegree(ExecutionDegree executionDegree) {
	throw new DomainException("error.candidacy.DegreeCandidacy.cannot.modify.executionDegree");
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.candidacy.DegreeCandidacy.cannot.modify.person");
    }

    @Override
    public void setPrecedentDegreeInformation(PrecedentDegreeInformation precedentDegreeInformation) {
	throw new DomainException(
		"error.candidacy.DegreeCandidacy.cannot.modify.precedentDegreeInformation");
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.CandidateResources").getString(
		"label.studentCandidacy")
		+ " - "
		+ getExecutionDegree().getDegreeCurricularPlan().getName()
		+ " - "
		+ getExecutionDegree().getExecutionYear().getYear();
    }

    // Static Methods
    public static Set<DegreeCandidacy> readAllCandidacies() {
	final Set<DegreeCandidacy> degreeCandidacies = new HashSet<DegreeCandidacy>();
	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
	    if (candidacy instanceof DegreeCandidacy) {
		degreeCandidacies.add((DegreeCandidacy) candidacy);
	    }
	}
	return degreeCandidacies;
    }

    public static Set<DegreeCandidacy> readAllCandidaciesInStandBy() {
	final Set<DegreeCandidacy> degreeCandidacies = new HashSet<DegreeCandidacy>();
	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
	    if (candidacy instanceof DegreeCandidacy) {
		final DegreeCandidacy degreeCandidacy = (DegreeCandidacy) candidacy;
		if (degreeCandidacy.getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.STAND_BY)
		    degreeCandidacies.add((DegreeCandidacy) degreeCandidacy);
	    }
	}
	return degreeCandidacies;
    }

    public static Set<DegreeCandidacy> readAllDegreeCandidaciesBetweenNumbers(final int fromNumber,
	    final int toNumber) {
	final Set<DegreeCandidacy> studentsCandidacies = new HashSet<DegreeCandidacy>();
	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
	    if (candidacy instanceof DegreeCandidacy) {
		DegreeCandidacy studentCandidacy = (DegreeCandidacy) candidacy;
		final int candidacyNumber = studentCandidacy.getNumber();
		if (candidacyNumber >= fromNumber && candidacyNumber <= toNumber)
		    studentsCandidacies.add(studentCandidacy);
	    }
	}
	return studentsCandidacies;
    }

    @SuppressWarnings("unchecked")
    public static int generateCandidateNumber() {
	int number = 0;
	final TreeSet<DegreeCandidacy> degreeCandidacies = new TreeSet<DegreeCandidacy>(
		new ReverseComparator(new BeanComparator("number")));
	degreeCandidacies.addAll(readAllCandidacies());

	if (!degreeCandidacies.isEmpty()) {
	    number = degreeCandidacies.iterator().next().getNumber();
	}
	return number + 1;
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
	final Set<Operation> operations = new HashSet<Operation>();
	switch (candidacySituation.getCandidacySituationType()) {
	case STAND_BY:
	    operations
		    .add(new FillPersonalDataOperation(Collections.singleton(RoleType.CANDIDATE), this));
	    break;
	case ADMITTED:
	    operations.add(new RegistrationOperation(Collections.singleton(RoleType.CANDIDATE), this));
	    break;
	case REGISTERED:
	    operations.add(new PrintScheduleOperation(Collections.singleton(RoleType.STUDENT), this));
	    operations.add(new PrintRegistrationDeclarationOperation(Collections.singleton(RoleType.STUDENT),
		    this));
	    operations.add(new PrintSystemAccessDataOperation(Collections.singleton(RoleType.STUDENT),
		    this));
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
	return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED);
    }

}