/*
 * LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest.java
 * JUnit based test
 *
 * Created on 04 de Janeiro de 2003, 12:35
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.util.TipoCurso;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest extends
        TestCaseReadServices {
    public LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest(
            java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {

        return "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");
        infoDegree.setTipoCurso(new TipoCurso(TipoCurso.LICENCIATURA_STRING));
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        infoDegreeCurricularPlan.setDegreeDuration(new Integer(15));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(15));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan,
                new InfoExecutionYear("2002/2003"));
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                new InfoExecutionYear("2002/2003"));
        infoExecutionPeriod.setSemester(new Integer(2));
        Integer curricularYear = new Integer(6);
        Object[] result = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");
        infoDegree.setTipoCurso(new TipoCurso(TipoCurso.LICENCIATURA_STRING));
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        infoDegreeCurricularPlan.setDegreeDuration(new Integer(15));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(15));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan,
                new InfoExecutionYear("2002/2003"));
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",
                new InfoExecutionYear("2002/2003"));
        infoExecutionPeriod.setSemester(new Integer(2));
        Integer curricularYear = new Integer(1);
        Object[] result = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        return null;
    }

    protected boolean needsAuthorization() {
        return true;
    }

}