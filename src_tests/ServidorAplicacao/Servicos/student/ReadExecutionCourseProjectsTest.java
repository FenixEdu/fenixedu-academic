/*
 *
 * Created on 03/09/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.student;

/**
 * 
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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