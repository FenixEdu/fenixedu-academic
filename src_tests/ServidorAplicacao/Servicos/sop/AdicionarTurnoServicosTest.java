/*
 * AdicionarTurnoServicosTest.java JUnit based test
 * 
 * Created on 26 de Outubro de 2002, 12:54
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author tfc130
 */
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AdicionarTurnoServicosTest extends TestCaseCreateServices
{

    private InfoClass infoClass = null;
    private InfoShift infoShift = null;

    public AdicionarTurnoServicosTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(AdicionarTurnoServicosTest.class);

        return suite;
    }

    protected void setUp()
    {
        super.setUp();
    }

    protected void tearDown()
    {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested()
    {
        return "AdicionarTurno";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {

        this.ligarSuportePersistente(false);
        Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

        return argsCriarTurmaTurno;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {

        this.ligarSuportePersistente(true);
        Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

        return argsCriarTurmaTurno;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    private void ligarSuportePersistente(boolean existing)
    {

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            ICursoPersistente icp = sp.getICursoPersistente();
            ICurso ic = icp.readBySigla("LEIC");

            IPersistentDegreeCurricularPlan ipccp = sp.getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan ipcc = ipccp.readByNameAndDegree("plano1", ic);

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            ICursoExecucaoPersistente icep = sp.getICursoExecucaoPersistente();
            ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
            ITurma turma =
                turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413", ice, iep);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            IExecutionCourse ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);

            ITurnoPersistente itp = sp.getITurnoPersistente();
            ITurno it = null;
            if (existing)
            {
                it = itp.readByNameAndExecutionCourse("turno453", ide);
            }
            else
            {
                it = itp.readByNameAndExecutionCourse("turno1", ide);
            }

            this.infoClass = Cloner.copyClass2InfoClass(turma);
            this.infoShift = Cloner.copyIShift2InfoShift(it);

            sp.confirmarTransaccao();

        }
        catch (ExcepcaoPersistencia excepcao)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia ex)
            {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }
    }
}