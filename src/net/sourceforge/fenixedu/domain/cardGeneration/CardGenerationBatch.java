package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

public class CardGenerationBatch extends CardGenerationBatch_Base {

    public static class CardGenerationBatchCreator implements FactoryExecutor {

	private final ExecutionYear executionYear;

	public CardGenerationBatchCreator(final ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public Object execute() {
	    return new CardGenerationBatch(executionYear);
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

    public CardGenerationBatch(final ExecutionYear executionYear) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExecutionYear(executionYear);
        final DateTime dateTime = new DateTime();
        setCreated(dateTime);
        setUpdated(dateTime);
        setPeopleForEntryCreation(getAllPeopleForEntryCreation());
        //createCardGenerationEntries();
    }

    public void delete() {
	removeExecutionYear();
	removeRootDomainObject();
	deleteDomainObject();
    }

    protected String getAllPeopleForEntryCreation() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Party party : getRootDomainObject().getPartysSet()) {
	    if (party.isPerson()) {
		if (party.isPerson()) {
		    final Person person = (Person) party;
		    if (stringBuilder.length() > 0) {
			stringBuilder.append(";");
		    }
		    stringBuilder.append(person.getIdInternal());
		}
	    }
	}
	return stringBuilder.toString();
    }

    protected void createCardGenerationEntries() {
	for (final Party party : getRootDomainObject().getPartysSet()) {
	    if (party.isPerson()) {
		final Person person = (Person) party;
		createCardGenerationEntries(person);
	    }
	}
    }

    protected void createCardGenerationEntries(final Person person) {
	final Student student = person.getStudent();
	if (student != null) {
	    for (final Registration registration : student.getRegistrationsSet()) {
		for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		    if (studentCurricularPlan.isActive()) {
			final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
			cardGenerationEntry.setCreated(getCreated());
			cardGenerationEntry.setCardGenerationBatch(this);
			cardGenerationEntry.setPerson(person);
			cardGenerationEntry.setLine(studentCurricularPlan);

			final DegreeType degreeType = studentCurricularPlan.getDegreeType();
			if (!degreeType.isBolonhaType()) {
			    new CardGenerationProblem(this, "message.active.non.bolonha.student.curricular.plan.for", registration.getNumber().toString());
			}
		    }
		}
	    }
	}
    }

}
