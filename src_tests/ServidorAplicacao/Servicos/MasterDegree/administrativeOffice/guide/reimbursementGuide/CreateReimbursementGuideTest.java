package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.RequiredJustificationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class CreateReimbursementGuideTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public CreateReimbursementGuideTest(String testName) {
        super(testName);
        if (testName.equals("testCreateWithGuideNotPayed")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testCreateReimbursementGuideWithGuideNotPayedDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testCreateReimbursementGuideDataSet.xml";
        }

    }

    protected String getNameOfServiceToBeTested() {
        return "CreateReimbursementGuide";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(1), "Some remarks", new ArrayList(), null };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {
        Object[] args = { new Integer(1), "Some remarks", new ArrayList(), userViewNotAuthorized };

        return args;
    }

    public void testSucessfull() {
        Integer guideId = new Integer(5762);
        InfoGuide infoGuide = null;
        try {
            Object argsReadGuide[] = { guideId };
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "ReadGuide",
                    argsReadGuide);

            List infoReimbursementGuideEntries = new ArrayList();

            InfoReimbursementGuideEntry infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
            infoReimbursementGuideEntry.setInfoGuideEntry((InfoGuideEntry) infoGuide
                    .getInfoGuideEntries().get(0));
            infoReimbursementGuideEntry.setJustification("alguma");
            infoReimbursementGuideEntry.setValue(new Double(500.0));

            infoReimbursementGuideEntries.add(infoReimbursementGuideEntry);

            Object createArgs[] = { guideId, "", infoReimbursementGuideEntries, userView };

            Integer reimbursementGuideId = (Integer) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), createArgs);

            assertEquals(reimbursementGuideId, new Integer(1));

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testExpectedCreateReimbursementGuideSucessfullDataSet.xml");

            //ok

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testCreateWithGuideNotPayed() {
        Integer guideId = new Integer(5762);
        InfoGuide infoGuide = null;
        try {
            Object argsReadGuide[] = { guideId };
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "ReadGuide",
                    argsReadGuide);

            List infoReimbursementGuideEntries = new ArrayList();

            InfoReimbursementGuideEntry infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
            infoReimbursementGuideEntry.setInfoGuideEntry((InfoGuideEntry) infoGuide
                    .getInfoGuideEntries().get(0));
            infoReimbursementGuideEntry.setJustification("alguma");
            infoReimbursementGuideEntry.setValue(new Double(500.0));

            infoReimbursementGuideEntries.add(infoReimbursementGuideEntry);

            Object createArgs[] = { guideId, "", infoReimbursementGuideEntries, userView };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    createArgs);

            //error
            fail("testCreateWithGuideNotPayed did not throw InvalidGuideSituationServiceException.");

        } catch (InvalidGuideSituationServiceException e) {
            //ok
        } catch (FenixServiceException e) {
            fail("testCreateWithGuideNotPayed " + e.getMessage());
        }

    }

    public void testCreateWithReimburseValueBiggerThanGuideValue() {
        Integer guideId = new Integer(5762);
        InfoGuide infoGuide = null;
        try {
            Object argsReadGuide[] = { guideId };
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "ReadGuide",
                    argsReadGuide);

            List infoReimbursementGuideEntries = new ArrayList();

            InfoReimbursementGuideEntry infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
            infoReimbursementGuideEntry.setInfoGuideEntry((InfoGuideEntry) infoGuide
                    .getInfoGuideEntries().get(0));
            infoReimbursementGuideEntry.setJustification("alguma");
            infoReimbursementGuideEntry.setValue(new Double(5000.0));

            infoReimbursementGuideEntries.add(infoReimbursementGuideEntry);

            Object createArgs[] = { guideId, "", infoReimbursementGuideEntries, userView };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    createArgs);

            //error
            fail("testCreateWithReimburseValueBiggerThanGuideValue did not throw InvalidReimbursementValueServiceException.");

        } catch (InvalidReimbursementValueServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testCreateWithReimburseValueBiggerThanGuideValue " + e.getMessage());
        }

    }

    public void testCreateWithoutJustification() {
        Integer guideId = new Integer(5762);
        InfoGuide infoGuide = null;
        try {
            Object argsReadGuide[] = { guideId };
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "ReadGuide",
                    argsReadGuide);

            List infoReimbursementGuideEntries = new ArrayList();

            InfoReimbursementGuideEntry infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
            infoReimbursementGuideEntry.setInfoGuideEntry((InfoGuideEntry) infoGuide
                    .getInfoGuideEntries().get(0));
            infoReimbursementGuideEntry.setJustification("");
            infoReimbursementGuideEntry.setValue(new Double(5000.0));

            infoReimbursementGuideEntries.add(infoReimbursementGuideEntry);

            Object createArgs[] = { guideId, "", infoReimbursementGuideEntries, userView };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    createArgs);

            //error
            fail("testCreateWithoutJustification did not throw RequiredJustificationServiceException.");

        } catch (RequiredJustificationServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testCreateWithoutJustification " + e.getMessage());
        }

    }

}