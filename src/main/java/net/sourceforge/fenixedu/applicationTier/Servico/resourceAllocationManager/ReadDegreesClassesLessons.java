/*
 * ReadDegreesClassesLessons.java
 * 
 * Created on 2003/07/17
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewClassSchedule;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * TODO Remove cloner deste servi�o...
 */
public class ReadDegreesClassesLessons {

    @Atomic
    public static List run(List infoExecutionDegrees, AcademicInterval academicInterval) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List infoViewClassScheduleList = new ArrayList();

        List classes = new ArrayList();
        for (int i = 0; i < infoExecutionDegrees.size(); i++) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(i);
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
            Collection degreeClasses = executionDegree.getSchoolClasses();
            for (Iterator iterator = degreeClasses.iterator(); iterator.hasNext();) {
                SchoolClass klass = (SchoolClass) iterator.next();
                if (klass.getAcademicInterval().equals(academicInterval)) {
                    classes.add(klass);
                }
            }
        }

        for (int i = 0; i < classes.size(); i++) {
            InfoViewClassSchedule infoViewClassSchedule = new InfoViewClassSchedule();
            SchoolClass turma = (SchoolClass) classes.get(i);

            // read class lessons
            Collection<Shift> shiftList = turma.getAssociatedShifts();
            Iterator<Shift> iterator = shiftList.iterator();
            List infoLessonList = new ArrayList();
            while (iterator.hasNext()) {
                Shift shift = iterator.next();

                final Collection lessons = shift.getAssociatedLessons();
                for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
                    final Lesson lesson = (Lesson) iterator2.next();
                    final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                    infoLessonList.add(infoLesson);
                }
            }

            InfoClass infoClass = InfoClass.newInfoFromDomain(turma);
            infoViewClassSchedule.setInfoClass(infoClass);
            infoViewClassSchedule.setClassLessons(infoLessonList);
            infoViewClassScheduleList.add(infoViewClassSchedule);
        }

        return infoViewClassScheduleList;
    }
}