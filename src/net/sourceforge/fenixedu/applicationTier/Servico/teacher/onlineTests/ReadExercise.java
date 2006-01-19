/*
 * Created on 27/Fev/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadataWithExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class ReadExercise implements IService {
    public InfoMetadata run(Integer executionCourseId, Integer metadataId, Integer variationId, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Metadata metadata = (Metadata) persistentSuport.getIPersistentMetadata().readByOID(Metadata.class, metadataId);
        if (metadata == null || !metadata.getVisibility().booleanValue()) {
            return null;
        }
        InfoMetadata infoMetadata = InfoMetadataWithExecutionCourse.newInfoFromDomain(metadata);
        List<InfoQuestion> visibleInfoQuestions = new ArrayList<InfoQuestion>();
        for (int i = 0; i < metadata.getVisibleQuestions().size(); i++) {
            Question question = metadata.getVisibleQuestions().get(i);
            InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
            if (question.getIdInternal().equals(variationId) || variationId.intValue() == -2) {
                ParseQuestion parse = new ParseQuestion();
                try {
                    infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, path.replace('\\', '/'));
                    if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                        infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion.getResponseProcessingInstructions(),
                                infoQuestion.getOptions()));
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
            }
            visibleInfoQuestions.add(infoQuestion);
        }
        infoMetadata.setVisibleQuestions(visibleInfoQuestions);
        return infoMetadata;
    }
}