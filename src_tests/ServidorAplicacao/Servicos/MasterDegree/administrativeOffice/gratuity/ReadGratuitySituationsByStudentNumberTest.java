package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadGratuitySituationsByStudentNumberTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadGratuitySituationsByStudentNumberTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuitySituationsByStudentNumberDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadGratuitySituationsByStudentNumber";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(5344) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(5344) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer studentNumber = new Integer(5344);

            Object[] args = { studentNumber };
            List infoGratuitySituationList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoGratuitySituationList);
            assertTrue(infoGratuitySituationList.size() == 2);

            assertEquals(((InfoGratuitySituation) infoGratuitySituationList
                    .get(0)).getIdInternal(), new Integer(8008));
            assertEquals(((InfoGratuitySituation) infoGratuitySituationList
                    .get(1)).getIdInternal(), new Integer(8243));

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer studentNumber = new Integer(5500);

            Object[] args = { studentNumber };
            List infoGratuitySituationList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoGratuitySituationList);
            assertTrue(infoGratuitySituationList.isEmpty());


        } catch (FenixServiceException e) {
            fail("testReadNonExisting " + e.getMessage());
        }

    }
}