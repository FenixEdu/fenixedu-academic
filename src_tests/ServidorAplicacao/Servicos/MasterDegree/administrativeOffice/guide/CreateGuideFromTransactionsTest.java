package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoContributor;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.GuideRequester;
import Util.SituationOfGuide;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class CreateGuideFromTransactionsTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public CreateGuideFromTransactionsTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/testCreateGuideFromTransactionsDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "CreateGuideFromTransactions";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new InfoGuide(), "Some remarks",
                SituationOfGuide.PAYED_TYPE, new ArrayList() };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new InfoGuide(), "Some remarks",
                SituationOfGuide.PAYED_TYPE, new ArrayList() };

        return args;
    }

    public void testSucessfull() {
        try {

            InfoGuide infoGuide = new InfoGuide();
            infoGuide.setCreationDate(Calendar.getInstance().getTime());
            infoGuide.setGuideRequester(new GuideRequester(
                    GuideRequester.STUDENT));

            Object[] argsReadContributor = { new Integer(181265168) };
            InfoContributor infoContributor = (InfoContributor) ServiceManagerServiceFactory
                    .executeService(userView, "ReadContributor",
                            argsReadContributor);

            infoGuide.setInfoContributor(infoContributor);

            Object[] argsReadExecutionDegree = { new Integer(185) };
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory
                    .executeService(userView, "ReadExecutionDegreeByOID",
                            argsReadExecutionDegree);

            infoGuide.setInfoExecutionDegree(infoExecutionDegree);

            Object[] argsReadStudent = { new Integer(8403) };
            InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory
                    .executeService(userView, "student.ReadStudentById",
                            argsReadStudent);

            infoGuide.setInfoPerson(infoStudent.getInfoPerson());
            infoGuide.setVersion(new Integer(1));
            infoGuide.setYear(new Integer(Calendar.getInstance().get(
                    Calendar.YEAR)));

            List transactionIDs = new ArrayList();
            transactionIDs.add(new Integer(881));

            Object[] argsCreateGuide = { infoGuide, "Some remarks",
                    SituationOfGuide.PAYED_TYPE, transactionIDs };

            ServiceManagerServiceFactory
            .executeService(userView, "CreateGuideFromTransactions",
                    argsCreateGuide);
            
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/guide/testExpectedCreateGuideFromTransactionsSucessfullDataSet.xml");

            //ok

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

}