/*
 * LerAulasDeDisciplinaExecucaoETipo.java
 *
 * Created on 28 de Outubro de 2002, 18:03
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucaoETipo.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeDisciplinaExecucaoETipo implements IService {

    public Object run(ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao,
            InfoExecutionCourse infoExecutionCourse) {

        ArrayList infoAulas = null;

        try {

            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //			List aulas =
            //				sp.getIAulaPersistente().readByExecutionCourseAndLessonType(
            //					executionCourse,
            //					tipoAulaAndKeyDisciplinaExecucao.getTipoAula());
            List aulas = new ArrayList();

            List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            for (int i = 0; i < shifts.size(); i++) {
                ITurno shift = (ITurno) shifts.get(i);
                List aulasTemp = sp.getIAulaPersistente().readLessonsByShiftAndLessonType(shift,
                        tipoAulaAndKeyDisciplinaExecucao.getTipoAula());

                aulas.addAll(aulasTemp);
            }

            Iterator iterator = aulas.iterator();
            infoAulas = new ArrayList();
            while (iterator.hasNext()) {
                IAula lesson = (IAula) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
                ITurno shift = lesson.getShift();
                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                infoLesson.setInfoShift(infoShift);

                infoAulas.add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoAulas;
    }

}