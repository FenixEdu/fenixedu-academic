/*
 * Created on 12/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionTest extends TestCaseReadServices
{

    /**
    * @param testName
    */
    public ReadQuestionTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadQuestion";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        //Object[] args = { new Integer(26), new Integer(2), new Integer(1)};
        Object[] args = { new Integer(26), new Integer(2), new Integer(-1)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteQuestion bodyComponent = new InfoSiteQuestion();
        InfoExecutionCourse infoExecutionCourse = null;
        InfoQuestion infoQuestion = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();
            IExecutionCourse executionCourse = new DisciplinaExecucao(new Integer(26));
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            assertNotNull("executionCourse null", executionCourse);

            IPersistentMetadata persistentMetadata = sp.getIPersistentMetadata();
            IMetadata metadata = new Metadata(new Integer(2));
            metadata = (IMetadata) persistentMetadata.readByOId(metadata, false);
            assertNotNull("Metadata null", metadata);

            IPersistentQuestion persistentQuestion = sp.getIPersistentQuestion();
            IQuestion question = new Question(new Integer(1));
            question = (IQuestion) persistentQuestion.readByOId(question, false);
            assertNotNull("Question null", question);

            sp.confirmarTransaccao();
            infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);

            InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
            ParseMetadata p = new ParseMetadata();
            try
            {
                infoMetadata = p.parseMetadata(metadata.getMetadataFile(), infoMetadata, "");
            } catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
            infoQuestion = Cloner.copyIQuestion2InfoQuestion(question);
            infoQuestion.setInfoMetadata(infoMetadata);
            ParseQuestion parse = new ParseQuestion();
            try
            {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, "");
            } catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
        } catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }
        bodyComponent.setInfoQuestion(infoQuestion);
        bodyComponent.setExecutionCourse(infoExecutionCourse);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
