/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * Service ReadShiftSignup
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentLessons implements IService {

    public Object run(final InfoStudent infoStudent) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IAulaPersistente persistentLesson = persistentSupport.getIAulaPersistente();

        final InfoPerson infoPerson = infoStudent.getInfoPerson();
        final List lessons = persistentLesson.readLessonsByStudent(infoPerson.getUsername());
        final List infoLessons = new ArrayList(lessons.size());

        for (final Iterator iterator = lessons.iterator(); iterator.hasNext(); ) {
            final ILesson lesson = (ILesson) iterator.next();
            final IShift shift = lesson.getShift();

            final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoLessons.add(infoLesson);
        }

        return infoLessons;
    }

}