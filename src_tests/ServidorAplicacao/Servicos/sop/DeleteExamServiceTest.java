/*
 * DeleteExamServiceTest.java
 *
 * Created on 2003/03/28
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;
import net.sourceforge.fenixedu.util.Season;

public class DeleteExamServiceTest extends TestCaseDeleteAndEditServices {

    public DeleteExamServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DeleteExamServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteExam";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        //		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(
        //			"Engenharia da Programação",
        //			"EP",
        //			"blob",
        //			new Double(1),
        //			new Double(0),
        //			new Double(0),
        //			new Double(0),
        //			new InfoExecutionPeriod("2º semestre",new
        // InfoExecutionYear("2002/2003")));

        Calendar beginning = Calendar.getInstance();
        beginning.set(Calendar.YEAR, 2003);
        beginning.set(Calendar.MONTH, Calendar.JUNE);
        beginning.set(Calendar.DAY_OF_MONTH, 27);
        beginning.set(Calendar.HOUR_OF_DAY, 9);
        beginning.set(Calendar.MINUTE, 0);
        beginning.set(Calendar.SECOND, 0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2003);
        end.set(Calendar.MONTH, Calendar.JUNE);
        end.set(Calendar.DAY_OF_MONTH, 27);
        end.set(Calendar.HOUR_OF_DAY, 12);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        //		Season season = new Season(Season.SEASON1);

        //InfoExam infoExam = new InfoExam(beginning, end, null, season);
        InfoExam infoExam = new InfoExam();
        infoExam.setIdInternal(new Integer(5));
        //List infoExecutionCourses = new ArrayList();
        //infoExecutionCourses.add(infoExecutionCourse);

        InfoViewExamByDayAndShift infoViewExam = new InfoViewExamByDayAndShift();
        infoViewExam.setInfoExam(infoExam);
        //infoViewExam.setInfoExecutionCourses(infoExecutionCourses);

        Object argsDeleteExam[] = new Object[1];
        argsDeleteExam[0] = infoViewExam;

        return argsDeleteExam;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        //		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(
        //			"Unexisting Course",
        //			"UC",
        //			"blob",
        //			new Double(1),
        //			new Double(0),
        //			new Double(0),
        //			new Double(0),
        //			new InfoExecutionPeriod("2º semestre",new
        // InfoExecutionYear("2002/2003")));

        Calendar beginning = Calendar.getInstance();
        beginning.set(Calendar.YEAR, 2003);
        beginning.set(Calendar.MONTH, Calendar.MARCH);
        beginning.set(Calendar.DAY_OF_MONTH, 19);
        beginning.set(Calendar.HOUR_OF_DAY, 9);
        beginning.set(Calendar.MINUTE, 0);
        beginning.set(Calendar.SECOND, 0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2003);
        end.set(Calendar.MONTH, Calendar.MARCH);
        end.set(Calendar.DAY_OF_MONTH, 19);
        end.set(Calendar.HOUR_OF_DAY, 12);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        Season season = new Season(Season.SEASON1);

        Object argsDeleteExam[] = new Object[1];
        argsDeleteExam[0] = new InfoExam(beginning, beginning, null, season);

        return argsDeleteExam;
    }
}