package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.Date;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.ReimbursementGuideState;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditReimbursementGuideTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public EditReimbursementGuideTest(String testName) {
        super(testName);
        if (testName.equals("")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testEditReimbursementGuideWhenActualStateIsEqualToPayedDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testEditReimbursementGuideDataSet.xml";
        }
    }

    protected String getNameOfServiceToBeTested() {
        return "EditReimbursementGuide";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(1), ReimbursementGuideState.PAYED,
                Calendar.getInstance().getTime(), "Some Remarks", null};

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(1), ReimbursementGuideState.PAYED,
                Calendar.getInstance().getTime(), "Some Remarks",
                userViewNotAuthorized};

        return args;
    }

    public void testSucessfullWhenNewStateIsNotEqualToPayed() {

        try {

            Integer reimbursementGuideId = new Integer(1);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "approved", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testExpectedEditReimbursementGuideSucessfullWhenNewStateIsNotEqualToPayedDataSet.xml");

            //ok

        } catch (FenixServiceException e) {
            fail("testSucessfullWhenNewStateIsNotEqualToPayed "
                    + e.getMessage());
        }

    }

  /*  public void testEditNonExistingReimbursementGuide() {
        try {
            Integer reimbursementGuideId = new Integer(150);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "issued", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            //error
            fail("testEditNonExistingReimbursementGuide did not throw NonExistingServiceException.");

        } catch (NonExistingServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testEditNonExistingReimbursementGuide " + e.getMessage());
        }

    }
*/
    public void testSucessfullWhenNewStateIsEqualToPayed() {

        try {

            Integer reimbursementGuideId = new Integer(1);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "payed", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testExpectedEditReimbursementGuideSucessfullWhenNewStateIsEqualToPayedDataSet.xml");
            //ok

        } catch (FenixServiceException e) {
            fail("testSucessfullWhenNewStateIsEqualToPayed " + e.getMessage());
        }

    }

    public void testEditWithoutChangingSituation() {
        try {
            Integer reimbursementGuideId = new Integer(1);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "issued", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            //error
            fail("testEditWithoutChangingSituation did not throw InvalidGuideSituationServiceException.");

        } catch (InvalidGuideSituationServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testEditWithoutChangingSituation " + e.getMessage());
        }

    }
    
    public void testEditWhenActualStateIsEqualToPayed() {
        try {
            Integer reimbursementGuideId = new Integer(1);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "issued", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            //error
            fail("testEditWhenActualStateIsEqualToPayed did not throw InvalidGuideSituationServiceException.");

        } catch (InvalidGuideSituationServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testEditWhenActualStateIsEqualToPayed " + e.getMessage());
        }

    }
    
    public void testEditWhenActualStateIsEqualToAnulled() {
        try {
            Integer reimbursementGuideId = new Integer(1);
            Date date = Calendar.getInstance().getTime();

            Object createArgs[] = { reimbursementGuideId, "issued", date, "",
                    userView};

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), createArgs);

            //error
            fail("testEditWhenActualStateIsEqualToAnulled did not throw InvalidGuideSituationServiceException.");

        } catch (InvalidGuideSituationServiceException e) {
            //ok

        } catch (FenixServiceException e) {
            fail("testEditWhenActualStateIsEqualToAnulled " + e.getMessage());
        }

    }

}