package ServidorAplicacao.Servicos.publico;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoMark;
import DataBeans.InfoSiteMarks;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorAplicacao.Servicos.UtilsTestCase;

/**
 * @author Fernanda Quitério
 *  
 */
public class ReadPublishedMarksByExamTest extends ServiceTestCase {
    /**
     * @param testName
     */
    public ReadPublishedMarksByExamTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadPublishedMarksByExam";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/publico/testReadPublishedMarksByExamDataSet.xml";
    }

    public void testNonExistingEvaluation() {

        Object[] args = { new Integer(4), new Integer(1000) };
        try {

            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingEvaluation was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingEvaluation");

        } catch (FenixServiceException e) {
            System.out.println("testNonExistingEvaluation was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("testNonExistingEvaluation was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingEvaluation");
        }
    }

    public void testNonExistingSite() {

        Object[] args = { new Integer(1000), new Integer(5) };
        try {

            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingSite was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingSite");

        } catch (FenixServiceException e) {
            System.out.println("testNonExistingSite was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("testNonExistingSite was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingSite");
        }
    }

    public void testExistingSiteAndEvaluationWithoutMarks() {

        Object[] args = { new Integer(4), new Integer(6) };
        ExecutionCourseSiteView result = null;

        try {

            result = (ExecutionCourseSiteView) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            InfoSiteMarks infoSiteMarks = (InfoSiteMarks) result.getComponent();
            List marksList = infoSiteMarks.getMarksList();

            assertEquals(marksList.size(), 0);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/publico/testReadPublishedMarksByExamExpectedDataSet.xml");

            System.out
                    .println("testExistingSiteAndEvaluationWithoutMarks was SUCCESSFULY runned by class: "
                            + this.getClass().getName());

        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println("testExistingSiteAndEvaluationWithoutMarks was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testExistingSiteAndEvaluationWithoutMarks");
        }

    }

    public void testExistingSiteAndEvaluation() {

        Object[] args = { new Integer(4), new Integer(5) };
        ExecutionCourseSiteView result = null;

        try {

            result = (ExecutionCourseSiteView) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            InfoSiteMarks infoSiteMarks = (InfoSiteMarks) result.getComponent();
            List marksList = infoSiteMarks.getMarksList();

            assertEquals(marksList.size(), 6);

            Object[] values = { new Integer(1), new Integer(2), new Integer(3), new Integer(4),
                    new Integer(5), new Integer(6) };
            UtilsTestCase.readTestList(marksList, values, "idInternal", InfoMark.class);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/publico/testReadPublishedMarksByExamExpectedDataSet.xml");

            System.out
                    .println("testExistingSiteAndEvaluationWithoutMarks was SUCCESSFULY runned by class: "
                            + this.getClass().getName());

        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println("testExistingSiteAndEvaluationWithoutMarks was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testExistingSiteAndEvaluationWithoutMarks");
        }

    }
}