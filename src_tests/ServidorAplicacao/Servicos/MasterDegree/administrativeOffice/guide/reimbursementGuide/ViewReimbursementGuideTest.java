package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ViewReimbursementGuideTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ViewReimbursementGuideTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testViewReimbursementGuideDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ViewReimbursementGuide";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(1)};

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(1)};

        return args;
    }

    public void testSucessfull() {
        try {
            Integer reibursementGuideId = new Integer(1);

            Object args[] = { reibursementGuideId};

            InfoReimbursementGuide infoReimbursementGuide = (InfoReimbursementGuide) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoReimbursementGuide);
            assertEquals(infoReimbursementGuide.getIdInternal(),
                    reibursementGuideId);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testViewNonExistingReimbursementGuide() {
        try {
            Integer guideId = new Integer(5762);
            Object args[] = { guideId};

           ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            fail("testViewNonExistingReimbursementGuide did not throw NonExistingServiceException");

        } catch (NonExistingServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testViewNonExistingReimbursementGuide " + e.getMessage());
        }

    }

}