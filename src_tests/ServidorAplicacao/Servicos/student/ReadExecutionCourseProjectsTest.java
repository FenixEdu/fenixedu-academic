/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 * 
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteProjects;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionCourseProjectsTest extends TestCaseReadServices {

    public ReadExecutionCourseProjectsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExecutionCourseProjectsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionCourseProjects";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] result = { new Integer(31) };
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(25) };
        return result;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        InfoSiteProjects infoSiteProjects = new InfoSiteProjects();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IGroupProperties groupProperties1 = (IGroupProperties) sp.getIPersistentGroupProperties()
                    .readByOID(GroupProperties.class, new Integer(3));
            IGroupProperties groupProperties2 = (IGroupProperties) sp.getIPersistentGroupProperties()
                    .readByOID(GroupProperties.class, new Integer(5));
            List infoAllGroupProperties = new ArrayList();
            infoAllGroupProperties
                    .add(Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties1));
            infoAllGroupProperties
                    .add(Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties2));

            infoSiteProjects.setInfoGroupPropertiesList(infoAllGroupProperties);
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSiteProjects;
    }

}