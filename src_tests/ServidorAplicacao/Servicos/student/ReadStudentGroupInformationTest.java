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
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadStudentGroupInformationTest extends TestCaseReadServices {

    public ReadStudentGroupInformationTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentGroupInformationTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentGroupInformation";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(8) };
        return result;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
                    StudentGroup.class, new Integer(8));

            InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
            infoSiteStudentInformation.setName("Nome da Pessoa");
            infoSiteStudentInformation.setEmail("s@h.c");
            infoSiteStudentInformation.setNumber(new Integer(42222));
            infoSiteStudentInformation.setUsername("13");

            List infoSiteStudentInformationList = new ArrayList();
            infoSiteStudentInformationList.add(infoSiteStudentInformation);
            infoSiteStudentGroup.setInfoSiteStudentInformationList(infoSiteStudentInformationList);
            infoSiteStudentGroup.setInfoStudentGroup(Cloner
                    .copyIStudentGroup2InfoStudentGroup(studentGroup));
            infoSiteStudentGroup.setNrOfElements(new Integer(3));
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSiteStudentGroup;

    }

}