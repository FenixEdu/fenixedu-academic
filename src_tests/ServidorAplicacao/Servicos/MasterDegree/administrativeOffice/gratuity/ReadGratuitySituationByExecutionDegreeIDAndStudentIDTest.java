package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadGratuitySituationByExecutionDegreeIDAndStudentIDTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadGratuitySituationByExecutionDegreeIDAndStudentIDTest(
            String testName) {
        super(testName);
        if (testName.equals("testReadNonExisting")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuitySituationByExecutionDegreeIDAndStudentIDNonExistingDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuitySituationByExecutionDegreeIDAndStudentIDDataSet.xml";
        }

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadGratuitySituationByExecutionDegreeIDAndStudentID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(8949), new Integer(2) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(8949), new Integer(2) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer executionDegreeID = new Integer(29);
            Integer studentID = new Integer(8403);

            Object[] args = { executionDegreeID, studentID };
            InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoGratuitySituation);
            assertEquals(infoGratuitySituation.getIdInternal(), new Integer(
                    8243));

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer executionDegreeID = new Integer(2);
            Integer studentID = new Integer(8403);

            Object[] args = { executionDegreeID, studentID };

            InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNull(infoGratuitySituation);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }
}