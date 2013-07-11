/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage {
    @Atomic
    public static String run(String registrationId, String distributedTestId, String questionId, Integer imageId,
            Integer feedbackId, Integer itemIndex, String path) throws FenixServiceException {
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        final Registration registration = FenixFramework.getDomainObject(registrationId);
        return run(registration, distributedTest, questionId, imageId, feedbackId, itemIndex, path);
    }

    @Atomic
    public static String run(Registration registration, DistributedTest distributedTest, String questionId, Integer imageId,
            Integer feedbackId, Integer itemIndex, String path) throws FenixServiceException {
        final Question question = FenixFramework.getDomainObject(questionId);
        for (StudentTestQuestion studentTestQuestion : registration.getStudentTestsQuestions()) {
            if (studentTestQuestion.getDistributedTest() == distributedTest && studentTestQuestion.getQuestion() == question) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                return studentTestQuestion.getStudentSubQuestions().get(itemIndex).getImage(imageId, feedbackId);
            }
        }
        return null;
    }

}