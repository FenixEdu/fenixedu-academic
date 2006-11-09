package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class NewTest extends NewTest_Base implements Positionable {

	public NewTest() {
		super();
	}

	public NewTest(NewTestGroup testGroup, double scale) {
		this();

		this.setTestGroup(testGroup);
		this.setScale(scale);
		this.setPosition(testGroup.getTestsCount());
	}

	@Override
	public NewTest getTest() {
		return this;
	}

	public boolean isFirst() {
		return this.getPosition() == 1;
	}

	public boolean isLast() {
		return this.getPosition() == this.getTestGroup().getTestsCount();
	}

	public void switchPosition(Integer relativePosition) {
		int currentPosition = this.getPosition();
		int newPosition = currentPosition + relativePosition;
		NewTestGroup testGroup = this.getTestGroup();

		if (relativePosition < 0 && this.isFirst()) {
			throw new DomainException("could.not.sort.up");
		}

		if (relativePosition > 0 && this.isLast()) {
			throw new DomainException("could.not.sort.down");
		}

		for (NewTest test : testGroup.getTests()) {
			if (test.getPosition() == newPosition) {
				test.setPosition(currentPosition);
				break;
			}
		}

		this.setPosition(newPosition);
	}

	@Override
	public void delete() {
		this.removeTestGroup();

		for (; this.hasAnyPersons(); this.removePersons(this.getPersons().get(0)))
			;

		super.delete();
	}
	
	public Person getPerson() {
		return AccessControl.getUserView().getPerson();
	}

}
