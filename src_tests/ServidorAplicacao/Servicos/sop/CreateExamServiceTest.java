/*
 * CreateExamServiceTest.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import Util.Season;

public class CreateExamServiceTest extends TestCaseCreateServices {

    public CreateExamServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CreateExamServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "CreateExam";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Calendar examDateAndTime = Calendar.getInstance();
        Season season = new Season(Season.SEASON2);
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        examDateAndTime.set(Calendar.YEAR, 2003);
        examDateAndTime.set(Calendar.MONTH, Calendar.JULY);
        examDateAndTime.set(Calendar.DAY_OF_MONTH, 8);
        examDateAndTime.set(Calendar.HOUR_OF_DAY, 17);
        examDateAndTime.set(Calendar.MINUTE, 0);
        examDateAndTime.set(Calendar.SECOND, 0);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                infoExecutionYear);

        infoExecutionCourse.setSigla("APR");
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        Object args[] = { examDateAndTime, examDateAndTime, season, infoExecutionCourse };

        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Calendar examDateAndTime = Calendar.getInstance();
        Season season = new Season(Season.SEASON1);
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        examDateAndTime.set(Calendar.YEAR, 2003);
        examDateAndTime.set(Calendar.MONTH, Calendar.JULY);
        examDateAndTime.set(Calendar.DAY_OF_MONTH, 8);
        examDateAndTime.set(Calendar.HOUR_OF_DAY, 17);
        examDateAndTime.set(Calendar.MINUTE, 0);
        examDateAndTime.set(Calendar.SECOND, 0);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                infoExecutionYear);

        infoExecutionCourse.setSigla("APR");
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        Object args[] = { examDateAndTime, examDateAndTime, season, infoExecutionCourse };

        return args;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }
}