/*
 * CriarTurnoServicosTest.java JUnit based test
 * 
 * Created on 27 de Outubro de 2002, 18:53
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author tfc130
 */
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.domain.Turno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoAula;

public class CriarTurnoServicosTest extends TestCaseCreateServices {

    private InfoShift infoShift = null;

    public CriarTurnoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CriarTurnoServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "CriarTurno";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsCriarTurno[] = { this.infoShift };

        return argsCriarTurno;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsCriarTurno[] = { this.infoShift };

        return argsCriarTurno;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
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