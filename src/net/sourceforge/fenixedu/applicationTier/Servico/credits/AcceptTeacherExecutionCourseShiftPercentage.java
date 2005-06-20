/*
 * Created on 19/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingLessonPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingPeriodException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage implements IService {
    /**
     * @author jpvl
     */
    public class InvalidProfessorshipPercentage extends FenixServiceException {

    }

    /**
     *  
     */
    public AcceptTeacherExecutionCourseShiftPercentage() {

    }

    private IShift getIShift(ITurnoPersistente shiftDAO, InfoShiftProfessorship infoShiftProfessorship)
            throws ExcepcaoPersistencia {

        IShift shift = (IShift) shiftDAO.readByOID(Shift.class, infoShiftProfessorship.getInfoShift().getIdInternal());
        return shift;
    }

    public List run(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse,
            List infoShiftProfessorshipList) throws FenixServiceException {
        List shiftWithErrors = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();            
            IPersistentShiftProfessorship shiftProfessorshipDAO = sp
                    .getIPersistentTeacherShiftPercentage();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            
            //read professorship
            IProfessorship professorship = professorshipDAO.readByTeacherAndExecutionCourse(infoTeacher.getIdInternal(),
                    infoExecutionCourse.getIdInternal());

            if (professorship != null) {
                Iterator iterator = infoShiftProfessorshipList.iterator();

                List shiftProfessorshipDeleted = new ArrayList();

                List shiftProfessorshipAdded = addShiftProfessorships(shiftDAO, shiftProfessorshipDAO,
                        professorship, iterator, shiftProfessorshipDeleted);

                validateShiftProfessorshipAdded(shiftProfessorshipAdded);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return shiftWithErrors; //retorna a lista com os turnos que causaram
        // erros!
    }

    private List addShiftProfessorships(ITurnoPersistente shiftDAO,
            IPersistentShiftProfessorship shiftProfessorshipDAO, IProfessorship professorship,
            Iterator iterator, List shiftProfessorshipDeleted) throws InvalidProfessorshipPercentage,
            ExcepcaoPersistencia, OverlappingPeriodException, FenixServiceException {
        List shiftProfessorshipAdded = new ArrayList();
        while (iterator.hasNext()) {
            InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) iterator.next();

            Double percentage = infoShiftProfessorship.getPercentage();
            if ((percentage != null)
                    && ((percentage.doubleValue() > 100) || (percentage.doubleValue() < 0))) {
                throw new InvalidProfessorshipPercentage();
            }

            IShift shift = getIShift(shiftDAO, infoShiftProfessorship);

            IShiftProfessorship shiftProfessorship = shiftProfessorshipDAO.readByProfessorshipAndShift(
                    professorship, shift);

            lockOrDeleteShiftProfessorship(shiftProfessorshipDAO, professorship, percentage, shift,
                    shiftProfessorship, shiftProfessorshipDeleted, shiftProfessorshipAdded);

        }
        return shiftProfessorshipAdded;
    }

    private void lockOrDeleteShiftProfessorship(IPersistentShiftProfessorship shiftProfessorshipDAO,
            IProfessorship professorship, Double percentage, IShift shift,
            IShiftProfessorship shiftProfessorship, List shiftProfessorshipDeleted,
            List shiftProfessorshipAdded) throws ExcepcaoPersistencia, OverlappingPeriodException,
            FenixServiceException {
        boolean toAdd = false;
        if (percentage.doubleValue() == 0) {
            shiftProfessorshipDAO.delete(shiftProfessorship);
            shiftProfessorshipDeleted.add(shiftProfessorship);
        } else {

            if (shiftProfessorship == null) {
                toAdd = true;
                shiftProfessorship = new ShiftProfessorship();
                shiftProfessorship.setProfessorship(professorship);
                shiftProfessorship.setShift(shift);                
            }
            shiftProfessorshipDAO.simpleLockWrite(shiftProfessorship);

            if(toAdd){
                professorship.getAssociatedShiftProfessorship().add(shiftProfessorship);
                shift.getAssociatedShiftProfessorship().add(shiftProfessorship);
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
                final IShiftProfessorship shiftProfessorship = (IShiftProfessorship) shiftProfessorshipAdded
                        .get(i);
                List shiftLessons = shiftProfessorship.getShift().getAssociatedLessons();
                lessonsList.addAll(shiftLessons);
                if (shiftProfessorship.getPercentage().doubleValue() == 100) {
                    fullShiftLessonList.addAll(shiftLessons);
                }
            }

            for (int j = 0; j < fullShiftLessonList.size(); j++) {
                ILesson lesson = (ILesson) lessonsList.get(j);
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
    private boolean overlapsWithAny(ILesson lesson, List lessonsList) {
        DiaSemana lessonWeekDay = lesson.getDiaSemana();
        Calendar lessonStart = lesson.getInicio();
        Calendar lessonEnd = lesson.getFim();
        for (int i = 0; i < lessonsList.size(); i++) {
            ILesson otherLesson = (ILesson) lessonsList.get(i);
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