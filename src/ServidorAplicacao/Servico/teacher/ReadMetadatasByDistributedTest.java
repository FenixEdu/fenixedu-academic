/*
 * Created on Oct 22, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadMetadatasByDistributedTest implements IServico
{

    private static ReadMetadatasByDistributedTest service = new ReadMetadatasByDistributedTest();
    private String path = new String();
    public static ReadMetadatasByDistributedTest getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadMetadatasByDistributedTest";
    }
    public SiteView run(Integer executionCourseId, Integer distributedTestId, String path)
        throws FenixServiceException
    {
        this.path = path.replace('\\', '/');
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSuport.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            if (executionCourse == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            List metadatas = persistentMetadata.readByExecutionCourse(executionCourse);
            List result = new ArrayList();
            IPersistentDistributedTest persistentDistributedTest =
                persistentSuport.getIPersistentDistributedTest();

            List questions = new ArrayList();

            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            distributedTest =
                (IDistributedTest) persistentDistributedTest.readByOId(distributedTest, false);

            if (distributedTest == null)
            {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentStudentTestQuestion persistentStudentTestQuestion =
                persistentSuport.getIPersistentStudentTestQuestion();
            List studentTestQuestionList =
                persistentStudentTestQuestion.readStudentTestQuestionsByDistributedTest(distributedTest);

            Iterator iter = metadatas.iterator();
            while (iter.hasNext())
            {
                IMetadata metadata = (IMetadata) iter.next();
                if (metadata.getVisibility().equals(new Boolean("true")))
                {

                    boolean contains = false;

                    Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
                    while (studentTestQuestionIt.hasNext())
                    {
                        IStudentTestQuestion studentTestQuestion =
                            (StudentTestQuestion) studentTestQuestionIt.next();
                        if (studentTestQuestion
                            .getQuestion()
                            .getKeyMetadata()
                            .equals(metadata.getIdInternal()))
                        {
                            contains = true;
                            break;
                        }
                    }
                    if (contains == false)
                    {
                        InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
                        ParseMetadata p = new ParseMetadata();
                        try
                        {
                            infoMetadata =
                                p.parseMetadata(metadata.getMetadataFile(), infoMetadata, this.path);
                        }
                        catch (Exception e)
                        {
                            throw new FenixServiceException(e);
                        }
                        result.add(infoMetadata);
                    }
                }
            }
            InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
            bodyComponent.setInfoMetadatas(result);
            bodyComponent.setExecutionCourse(
                (InfoExecutionCourse) Cloner.get(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}