/*
 * ReadExamsByDayAndBeginningServiceTest.java
 *
 * Created on 2003/03/29
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourseAndExams;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseRequeiersAuthorizationServices;
import Util.Season;
import Util.TipoCurso;

public class ReadExamsByExecutionDegreeAndCurricularYearServiceTest extends
        TestCaseRequeiersAuthorizationServices {

    public ReadExamsByExecutionDegreeAndCurricularYearServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExamsByExecutionDegreeAndCurricularYearServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamsByExecutionDegreeAndCurricularYear";
    }

    public void testReadValidResult() {

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");
        infoDegree.setTipoCurso(new TipoCurso(TipoCurso.LICENCIATURA_STRING));
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        infoDegreeCurricularPlan.setDegreeDuration(new Integer(5));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan,
                infoExecutionYear);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                new InfoExecutionYear("2002/2003"));
        infoExecutionPeriod.setSemester(new Integer(2));
        Integer curricularYear = new Integer(1);

        args = new Object[3];
        args[0] = infoExecutionDegree;
        args[1] = infoExecutionPeriod;
        args[2] = curricularYear;

        try {
            callServiceWithAuthorizedUserView();
        } catch (FenixServiceException e) {
            fail("Unexpected exception: " + e);
        }

        assertNotNull("Result of call to service was null!", result);
        assertEquals("Type of result was unexpected!", ArrayList.class.getName(), result.getClass()
                .getName());
        List resultList = (List) result;
        assertEquals("Result size was unexpected!", 1, resultList.size());
        assertEquals("Type of result was unexpected!", InfoExecutionCourseAndExams.class.getName(),
                resultList.get(0).getClass().getName());
        InfoExecutionCourseAndExams infoExecutionCourseAndExams = (InfoExecutionCourseAndExams) resultList
                .get(0);
        assertNotNull("First season exam of execution course was null!", infoExecutionCourseAndExams
                .getInfoExam1());
        assertEquals("First season exam of execution course is inconsistent!",
                new Season(Season.SEASON1), infoExecutionCourseAndExams.getInfoExam1().getSeason());
        assertNotNull("Second season exam of execution course was null!", infoExecutionCourseAndExams
                .getInfoExam2());
        assertEquals("Second season exam of execution course is inconsistent!", new Season(
                Season.SEASON2), infoExecutionCourseAndExams.getInfoExam2().getSeason());
        assertEquals("Unexpected number of students attending course!", new Integer(0),
                infoExecutionCourseAndExams.getNumberStudentesAttendingCourse());
        assertNotNull("Execution course was null!", infoExecutionCourseAndExams.getInfoExecutionCourse());
        assertEquals("Execution course was null!", "AC", infoExecutionCourseAndExams
                .getInfoExecutionCourse().getSigla());
    }

}