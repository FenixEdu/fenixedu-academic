/*
 * ReadExamsMapServiceTest.java
 *
 * Created on 2003/04/02
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseRequeiersAuthorizationServices;

public class ReadExamsMapServiceTest extends TestCaseRequeiersAuthorizationServices {

    InfoDegree infoDegree = null;

    InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

    InfoExecutionYear infoExecutionYear = null;

    InfoExecutionPeriod infoExecutionPeriod = null;

    InfoExecutionDegree infoExecutionDegree = null;

    List curricularYears = null;

    public ReadExamsMapServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExamsMapServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamsMap";
    }

    public void testReadValidResult() {
        infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
        infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);

        infoDegreeCurricularPlan.setDegreeDuration(new Integer(5));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));

        infoExecutionYear = new InfoExecutionYear("2002/2003");
        infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(2));
        infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
        curricularYears = new ArrayList();
        curricularYears.add(new Integer(1));
        curricularYears.add(new Integer(3));
        curricularYears.add(new Integer(5));

        args = new Object[3];
        args[0] = infoExecutionDegree;
        args[1] = curricularYears;
        args[2] = infoExecutionPeriod;

        try {
            callServiceWithAuthorizedUserView();
        } catch (FenixServiceException e) {
            fail("Unexpected exception: " + e);
        }

        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2003);
        startSeason1.set(Calendar.MONTH, Calendar.JUNE);
        startSeason1.set(Calendar.DAY_OF_MONTH, 23);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2003);
        endSeason2.set(Calendar.MONTH, Calendar.JULY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 26);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

        assertNotNull("Result of call to service was null!", result);
        assertEquals("Type of result was unexpected!", InfoExamsMap.class.getName(), result.getClass()
                .getName());
        InfoExamsMap infoExamsMap = (InfoExamsMap) result;
        assertEquals("Start of exam season1 was unexpected!", startSeason1, infoExamsMap
                .getStartSeason1());
        assertEquals("End of exam season1 was unexpected!", null, infoExamsMap.getEndSeason1());
        assertEquals("Start of exam season2 was unexpected!", null, infoExamsMap.getStartSeason2());
        assertEquals("End of exam season2 was unexpected!", endSeason2, infoExamsMap.getEndSeason2());
        assertNotNull("Curricular years list was null!", infoExamsMap.getCurricularYears());
        assertEquals("Unexpected number of curricular years!", 3, infoExamsMap.getCurricularYears()
                .size());
        assertTrue("Curricular year 1 not in list!", infoExamsMap.getCurricularYears().contains(
                new Integer(1)));
        assertTrue("Curricular year 3 not in list!", infoExamsMap.getCurricularYears().contains(
                new Integer(3)));
        assertTrue("Curricular year 5 not in list!", infoExamsMap.getCurricularYears().contains(
                new Integer(5)));
        assertNotNull("Execution course list was null!", infoExamsMap.getExecutionCourses());
        assertEquals("Unexpected number of execution courses!", 1, infoExamsMap.getExecutionCourses()
                .size());
    }

}