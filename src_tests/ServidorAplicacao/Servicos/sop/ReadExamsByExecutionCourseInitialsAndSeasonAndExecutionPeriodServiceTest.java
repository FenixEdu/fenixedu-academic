/*
 * ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseRequeiersAuthorizationServices;
import Util.Season;

public class ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest extends
        TestCaseRequeiersAuthorizationServices {

    public ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest(
            java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriodServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod";
    }

    public void testReadValidResult() {
        String executionCourseInitials = "RCI";
        Season season = new Season(Season.SEASON1);
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                infoExecutionYear);

        args = new Object[3];
        args[0] = executionCourseInitials;
        args[1] = season;
        args[2] = infoExecutionPeriod;

        try {
            callServiceWithAuthorizedUserView();
        } catch (FenixServiceException e) {
            fail("Unexpected exception: " + e);
        }

        InfoViewExamByDayAndShift infoViewExamByDayAndShift = new InfoViewExamByDayAndShift();

        InfoExam infoExam = new InfoExam();
        infoExam.setSeason(new Season(Season.SEASON1));

        infoViewExamByDayAndShift.setInfoExam(infoExam);

        assertEquals("Result was unexpected!", infoViewExamByDayAndShift, result);
    }

}