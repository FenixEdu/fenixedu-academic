/*
 * Created on 26/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAula;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.domain.Turno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByExecutionCourseCodeTest extends TestCaseReadServices {

    public ReadShiftsByExecutionCourseCodeTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadShiftsByExecutionCourseCode";
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(26) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 3;
    }

    protected Object getObjectToCompare() {
        List shiftList = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            ITurnoPersistente turnoPersistente = sp.getITurnoPersistente();
            ITurno turno19 = (ITurno) turnoPersistente.readByOID(Turno.class, new Integer(19));
            assertNotNull("turno19 null", turno19);

            ITurno turno20 = (ITurno) turnoPersistente.readByOID(Turno.class, new Integer(20));
            assertNotNull("turno20 null", turno20);

            ITurno turno21 = (ITurno) turnoPersistente.readByOID(Turno.class, new Integer(21));
            assertNotNull("turno21 null", turno21);

            sp.confirmarTransaccao();

            InfoShift infoTurno19 = (InfoShift) Cloner.get(turno19);
            InfoShift infoTurno20 = (InfoShift) Cloner.get(turno20);
            InfoShift infoTurno21 = (InfoShift) Cloner.get(turno21);

            List lessons = turno19.getAssociatedLessons();
            Iterator itLessons = lessons.iterator();
            List infoLessons = new ArrayList();
            InfoLesson infoLesson;
            while (itLessons.hasNext()) {
                infoLesson = Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
                infoLessons.add(infoLesson);
            }
            infoTurno19.setInfoLessons(infoLessons);

            lessons = turno20.getAssociatedLessons();
            itLessons = lessons.iterator();
            infoLessons = new ArrayList();
            while (itLessons.hasNext()) {
                infoLesson = Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
                infoLessons.add(infoLesson);
            }
            infoTurno20.setInfoLessons(infoLessons);

            lessons = turno21.getAssociatedLessons();
            itLessons = lessons.iterator();
            infoLessons = new ArrayList();
            while (itLessons.hasNext()) {
                infoLesson = Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
                infoLessons.add(infoLesson);
            }
            infoTurno21.setInfoLessons(infoLessons);

            shiftList.add(infoTurno19);
            shiftList.add(infoTurno20);
            shiftList.add(infoTurno21);
        } catch (ExcepcaoPersistencia e) {
            fail("exception: ExcepcaoPersistencia ");
        }
        return shiftList;
    }

}