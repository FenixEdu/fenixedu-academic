package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

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