package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadReimbursementGuidesByGuideTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadReimbursementGuidesByGuideTest(String testName) {
        super(testName);
        if (testName.equals("testReadNonExistingReimbursementGuides")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testReadReimbursementGuidesByGuideNonExistingReimbursementGuidesDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testReadReimbursementGuidesByGuideDataSet.xml";
        }
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadReimbursementGuidesByGuide";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(1) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {
        Object[] args = { new Integer(1) };

        return args;
    }

    public void testSucessfull() {
        try {
            Integer guideId = new Integer(5762);

            Object args[] = { guideId };

            List infoReimbursementGuideList = (List) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), args);

            assertFalse(infoReimbursementGuideList.isEmpty());

            for (Iterator iter = infoReimbursementGuideList.iterator(); iter.hasNext();) {

                InfoReimbursementGuide infoReimbursementGuide = (InfoReimbursementGuide) iter.next();

                assertEquals(infoReimbursementGuide.getInfoGuide().getIdInternal(), guideId);
            }

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExistingReimbursementGuides() {
        try {
            Integer guideId = new Integer(5762);

            Object args[] = { guideId };

            List infoReimbursementGuideList = (List) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), args);

            assertTrue(infoReimbursementGuideList.isEmpty());

        } catch (FenixServiceException e) {
            fail("testReadNonExistingReimbursementGuides " + e.getMessage());
        }

    }

}