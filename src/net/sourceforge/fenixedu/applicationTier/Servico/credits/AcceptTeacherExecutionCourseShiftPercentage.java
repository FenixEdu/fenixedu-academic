/*
 * Created on 19/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingLessonPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingPeriodException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage extends Service {

    public class InvalidProfessorshipPercentage extends FenixServiceException {

    }

    private Shift getShift(InfoShiftProfessorship infoShiftProfessorship) throws ExcepcaoPersistencia {

        Shift shift = (Shift) persistentObject.readByOID(Shift.class, infoShiftProfessorship
                .getInfoShift().getIdInternal());
        return shift;
    }

    public List run(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse,
            List infoShiftProfessorshipList) throws FenixServiceException, ExcepcaoPersistencia {
        List shiftWithErrors = new ArrayList();

        IPersistentShiftProfessorship shiftProfessorshipDAO = persistentSupport
                .getIPersistentTeacherShiftPercentage();

        // read professorship
        Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse
                .getIdInternal());

        Professorship professorship = null;
        if (teacher != null) {
            professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
        }

        if (professorship != null) {
            Iterator iterator = infoShiftProfessorshipList.iterator();

            List<ShiftProfessorship> shiftProfessorshipDeleted = new ArrayList<ShiftProfessorship>();

            List shiftProfessorshipAdded = addShiftProfessorships(shiftProfessorshipDAO, professorship,
                    iterator, shiftProfessorshipDeleted);

            validateShiftProfessorshipAdded(shiftProfessorshipAdded);
        }

        return shiftWithErrors; // retorna a lista com os turnos que causaram
        // erros!
    }

    private List addShiftProfessorships(IPersistentShiftProfessorship shiftProfessorshipDAO,
            Professorship professorship, Iterator iterator,
            List<ShiftProfessorship> shiftProfessorshipDeleted) throws InvalidProfessorshipPercentage,
            ExcepcaoPersistencia, OverlappingPeriodException, FenixServiceException {
        List<ShiftProfessorship> shiftProfessorshipAdded = new ArrayList<ShiftProfessorship>();
        while (iterator.hasNext()) {
            InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) iterator.next();

            Double percentage = infoShiftProfessorship.getPercentage();
            if ((percentage != null)
                    && ((percentage.doubleValue() > 100) || (percentage.doubleValue() < 0))) {
                throw new InvalidProfessorshipPercentage();
            }

            Shift shift = getShift(infoShiftProfessorship);

            ShiftProfessorship shiftProfessorship = shiftProfessorshipDAO.readByProfessorshipAndShift(
                    professorship, shift);

            lockOrDeleteShiftProfessorship(professorship, percentage, shift, shiftProfessorship,
                    shiftProfessorshipDeleted, shiftProfessorshipAdded);

        }
        return shiftProfessorshipAdded;
    }

    private void lockOrDeleteShiftProfessorship(Professorship professorship, Double percentage,
            Shift shift, ShiftProfessorship shiftProfessorship,
            List<ShiftProfessorship> shiftProfessorshipDeleted,
            List<ShiftProfessorship> shiftProfessorshipAdded) throws ExcepcaoPersistencia,
            OverlappingPeriodException, FenixServiceException {
        boolean toAdd = false;
        if (percentage.doubleValue() == 0) {
            shiftProfessorship.delete();
            shiftProfessorshipDeleted.add(shiftProfessorship);
            if (professorship.getAssociatedShiftProfessorship() != null) {
                professorship.getAssociatedShiftProfessorship().remove(shiftProfessorship);
            }
            if (shift.getAssociatedShiftProfessorship() != null) {
                shift.getAssociatedShiftProfessorship().remove(shiftProfessorship);
            }
        } else {

            if (shiftProfessorship == null) {
                toAdd = true;
                shiftProfessorship = DomainFactory.makeShiftProfessorship();
                shiftProfessorship.setProfessorship(professorship);
                shiftProfessorship.setShift(shift);
            }

            if (toAdd) {
                if (professorship.getAssociatedShiftProfessorship() != null) {
                    professorship.getAssociatedShiftProfessorship().add(shiftProfessorship);
                }
                if (shift.getAssociatedShiftProfessorship() != null) {
                    shift.getAssociatedShiftProfessorship().add(shiftProfessorship);
                }
            }
            shiftProfessorship.setPercentage(percentage);

            CreditsValidator.validatePeriod(professorship.getTeacher().getIdInternal(), professorship
                    .getExecutionCourse().getExecutionPeriod().getIdInternal(), shiftProfessorship);

            shiftProfessorshipAdded.add(shiftProfessorship);
        }
    }

    /**
     * @param infoShiftProfessorshipAdded
     */
    private void validateShiftProfessorshipAdded(List shiftProfessorshipAdded)
            throws OverlappingLessonPeriod {

        if (shiftProfessorshipAdded.size() > 1) {
            List lessonsList = new ArrayList();
            List fullShiftLessonList = new ArrayList();
            for (int i = 0; i < shiftProfessorshipAdded.size(); i++) {
                final ShiftProfessorship shiftProfessorship = (ShiftProfessorship) shiftProfessorshipAdded
                        .get(i);
                List shiftLessons = shiftProfessorship.getShift().getAssociatedLessons();
                lessonsList.addAll(shiftLessons);
                if (shiftProfessorship.getPercentage().doubleValue() == 100) {
                    fullShiftLessonList.addAll(shiftLessons);
                }
            }

            for (int j = 0; j < fullShiftLessonList.size(); j++) {
                Lesson lesson = (Lesson) lessonsList.get(j);
                if (overlapsWithAny(lesson, lessonsList)) {
                    throw new OverlappingLessonPeriod();
                }
            }
        }
    }

    /**
     * @param lesson
     * @param lessonsList
     * @return
     */
    private boolean overlapsWithAny(Lesson lesson, List lessonsList) {
        DiaSemana lessonWeekDay = lesson.getDiaSemana();
        Calendar lessonStart = lesson.getInicio();
        Calendar lessonEnd = lesson.getFim();
        for (int i = 0; i < lessonsList.size(); i++) {
            Lesson otherLesson = (Lesson) lessonsList.get(i);
            if (!otherLesson.equals(lesson)) {
                if (otherLesson.getDiaSemana().equals(lessonWeekDay)) {
                    Calendar otherStart = otherLesson.getInicio();
                    Calendar otherEnd = otherLesson.getFim();
                    if (((otherStart.equals(lessonStart)) && otherEnd.equals(lessonEnd))
                            || (lessonStart.before(otherEnd) && lessonStart.after(otherStart))
                            || (lessonEnd.before(otherEnd) && lessonEnd.after(otherStart))) {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}