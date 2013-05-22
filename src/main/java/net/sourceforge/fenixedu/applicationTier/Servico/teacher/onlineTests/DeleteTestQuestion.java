package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

public class DeleteTestQuestion extends FenixService {

    protected void run(Integer executionCourseId, Integer testId, final Integer questionId)
            throws InvalidArgumentsServiceException {
        Test test = rootDomainObject.readTestByOID(testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }

        TestQuestion testQuestion = (TestQuestion) CollectionUtils.find(test.getTestQuestions(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final TestQuestion testQuestion = (TestQuestion) arg0;
                return testQuestion.getQuestion().getIdInternal().equals(questionId);
            }
        });
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }

        test.deleteTestQuestion(testQuestion);
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTestQuestion serviceInstance = new DeleteTestQuestion();

    @Service
    public static void runDeleteTestQuestion(Integer executionCourseId, Integer testId, Integer questionId)
            throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, questionId);
    }

}