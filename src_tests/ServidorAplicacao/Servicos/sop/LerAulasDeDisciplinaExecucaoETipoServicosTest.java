/*
 * LerAulasDeDisciplinaExecucaoETipoServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 18:36
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TipoAula;

public class LerAulasDeDisciplinaExecucaoETipoServicosTest extends TestCaseReadServices {

    public LerAulasDeDisciplinaExecucaoETipoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerAulasDeDisciplinaExecucaoETipoServicosTest.class);

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
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = new ExecutionCourseKeyAndLessonType(
                new TipoAula(3), "xpto");
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse("Trabalho Final de Curso I",
                "TFCI", "programa1", new Double(0), new Double(0), new Double(0), new Double(0),
                new InfoExecutionPeriod("2º Semestre", new InfoExecutionYear("2002/2003")));
        Object[] result = { tipoAulaAndKeyDisciplinaExecucao, infoExecutionCourse };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = new ExecutionCourseKeyAndLessonType(
                new TipoAula(1), "TFCI");
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse("Trabalho Final de Curso I",
                "TFCI", "programa1", new Double(0), new Double(0), new Double(0), new Double(0),
                new InfoExecutionPeriod("2º Semestre", new InfoExecutionYear("2002/2003")));
        Object[] result = { tipoAulaAndKeyDisciplinaExecucao, infoExecutionCourse };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 6;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {

        return "LerAulasDeDisciplinaExecucaoETipo";
    }

    protected boolean needsAuthorization() {
        return true;
    }
}