/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTest implements IService {

    private String path = new String();

    public InfoTest run(Integer executionCourseId, Integer testId, String path) throws FenixServiceException, ExcepcaoPersistencia {
        this.path = path.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Test test = (Test) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }
        List<InfoTestQuestion> infoTestQuestions = new ArrayList<InfoTestQuestion>();
        ParseQuestion parse = new ParseQuestion();
        for (TestQuestion testQuestion : test.getTestQuestions()) {
            InfoTestQuestion infoTestQuestion = InfoTestQuestionWithInfoQuestion.newInfoFromDomain(testQuestion);
            try {
                infoTestQuestion.setQuestion(parse.parseQuestion(infoTestQuestion.getQuestion().getXmlFile(), infoTestQuestion.getQuestion(),
                        this.path));
                if (infoTestQuestion.getQuestion().getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                    infoTestQuestion.getQuestion().setResponseProcessingInstructions(
                            parse.newResponseList(infoTestQuestion.getQuestion().getResponseProcessingInstructions(), infoTestQuestion.getQuestion()
                                    .getOptions()));

            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            infoTestQuestions.add(infoTestQuestion);
        }
        InfoTest infoTest = InfoTest.newInfoFromDomain(test);
        infoTest.setInfoTestQuestions(infoTestQuestions);
        return infoTest;
    }

    public InfoTest run(Integer executionCourseId, Integer testId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Test test = (Test) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }
        return InfoTest.newInfoFromDomain(test);
    }

}