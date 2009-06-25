package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class CardGenerationBatch extends CardGenerationBatch_Base {

    public static final Comparator<CardGenerationBatch> COMPARATOR_BY_CREATION_DATE = new Comparator<CardGenerationBatch>() {

	@Override
	public int compare(CardGenerationBatch o1, CardGenerationBatch o2) {
	    final int c = o1.getCreated().compareTo(o2.getCreated());
	    return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
	}
	
    };

    public static class CardGenerationBatchCreator implements FactoryExecutor {

	private final ExecutionYear executionYear;
	private final boolean emptyCardGenerationBatch;

	public CardGenerationBatchCreator(final ExecutionYear executionYear, final boolean emptyCardGenerationBatch) {
	    this.executionYear = executionYear;
	    this.emptyCardGenerationBatch = emptyCardGenerationBatch;
	}

	public Object execute() {
	    return new CardGenerationBatch(null, executionYear, emptyCardGenerationBatch);
	}

    }

    public static class CardGenerationBatchDeleter implements FactoryExecutor {

	private final CardGenerationBatch cardGenerationBatch;

	public CardGenerationBatchDeleter(final CardGenerationBatch cardGenerationBatch) {
	    this.cardGenerationBatch = cardGenerationBatch;
	}

	public Object execute() {
	    if (cardGenerationBatch != null) {
		cardGenerationBatch.delete();
	    }
	    return null;
	}
    }

    public CardGenerationBatch(String description, final ExecutionYear executionYear, boolean createEmptyBatch) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setExecutionYear(executionYear);
	setDescription(description);
	final DateTime dateTime = new DateTime();
	setCreated(dateTime);
	setUpdated(dateTime);
	if (!createEmptyBatch) {
	    setPeopleForEntryCreation(getAllPeopleForEntryCreation());
	}
    }

    public void delete() {
	removeExecutionYear();
	removeRootDomainObject();
	for (final CardGenerationProblem cardGenerationProblem : getCardGenerationProblemsSet()) {
	    cardGenerationProblem.delete();
	}
	for (final CardGenerationEntry cardGenerationEntry : getCardGenerationEntriesSet()) {
	    cardGenerationEntry.delete();
	}
	deleteDomainObject();
    }

    @Override
    @Service
    public void setSent(DateTime dateTime) {
	super.setSent(dateTime);
    }

    protected String getAllPeopleForEntryCreation() {
	return "ALL";
    }

    public void createCardGenerationEntries(final DateTime begin, final DateTime end, final Person person) {
	final Student student = person.getStudent();
	if (student != null) {
	    final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlans(begin, end, student);

	    if (!studentCurricularPlans.isEmpty()) {
		final DegreeType maxDegreeType = findMaxDegreeType(studentCurricularPlans);
		for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
		    final DegreeType degreeType = studentCurricularPlan.getDegreeType();
		    if (!degreeType.isEmpty()) {
			if (degreeType.compareTo(maxDegreeType) >= 0) {
			    // System.out.println("Using: " + degreeType +
			    // " same as " + maxDegreeType);
			    createCardGenerationEntry(person, studentCurricularPlan);
			} else {
			    System.out.println("Not using: " + degreeType + " because of " + maxDegreeType);
			}
		    }
		}
	    }
	}
    }

    public Set<StudentCurricularPlan> getStudentCurricularPlans(final DateTime begin, final DateTime end, final Student student) {
	final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();

	for (final Registration registration : student.getRegistrationsSet()) {
	    if (registration.getRegistrationAgreement() != RegistrationAgreement.NORMAL) {
		continue;
	    }
	    final DegreeType degreeType = registration.getDegreeType();
	    if (!degreeType.isBolonhaType()) {
		continue;
	    }
	    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		if (studentCurricularPlan.isActive()) {
		    if (degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
			    || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
			    || degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
			studentCurricularPlans.add(studentCurricularPlan);
		    } else {
			final RegistrationState registrationState = registration.getActiveState();
			if (registrationState != null) {
			    final DateTime dateTime = registrationState.getStateDate();
			    if (!dateTime.isBefore(begin) && !dateTime.isAfter(end)) {
				studentCurricularPlans.add(studentCurricularPlan);
			    }
			}
		    }
		}
	    }
	}
	return studentCurricularPlans;
    }

    public static DegreeType findMaxDegreeType(final Set<StudentCurricularPlan> studentCurricularPlans) {
	return Collections.max(studentCurricularPlans, StudentCurricularPlan.COMPARATOR_BY_DEGREE_TYPE).getDegreeType();
    }

    protected void createCardGenerationEntry(final Person person, final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getDegreeType() == DegreeType.MASTER_DEGREE) {
	    System.out.println("Master degree student curricular plan: "
		    + studentCurricularPlan.getDegreeCurricularPlan().getName() + " - " + studentCurricularPlan.getDegreeType()
		    + " " + person.getUsername());
	}
	final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
	cardGenerationEntry.setCreated(getCreated());
	cardGenerationEntry.setCardGenerationBatch(this);
	cardGenerationEntry.setPerson(person);
	cardGenerationEntry.setLine(studentCurricularPlan);

	final ExecutionYear executionYear = getExecutionYear();
	for (final CardGenerationEntry otherCardGenerationEntry : person.getCardGenerationEntriesSet()) {
	    if (otherCardGenerationEntry != cardGenerationEntry) {
		final CardGenerationBatch cardGenerationBatch = otherCardGenerationEntry.getCardGenerationBatch();
		if (cardGenerationBatch.getExecutionYear() == executionYear) {
		    if (cardGenerationBatch == this) {
			new CardGenerationProblem(this, "message.person.has.multiple.entries.in.same.batch",
				person.getUsername(), person);
		    } else {
			new CardGenerationProblem(this, "message.person.has.multiple.entries.in.same.execution.year", person
				.getUsername(), person);
		    }
		}
	    }
	}
    }

    public boolean contains(final Person person) {
	for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
	    if (cardGenerationEntry.getCardGenerationBatch() == this) {
		return true;
	    }
	}
	return false;
    }

    public CardGenerationEntry createCardGenerationEntries(final String line) {
	final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
	cardGenerationEntry.setCreated(getCreated());
	cardGenerationEntry.setCardGenerationBatch(this);
	cardGenerationEntry.setPerson(null);
	cardGenerationEntry.setLine(CardGenerationEntry.normalize(line));
	return cardGenerationEntry;
    }

    public CardGenerationBatch getCardGenerationBatchProblems() {
	final ExecutionYear executionYear = getExecutionYear();
	final String description = "Com Problemas";
	for (final CardGenerationBatch cardGenerationBatch : executionYear.getCardGenerationBatchesSet()) {
	    if (description.equals(cardGenerationBatch.getDescription())) {
		return cardGenerationBatch;
	    }
	}
	return new CardGenerationBatch(description, executionYear, true);
    }

    private StudentCurricularPlan findStudentCurricularPlan(final Student student, final DateTime begin, final DateTime end) {
	final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlans(begin, end, student);
	if (studentCurricularPlans.size() == 1) {
	    return studentCurricularPlans.iterator().next();
	} else if (studentCurricularPlans.size() > 1) {
	    final StudentCurricularPlan max = findMaxStudentCurricularPlan(studentCurricularPlans);
	    return max;
	}
	return null;
    }

    private StudentCurricularPlan findMaxStudentCurricularPlan(final Set<StudentCurricularPlan> studentCurricularPlans) {
	return Collections.max(studentCurricularPlans, new Comparator<StudentCurricularPlan>() {

	    @Override
	    public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
		final DegreeType degreeType1 = o1.getDegreeType();
		final DegreeType degreeType2 = o2.getDegreeType();
		if (degreeType1 == degreeType2) {
		    final YearMonthDay yearMonthDay1 = o1.getStartDateYearMonthDay();
		    final YearMonthDay yearMonthDay2 = o2.getStartDateYearMonthDay();
		    final int c = yearMonthDay1.compareTo(yearMonthDay2);
		    return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
		} else {
		    return degreeType1.compareTo(degreeType2);
		}
	    }
	    
	});
    }

    public static SortedSet<CardGenerationBatch> getAvailableBatchesFor() {
	final SortedSet<CardGenerationBatch> cardGenerationBatchs = new TreeSet<CardGenerationBatch>(COMPARATOR_BY_CREATION_DATE);
	for (final CardGenerationBatch cardGenerationBatch : RootDomainObject.getInstance().getCardGenerationBatchesSet()) {
	    final ExecutionYear executionYear = cardGenerationBatch.getExecutionYear();
	    if (executionYear.isCurrent()) {
		cardGenerationBatchs.add(cardGenerationBatch);
	    }
	}
	return cardGenerationBatchs;
    }

    @Service
    public void createCardGenerationEntry(final Student student) {
	final ExecutionYear executionYear = getExecutionYear();
	final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
	final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();;
	
	final Person person = student.getPerson();
	if (person != null) {
	    final Teacher teacher = person.getTeacher();
	    final Employee employee = person.getEmployee();

	    if ((teacher == null || !teacher.isActive()) && (employee == null || !employee.isActive())
		    && !student.getActiveRegistrations().isEmpty()) {
		final StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(student, begin, end);
		if (studentCurricularPlan != null && studentCurricularPlan.getDegreeType() != DegreeType.EMPTY) {
		    final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
		    cardGenerationEntry.setCreated(new DateTime());
		    cardGenerationEntry.setCardGenerationBatch(this);
		    cardGenerationEntry.setPerson(person);
		    cardGenerationEntry.setLine(studentCurricularPlan);
		}
	    }
	}
    }

}
