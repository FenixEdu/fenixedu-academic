/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerTurnosDeDisciplinaExecucao.
 * 
 * @author tfc130
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurnosDeDisciplinaExecucao implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        List infoShiftList = new ArrayList();
        List infoShiftAndLessons = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            infoShiftList = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            Iterator itShiftList = infoShiftList.iterator();

            while (itShiftList.hasNext()) {
                ITurno shift = (ITurno) itShiftList.next();
                InfoShift infoTurno = (InfoShift) Cloner.get(shift);

                List lessons = shift.getAssociatedLessons();
                Iterator itLessons = lessons.iterator();

                List infoLessons = new ArrayList();
                InfoLesson infoLesson;

                while (itLessons.hasNext()) {
                    infoLesson = Cloner.copyILesson2InfoLesson((IAula) itLessons.next());

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