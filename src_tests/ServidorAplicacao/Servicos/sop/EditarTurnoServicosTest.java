/*
 * EditarTurnoServicosTest.java JUnit based test
 * 
 * Created on 27 de Outubro de 2002, 21:05
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.domain.Turno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoAula;

public class EditarTurnoServicosTest extends TestCaseDeleteAndEditServices {

    private InfoShift infoShift = null;

    public EditarTurnoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditarTurnoServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "EditarTurno";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsEditarTurno[] = new Object[2];
        argsEditarTurno[0] = this.infoShift;
        ITurno turno = Cloner.copyInfoShift2Shift(this.infoShift);
        InfoShift newInfoShift = Cloner.copyShift2InfoShift(turno);
        newInfoShift.setLotacao(new Integer(200));
        newInfoShift.setTipo(new TipoAula(TipoAula.DUVIDAS));
        newInfoShift.setNome("turno3243324324sdv");
        argsEditarTurno[1] = newInfoShift;

        return argsEditarTurno;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsEditarTurno[] = new Object[2];
        argsEditarTurno[0] = this.infoShift;
        ITurno turno = Cloner.copyInfoShift2Shift(this.infoShift);
        InfoShift newInfoShift = Cloner.copyShift2InfoShift(turno);
        newInfoShift.setLotacao(new Integer(200));
        newInfoShift.setTipo(new TipoAula(TipoAula.DUVIDAS));
        newInfoShift.setNome("turno3243324324sdv");
        argsEditarTurno[1] = newInfoShift;

        return argsEditarTurno;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            IExecutionCourse ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);

            ITurnoPersistente itp = sp.getITurnoPersistente();
            ITurno it = null;

            if (existing) {
                it = itp.readByNameAndExecutionCourse("turno1", ide);
            } else {
                it = new Turno("turnoXPTO", new TipoAula(TipoAula.TEORICA), new Integer(100), ide);
            }

            this.infoShift = (InfoShift) Cloner.get(it);

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