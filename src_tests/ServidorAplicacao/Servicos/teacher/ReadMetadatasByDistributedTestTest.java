/*
 * Created on Oct 28, 2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadMetadatasByDistributedTestTest extends TestCaseReadServices
{

    public ReadMetadatasByDistributedTestTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadMetadatasByDistributedTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26), new Integer(3)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
        InfoExecutionCourse infoExecutionCourse = null;
        List infoMetadatas = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentMetadata persistentMetadata = sp.getIPersistentMetadata();
            IExecutionCourse executionCourse = new ExecutionCourse(new Integer(26));

            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            assertNotNull("executionCourse null", executionCourse);

            IMetadata metadata2 = new Metadata(new Integer(2));
            metadata2 = (IMetadata) persistentMetadata.readByOId(metadata2, false);
            assertNotNull("metadata null", metadata2);
            IMetadata metadata3 = new Metadata(new Integer(3));
            metadata3 = (IMetadata) persistentMetadata.readByOId(metadata3, false);
            assertNotNull("metadata null", metadata3);
            IMetadata metadata4 = new Metadata(new Integer(4));
            metadata4 = (IMetadata) persistentMetadata.readByOId(metadata4, false);
            assertNotNull("metadata null", metadata4);
            IMetadata metadata6 = new Metadata(new Integer(6));
            metadata6 = (IMetadata) persistentMetadata.readByOId(metadata6, false);
            assertNotNull("metadata null", metadata6);

            sp.confirmarTransaccao();
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            InfoMetadata infoMetadata2 = Cloner.copyIMetadata2InfoMetadata(metadata2);
            InfoMetadata infoMetadata3 = Cloner.copyIMetadata2InfoMetadata(metadata3);
            InfoMetadata infoMetadata4 = Cloner.copyIMetadata2InfoMetadata(metadata4);
            InfoMetadata infoMetadata6 = Cloner.copyIMetadata2InfoMetadata(metadata6);

            ParseMetadata p = new ParseMetadata();
            try
            {
                infoMetadata2 = p.parseMetadata(metadata2.getMetadataFile(), infoMetadata2, "");
                infoMetadata3 = p.parseMetadata(metadata3.getMetadataFile(), infoMetadata3, "");
                infoMetadata4 = p.parseMetadata(metadata4.getMetadataFile(), infoMetadata4, "");
                infoMetadata6 = p.parseMetadata(metadata6.getMetadataFile(), infoMetadata6, "");
            }
            catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
            infoMetadatas.add(infoMetadata2);
            infoMetadatas.add(infoMetadata3);
            infoMetadatas.add(infoMetadata4);
            infoMetadatas.add(infoMetadata6);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }

        bodyComponent.setExecutionCourse(infoExecutionCourse);
        bodyComponent.setInfoMetadatas(infoMetadatas);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;

    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
