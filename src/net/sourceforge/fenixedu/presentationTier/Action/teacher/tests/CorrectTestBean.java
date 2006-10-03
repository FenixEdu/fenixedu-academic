package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.Grade;
import net.sourceforge.fenixedu.domain.tests.NewTest;

public class CorrectTestBean implements Serializable {

	private NewTest test;

	private Person person;

	public CorrectTestBean(NewTest test, Person person) {
		super();
		this.test = test;
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public NewTest getTest() {
		return test;
	}
	
	public int getAllUncorrectedQuestionsCount() {
		return test.getAllUncorrectedQuestionsCount(person);
	}
	
	public Grade getFinalGrade() {
		return test.getFinalGrade(person);
	}

}
