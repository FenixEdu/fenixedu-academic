package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import DataBeans.InfoGratuitySituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadGratuitySituationByIDTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadGratuitySituationByIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuitySituationByIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadGratuitySituationById";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(5344) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(8243) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer gratuitySituationID = new Integer(8243);

            Object[] args = { gratuitySituationID };
            InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoGratuitySituation);
            assertEquals(infoGratuitySituation.getIdInternal(),
                    gratuitySituationID);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer gratuitySituationID = new Integer(5500);

            Object[] args = { gratuitySituationID };
             ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            //error
            fail("testReadNonExisting did not throw NonExistingServiceException");

        } catch (NonExistingServiceException e) {
            // ok
        } catch (FenixServiceException e) {
            fail("testReadNonExisting " + e.getMessage());
        }

    }
}