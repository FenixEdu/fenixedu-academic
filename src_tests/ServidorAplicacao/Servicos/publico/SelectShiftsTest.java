package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author João Mota
 *  
 */
public class SelectShiftsTest extends TestCaseServicos {

    /**
     * Constructor for SelectShiftsTest.
     * 
     * @param testName
     */
    public SelectShiftsTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SelectShiftsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {
        Object argsSelecthifts[] = new Object[1];

        List result = null;
        InfoExecutionCourse iDE = new InfoExecutionCourse("Trabalho Final de Curso I", "TFCI",
                "programa1", new Double(0), new Double(0), new Double(0), new Double(0),
                new InfoExecutionPeriod("2º Semestre", new InfoExecutionYear("2002/2003")));
        InfoShift infoShift = new InfoShift();
        infoShift.setInfoDisciplinaExecucao(iDE);
        argsSelecthifts[0] = infoShift;

        try {
            result = (ArrayList) ServiceManagerServiceFactory.executeService(null, "SelectShifts",
                    argsSelecthifts);
        } catch (Exception e) {
            fail("test read all shifts");
            e.printStackTrace();
        }
        assertNotNull("test real all shifts", result);

    }

}