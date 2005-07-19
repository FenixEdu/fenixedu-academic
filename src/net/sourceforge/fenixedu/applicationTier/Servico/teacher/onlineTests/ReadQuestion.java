/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestionWithInfoMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadQuestion implements IService {

    private String path = new String();

    public InfoQuestion run(Integer executionCourseId, Integer metadataId, Integer questionId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IQuestion question = null;
            if (questionId == null || questionId.equals(new Integer(-1))) {
                if (metadataId == null)
                    throw new InvalidArgumentsServiceException();
                IMetadata metadata = (IMetadata) persistentSuport.getIPersistentMetadata().readByOID(Metadata.class, metadataId);
                if (metadata == null) {
                    throw new InvalidArgumentsServiceException();
                }
                if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0) {
                    question = metadata.getVisibleQuestions().get(0);
                } else {
                    throw new InvalidArgumentsServiceException();
                }
            } else {
                question = (IQuestion) persistentSuport.getIPersistentQuestion().readByOID(Question.class, questionId);
            }
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            InfoQuestion infoQuestion = InfoQuestionWithInfoMetadata.newInfoFromDomain(question);
            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
                if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                    infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion.getResponseProcessingInstructions(),
                            infoQuestion.getOptions()));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            return infoQuestion;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}