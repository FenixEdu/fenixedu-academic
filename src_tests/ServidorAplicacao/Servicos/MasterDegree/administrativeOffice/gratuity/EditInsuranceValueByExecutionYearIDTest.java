package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditInsuranceValueByExecutionYearIDTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public EditInsuranceValueByExecutionYearIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testEditInsuranceValueByExecutionYearIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "EditInsuranceValueByExecutionYearID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(2), new Double(150), new Date() };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(2), new Double(150), new Date() };

        return args;
    }

    public void testEditExisting() {

        try {
            Integer executionYearID = new Integer(2);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2004);
            calendar.set(Calendar.MONTH, 9);
            calendar.set(Calendar.DAY_OF_MONTH, 10);

            Object[] args = { executionYearID, new Double(200),
                    calendar.getTime() };

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testExpectedEditInsuranceValueByExecutionYearIDEditExistingDataSet.xml");

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testEditNonExisting() {
        try {
            Integer executionYearID = new Integer(3);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2004);
            calendar.set(Calendar.MONTH, 9);
            calendar.set(Calendar.DAY_OF_MONTH, 10);

            Object[] args = { executionYearID, new Double(200),
                    calendar.getTime() };

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testExpectedEditInsuranceValueByExecutionYearIDEditNonExistingDataSet.xml");

        } catch (FenixServiceException e) {
            fail("testEditNonExisting " + e.getMessage());
        }

    }
}