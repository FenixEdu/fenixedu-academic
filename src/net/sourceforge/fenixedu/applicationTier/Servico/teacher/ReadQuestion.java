/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestionWithInfoMetadata;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteQuestion;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
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

    public ReadQuestion() {
    }

    public SiteView run(Integer executionCourseId, Integer metadataId, Integer questionId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion question = null;
            if (questionId == null || questionId.equals(new Integer(-1))) {
                if (metadataId == null)
                    throw new InvalidArgumentsServiceException();
                IMetadata metadata = (IMetadata) persistentSuport.getIPersistentMetadata().readByOID(
                        Metadata.class, metadataId);
                if (metadata == null)
                    throw new InvalidArgumentsServiceException();
                if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0)
                    question = (IQuestion) metadata.getVisibleQuestions().get(0);
                else
                    throw new InvalidArgumentsServiceException();
            } else {
                question = (IQuestion) persistentQuestion.readByOID(Question.class, questionId);
            }
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            InfoQuestion infoQuestion = InfoQuestionWithInfoMetadata.newInfoFromDomain(question);
            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
                if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                    infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion
                            .getResponseProcessingInstructions(), infoQuestion.getOptions()));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            InfoSiteQuestion bodyComponent = new InfoSiteQuestion();
            bodyComponent.setInfoQuestion(infoQuestion);
            bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}