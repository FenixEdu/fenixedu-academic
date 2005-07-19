/*
 * Created on 27/Fev/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadataWithVisibleQuestions;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteExercise;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadExercise implements IService {
    private String path = new String();

    public SiteView run(Integer executionCourseId, Integer metadataId, Integer variationId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, metadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            InfoMetadata infoMetadata = InfoMetadataWithVisibleQuestions.newInfoFromDomain(metadata);
            List<InfoQuestion> visibleInfoQuestions = new ArrayList<InfoQuestion>();
            List<LabelValueBean> questionNames = new ArrayList<LabelValueBean>();
            if (metadata.getVisibleQuestions() != null) {
                for (IQuestion question : metadata.getVisibleQuestions()) {
                    if (question.getIdInternal().equals(variationId) || variationId.intValue() == -2) {
                        InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                        ParseQuestion parse = new ParseQuestion();
                        try {
                            infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
                            if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                                infoQuestion.setResponseProcessingInstructions(parse.newResponseList(
                                        infoQuestion.getResponseProcessingInstructions(), infoQuestion.getOptions()));
                        } catch (Exception e) {
                            throw new FenixServiceException(e);
                        }
                        visibleInfoQuestions.add(infoQuestion);
                    }
                    questionNames.add(new LabelValueBean(question.getXmlFileName(), question.getIdInternal().toString()));
                }
            }
            infoMetadata.setVisibleQuestions(visibleInfoQuestions);
            InfoSiteExercise infoSiteExercise = new InfoSiteExercise();
            infoSiteExercise.setInfoMetadata(infoMetadata);
            infoSiteExercise.setQuestionNames(questionNames);
            infoSiteExercise.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(infoSiteExercise, infoSiteExercise);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}