/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class Question extends Question_Base {

    private List<SubQuestion> subQuestions;

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

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<SubQuestion> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public void addSubQuestion(SubQuestion subQuestion) {
        if (subQuestions == null) {
            subQuestions = new ArrayList<SubQuestion>();
        }
        subQuestions.add(subQuestion);
    }

//    public SubQuestion getSubQuestionByItem(String itemId) {
//        for (SubQuestion subQuestion : getSubQuestions()) {
//            if (itemId != null && subQuestion.getItemId() != null) {
//                if (itemId.equals(subQuestion.getItemId())) {
//                    return subQuestion;
//                }
//            } else if (itemId == null && subQuestion.getItemId() == null) {
//                return subQuestion;
//            }
//        }
//        return null;
//    }

}
