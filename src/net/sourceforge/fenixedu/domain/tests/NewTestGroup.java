package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class NewTestGroup extends NewTestGroup_Base {

	public NewTestGroup() {
		super();

		this.setRootDomainObject(RootDomainObject.getInstance());
		this.setStatus(TestGroupStatus.CREATED);
	}

	public NewTestGroup(String name, Teacher owner, ExecutionCourse executionCourse, DateTime finalDate) {
		this();

		this.setName(name);
		this.setCreator(owner);
		this.setExecutionCourse(executionCourse);
		this.setFinalDate(finalDate);
	}

	public int getQuestionCount() {
		return this.getTests().get(0).getQuestionCount();
	}

	public List<NewTest> getOrderedTests() {
		List<NewTest> tests = new ArrayList<NewTest>(getTests());

		Collections.sort(tests, Positionable.POSITION_COMPARATOR);

		return tests;
	}

	public void publish() {
		if (!isPublishable()) {
			throw new DomainException("could.not.publish");
		}
		this.setStatus(TestGroupStatus.PUBLISHED);
		this.setBeginDate(new DateTime());
		if (this.getFinalDate() == null || this.getFinalDate().isBeforeNow()) {
			this.setFinalDate(null);
		}
	}

	public boolean isPublishable() {
		return this.getStatus().equals(TestGroupStatus.CREATED)
				|| this.getStatus().equals(TestGroupStatus.FINISHED);
	}

	public void unpublish() {
		if (!isUnpublishable()) {
			throw new DomainException("could.not.unpublish");
		}
		this.setStatus(TestGroupStatus.CREATED);
	}

	public boolean isUnpublishable() {
		return this.getStatus().equals(TestGroupStatus.PUBLISHED);
	}

	public void finish() {
		if (!isFinishable()) {
			throw new DomainException("could.not.finish");
		}
		this.setStatus(TestGroupStatus.FINISHED);
		if (this.getFinalDate() == null || this.getFinalDate().isBeforeNow()) {
			this.setFinalDate(new DateTime());
		}
	}

	public void correct() {
		if (!isCorrectable()) {
			throw new DomainException("could.not.correct");
		}
		this.setStatus(TestGroupStatus.CORRECTING);
	}

	public boolean isFinishable() {
		return this.istPublished();
	}

	public boolean istPublished() {
		return this.getStatus().equals(TestGroupStatus.PUBLISHED);
	}

	public boolean isCorrectable() {

		return !this.isCorrected() && this.isFinished() || this.isCorrecting();
		// return this.getStatus().equals(TestGroupStatus.FINISHED)
		// || this.getStatus().equals(TestGroupStatus.CORRECTING);
	}

	public boolean isFinished() {
		return this.getFinalDate() == null ? false : this.getFinalDate().isBeforeNow();
		// return this.getStatus().equals(TestGroupStatus.FINISHED);
	}

	public boolean isCorrecting() {
		return this.getStatus().equals(TestGroupStatus.CORRECTING);
	}

	public boolean isCorrected() {
		return this.getStatus().equals(TestGroupStatus.CORRECTED);
	}

	public boolean isDeletable() {
		return this.isCreated();
		// return this.getStatus().equals(TestGroupStatus.CREATED);
	}

	public boolean isCreated() {
		return this.getStatus().equals(TestGroupStatus.CREATED);
	}

	public List<Person> getPersons() {
		List<Person> persons = new ArrayList<Person>();

		for (NewTest test : this.getTests()) {
			persons.addAll(test.getPersons());
		}

		return persons;
	}

	public void delete() {
		if (!isDeletable()) {
			throw new DomainException("could.not.delete");
		}
		for (; this.hasAnyTests(); this.getTests().get(0).delete())
			;

		this.removeCreator();
		this.removeExecutionCourse();
		this.removeRootDomainObject();

		super.deleteDomainObject();
	}

	public NewTest getOrAssignTest(Person person) {
		for (NewTest test : this.getTests()) {
			if (test.getPersons().contains(person)) {
				return test;
			}
		}

		NewTest candidate = getLeastUsedTest();

		candidate.addPersons(person);

		return candidate;
	}

	public NewTest getTest(Person person) {
		for (NewTest test : this.getTests()) {
			if (test.getPersons().contains(person)) {
				return test;
			}
		}

		return null;
	}

	public NewTest getTest() {
		return this.getTest(AccessControl.getPerson());
	}

	private NewTest getLeastUsedTest() {
		NewTest candidate = this.getTests().get(0);

		for (NewTest test : this.getTests()) {
			if (test.getPersonsCount() < candidate.getPersonsCount()) {
				candidate = test;
			}
		}

		return candidate;
	}

	public void publishGrades() {
		this.setStatus(TestGroupStatus.CORRECTED);

		for (NewTest test : this.getTests()) {
			test.publishGrades();
		}
	}

	public Grade getGrade() {
		return this.getTest().getFinalGrade();
	}

}
