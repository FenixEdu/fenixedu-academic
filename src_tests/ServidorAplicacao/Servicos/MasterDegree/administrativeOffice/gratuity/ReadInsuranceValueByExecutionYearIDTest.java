package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadInsuranceValueByExecutionYearIDTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadInsuranceValueByExecutionYearIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadInsuranceValueByExecutionYearIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadInsuranceValueByExecutionYearID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(2) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(2) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer executionYearID = new Integer(2);

            Object[] args = { executionYearID };
            InfoInsuranceValue infoInsuranceValue = (InfoInsuranceValue) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoInsuranceValue);
            assertEquals(infoInsuranceValue.getIdInternal(), new Integer(83));

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer executionYearID = new Integer(3);

            Object[] args = { executionYearID };
            InfoInsuranceValue infoInsuranceValue = (InfoInsuranceValue) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNull(infoInsuranceValue);

        } catch (FenixServiceException e) {
            fail("testReadNonExisting " + e.getMessage());
        }

    }
}