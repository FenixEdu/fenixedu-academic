/*
 * EditarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:54
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarTurmaServicosTest extends TestCaseDeleteAndEditServices {

    private InfoClass infoClass = null;

    public EditarTurmaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditarTurmaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "EditarTurma";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsEditarTurma[] = new Object[2];
        argsEditarTurma[0] = this.infoClass;
        ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
        InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
        newInfoClass.setAnoCurricular(new Integer(2));
        //TODO: verify if infoExecutionDegree is needed for the service
        newInfoClass.setInfoExecutionPeriod(new InfoExecutionPeriod("2º Semestre",
                new InfoExecutionYear("2002/2003")));
        newInfoClass.setNome("turmaXPTO");
        argsEditarTurma[1] = newInfoClass;

        return argsEditarTurma;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsEditarTurma[] = new Object[2];
        argsEditarTurma[0] = this.infoClass;
        ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
        InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
        newInfoClass.setAnoCurricular(new Integer(2));
        //TODO: verify if infoExecutionDegree is needed for the service
        newInfoClass.setInfoExecutionPeriod(new InfoExecutionPeriod("2º Semestre",
                new InfoExecutionYear("2002/2003")));
        newInfoClass.setNome("turmaXPTO");
        argsEditarTurma[1] = newInfoClass;

        return argsEditarTurma;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            ICursoPersistente icp = sp.getICursoPersistente();
            ICurso ic = icp.readBySigla("LEIC");

            IPersistentDegreeCurricularPlan ipccp = sp.getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan ipcc = ipccp.readByNameAndDegree("plano1", ic);

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionDegree icep = sp.getIPersistentExecutionDegree();
            ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
            ITurma turma = null;
            if (existing) {
                turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413", ice,
                        iep);
            } else {
                turma = new Turma("asdasdsad", new Integer(1), ice, iep);
            }

            this.infoClass = Cloner.copyClass2InfoClass(turma);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }
    }
}