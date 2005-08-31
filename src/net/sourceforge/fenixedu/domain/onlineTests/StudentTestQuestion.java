/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {

    public void delete() {
        removeDistributedTest();
        removeQuestion();
        removeStudent();
        super.deleteDomainObject();
    }

}
