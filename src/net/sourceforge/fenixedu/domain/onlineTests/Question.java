/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class Question extends Question_Base {

    public Question() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        removeMetadata();
        removeRootDomainObject();
        getStudentTestsQuestions().clear();
        getTestQuestions().clear();
        super.deleteDomainObject();
    }

    public Set<DistributedTest> findDistributedTests() {
        final Set<DistributedTest> distributedTests = new HashSet<DistributedTest>();
        for (final StudentTestQuestion studentTestQuestion : getStudentTestsQuestionsSet()) {
            distributedTests.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTests;
    }

}
