/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage extends FenixService {
	@Service
	public static String run(Integer registrationId, Integer distributedTestId, Integer questionId, Integer imageId,
			Integer feedbackId, Integer itemIndex, String path) throws FenixServiceException {
		final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
		final Registration registration = rootDomainObject.readRegistrationByOID(registrationId);
		return run(registration, distributedTest, questionId, imageId, feedbackId, itemIndex, path);
	}

	@Service
	public static String run(Registration registration, DistributedTest distributedTest, Integer questionId, Integer imageId,
			Integer feedbackId, Integer itemIndex, String path) throws FenixServiceException {
		final Question question = rootDomainObject.readQuestionByOID(questionId);
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