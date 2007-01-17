/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage extends Service {
    public String run(Integer registrationId, Integer distributedTestId, Integer questionId,
            Integer imageId, String feedbackId, Integer itemIndex, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        final Registration registration = rootDomainObject.readRegistrationByOID(registrationId);
        return run(registration, distributedTest, questionId, imageId, feedbackId, itemIndex, path);
    }

    public String run(Registration otherRegistration, DistributedTest distributedTest, Integer questionId,
            Integer imageId, String feedbackId, Integer itemIndex, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
	for (final Registration registration : otherRegistration.getStudent().getRegistrationsSet()) {
	    for (StudentTestQuestion studentTestQuestion : registration.getStudentTestsQuestions()) {
		if (studentTestQuestion.getKeyDistributedTest().equals(distributedTest.getIdInternal())
			&& studentTestQuestion.getKeyQuestion().equals(questionId)) {
		    ParseSubQuestion parse = new ParseSubQuestion();
		    try {
			parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
		    } catch (Exception e) {
			throw new FenixServiceException(e);
		    }
		    return studentTestQuestion.getStudentSubQuestions().get(itemIndex).getImage(imageId);
		}
	    }
	}
        return null;
    }

}