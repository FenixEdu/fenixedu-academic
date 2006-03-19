/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {

    public StudentTestQuestion() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        removeDistributedTest();
        removeQuestion();
        removeStudent();
        super.deleteDomainObject();
    }

}
