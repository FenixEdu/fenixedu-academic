/*
 * EditExamServiceTest.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import Util.Season;

public class EditExamServiceTest extends TestCaseCreateServices {

    public EditExamServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditExamServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "EditExam";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Calendar examDateAndTime = Calendar.getInstance();
        Season season = new Season(Season.SEASON1);
        InfoViewExamByDayAndShift infoViewOldExam = new InfoViewExamByDayAndShift();

        examDateAndTime.set(Calendar.YEAR, 2003);
        examDateAndTime.set(Calendar.MONTH, Calendar.JULY);
        examDateAndTime.set(Calendar.DAY_OF_MONTH, 8);
        examDateAndTime.set(Calendar.HOUR_OF_DAY, 17);
        examDateAndTime.set(Calendar.MINUTE, 0);
        examDateAndTime.set(Calendar.SECOND, 0);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(2));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setSigla("RCI");
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        List infoExecutionCourses = new ArrayList();
        infoExecutionCourses.add(infoExecutionCourse);

        InfoExam infoExam = new InfoExam();
        infoExam.setSeason(new Season(Season.SEASON1));

        infoViewOldExam.setInfoExecutionCourses(infoExecutionCourses);
        infoViewOldExam.setInfoExam(infoExam);

        Object args[] = { examDateAndTime, examDateAndTime, season, infoViewOldExam };

        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Calendar examDateAndTime = Calendar.getInstance();
        Season season = new Season(Season.SEASON2);
        InfoViewExamByDayAndShift infoViewOldExam = new InfoViewExamByDayAndShift();

        examDateAndTime.set(Calendar.YEAR, 2003);
        examDateAndTime.set(Calendar.MONTH, Calendar.JULY);
        examDateAndTime.set(Calendar.DAY_OF_MONTH, 8);
        examDateAndTime.set(Calendar.HOUR_OF_DAY, 17);
        examDateAndTime.set(Calendar.MINUTE, 0);
        examDateAndTime.set(Calendar.SECOND, 0);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(2));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setSigla("RCI");
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        List infoExecutionCourses = new ArrayList();
        infoExecutionCourses.add(infoExecutionCourse);

        InfoExam infoExam = new InfoExam();
        infoExam.setSeason(new Season(Season.SEASON1));

        infoViewOldExam.setInfoExecutionCourses(infoExecutionCourses);
        infoViewOldExam.setInfoExam(infoExam);

        Object args[] = { examDateAndTime, examDateAndTime, season, infoViewOldExam };

        return args;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }
}