package net.sourceforge.fenixedu.applicationTier.Servicos.commons.workLocation;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditWorkLocationTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public EditWorkLocationTest(String testName) {
        super(testName);

        this.dataSetFilePath = "etc/datasets/servicos/commons/workLocation/testEditWorkLocationDataSet.xml";

    }

    protected void setUp() {
        super.setUp();
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "EditWorkLocation";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException {

        Object args[] = { new Integer(21), "Some name" };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {
        Object args[] = { new Integer(21), "Some name" };

        return args;
    }

    public void testSuccessfullEdit() {
        try {

            Object[] args = { new Integer(21), "Some cool name" };

            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/commons/workLocation/testExpectedEditWorkLocationSucessfullEditingDataSet.xml");

            //ok
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSuccessfullEdit " + ex.getMessage());
        }

    }

    public void testChangeToExistingWorkLocation() {
        try {

            Object[] args = { new Integer(21), "INSTITUTO DE TELECOMUNICAÇÕES" };

            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            fail("testChangeToExistingWorkLocation did not throw ExistingServiceException");

        } catch (ExistingServiceException ex) {
            //ok
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testChangeToExistingWorkLocation " + ex.getMessage());
        }
    }

}