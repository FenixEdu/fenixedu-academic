/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerTurnosDeDisciplinaExecucao.
 * 
 * @author tfc130
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurnosDeDisciplinaExecucao implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        List infoShiftList = new ArrayList();
        List infoShiftAndLessons = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            infoShiftList = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            Iterator itShiftList = infoShiftList.iterator();

            while (itShiftList.hasNext()) {
                IShift shift = (IShift) itShiftList.next();
                InfoShift infoTurno = (InfoShift) Cloner.get(shift);

                List lessons = shift.getAssociatedLessons();
                Iterator itLessons = lessons.iterator();

                List infoLessons = new ArrayList();
                InfoLesson infoLesson;

                while (itLessons.hasNext()) {
                    infoLesson = Cloner.copyILesson2InfoLesson((ILesson) itLessons.next());

                    infoLesson.setInfoShift(infoTurno);

                    infoLessons.add(infoLesson);
                }

                infoTurno.setInfoLessons(infoLessons);
                infoShiftAndLessons.add(infoTurno);

            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoShiftAndLessons;

    }
}