/*
 * Created on 27/Fev/2004
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteExercise;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadExercise implements IService
{
    private String path = new String();

    public ReadExercise() {
    }

    public SiteView run(Integer executionCourseId, Integer metadataId, Integer variationId, String path)
            throws FenixServiceException
    {
        this.path = path.replace('\\', '/');
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse,
                    false);
            if (executionCourse == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = new Metadata(metadataId);
            metadata = (IMetadata) persistentMetadata.readByOId(metadata, false);
            if (metadata == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
            List visibleInfoQuestions = new ArrayList();
            List questionNames = new ArrayList();
            if (metadata.getVisibleQuestions() != null)
            {
                Iterator it = metadata.getVisibleQuestions().iterator();
                while (it.hasNext())
                {
                    IQuestion question = (IQuestion) it.next();
                    if (question.getIdInternal().equals(variationId) || variationId.intValue() == -2)
                    {
                        InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion(question);
                        ParseQuestion parse = new ParseQuestion();
                        try
                        {
                            infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion,
                                    this.path);
                            if (infoQuestion.getQuestionType().getType().equals(
                                    new Integer(QuestionType.LID)))
                                infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion
                                        .getResponseProcessingInstructions(), infoQuestion.getOptions()));
                        }
                        catch (Exception e)
                        {
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
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}