/*
 * Created on 27/Fev/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoMetadata;
import net.sourceforge.fenixedu.dataTransferObject.InfoMetadataWithVisibleQuestions;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExercise;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadExercise implements IService {
    private String path = new String();

    public ReadExercise() {
    }

    public SiteView run(Integer executionCourseId, Integer metadataId, Integer variationId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, metadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            InfoMetadata infoMetadata = InfoMetadataWithVisibleQuestions.newInfoFromDomain(metadata);
            List visibleInfoQuestions = new ArrayList();
            List questionNames = new ArrayList();
            if (metadata.getVisibleQuestions() != null) {
                Iterator it = metadata.getVisibleQuestions().iterator();
                while (it.hasNext()) {
                    IQuestion question = (IQuestion) it.next();
                    if (question.getIdInternal().equals(variationId) || variationId.intValue() == -2) {
                        InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                        ParseQuestion parse = new ParseQuestion();
                        try {
                            infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion,
                                    this.path);
                            if (infoQuestion.getQuestionType().getType().equals(
                                    new Integer(QuestionType.LID)))
                                infoQuestion.setResponseProcessingInstructions(parse.newResponseList(
                                        infoQuestion.getResponseProcessingInstructions(), infoQuestion
                                                .getOptions()));
                        } catch (Exception e) {
                            throw new FenixServiceException(e);
                        }
                        visibleInfoQuestions.add(infoQuestion);
                    }
                    questionNames.add(new LabelValueBean(question.getXmlFileName(), question
                            .getIdInternal().toString()));
                }
            }
            infoMetadata.setVisibleQuestions(visibleInfoQuestions);
            InfoSiteExercise infoSiteExercise = new InfoSiteExercise();
            infoSiteExercise.setInfoMetadata(infoMetadata);
            infoSiteExercise.setQuestionNames(questionNames);
            infoSiteExercise.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(infoSiteExercise, infoSiteExercise);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}