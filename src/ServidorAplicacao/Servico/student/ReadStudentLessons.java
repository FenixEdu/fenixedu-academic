/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.InfoPerson;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import Dominio.IAula;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentLessons implements IService {

    public Object run(final InfoStudent infoStudent) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IAulaPersistente persistentLesson = persistentSupport.getIAulaPersistente();

        final InfoPerson infoPerson = infoStudent.getInfoPerson();
        final List lessons = persistentLesson.readLessonsByStudent(infoPerson.getUsername());
        final List infoLessons = new ArrayList(lessons.size());

        for (final Iterator iterator = lessons.iterator(); iterator.hasNext(); ) {
            final IAula lesson = (IAula) iterator.next();
            final ITurno shift = lesson.getShift();

            final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoLessons.add(infoLesson);
        }

        return infoLessons;
    }

}