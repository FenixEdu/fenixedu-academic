/*
 * LerAulasDeTurma.java
 * 
 * Created on 29 de Outubro de 2002, 22:18
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurma
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ILesson;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeTurma implements IService {

    public List run(InfoClass infoClass) throws ExcepcaoPersistencia {
        ArrayList infoLessonList = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurmaPersistente persistentDomainClass = sp.getITurmaPersistente();
        ISchoolClass group = null;
        if (infoClass.getIdInternal() != null) {

            group = (ISchoolClass) persistentDomainClass.readByOID(SchoolClass.class, infoClass.getIdInternal());
        } else {
            group = Cloner.copyInfoClass2Class(infoClass);
        }

        List shiftList = sp.getITurmaTurnoPersistente().readByClass(group);

        Iterator iterator = shiftList.iterator();

        infoLessonList = new ArrayList();

        while (iterator.hasNext()) {
            IShift shift = (IShift) iterator.next();
            List lessonList = shift.getAssociatedLessons();
            Iterator lessonIterator = lessonList.iterator();
            while (lessonIterator.hasNext()) {
                ILesson elem = (ILesson) lessonIterator.next();
                // ILesson lesson = (ILesson)
                // sp.getIAulaPersistente().readByOID(
                // Lesson.class, elem.getIdInternal());
                // InfoLesson infoLesson =
                // Cloner.copyILesson2InfoLesson(elem);
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);

                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                infoLesson.setInfoShift(infoShift);

                if (infoLesson != null) {
                    infoLessonList.add(infoLesson);
                }

            }
        }

        return infoLessonList;
    }

}