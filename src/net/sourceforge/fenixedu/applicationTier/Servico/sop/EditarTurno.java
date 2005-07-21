/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço EditarTurno.
 * 
 * @author tfc130
 */
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditarTurno implements IService {

    public Object run(InfoShift infoShiftOld, InfoShift infoShiftNew) throws FenixServiceException,
            ExcepcaoPersistencia {

        newShiftIsValid(infoShiftOld, infoShiftNew.getTipo(), infoShiftNew.getInfoDisciplinaExecucao(),
                infoShiftNew.getLotacao());

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IShift shiftToEdit = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShiftOld.getIdInternal());

        int capacityDiference = infoShiftNew.getLotacao().intValue()
                - shiftToEdit.getLotacao().intValue();

        if (shiftToEdit.getAvailabilityFinal().intValue() + capacityDiference < 0) {
            throw new InvalidFinalAvailabilityException();
        }

        IShift otherShiftWithSameNewName = sp.getITurnoPersistente().readByNameAndExecutionCourse(
                infoShiftNew.getNome(), infoShiftNew.getInfoDisciplinaExecucao().getIdInternal());

        if ((otherShiftWithSameNewName != null)
                && !(otherShiftWithSameNewName.getIdInternal().equals(shiftToEdit.getIdInternal()))) {
            throw new ExistingServiceException("Duplicate Entry: " + otherShiftWithSameNewName.getNome());
        }

        sp.getITurnoPersistente().simpleLockWrite(shiftToEdit);

        shiftToEdit.setNome(infoShiftNew.getNome());
        shiftToEdit.setTipo(infoShiftNew.getTipo());

        shiftToEdit.setLotacao(infoShiftNew.getLotacao());
        shiftToEdit.setAvailabilityFinal(new Integer(shiftToEdit.getAvailabilityFinal().intValue()
                + capacityDiference));

        final IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class,
                        infoShiftNew.getInfoDisciplinaExecucao().getIdInternal());
        shiftToEdit.setDisciplinaExecucao(executionCourse);

        // Also change the type of associated lessons and lessons execution
        // course
        if (shiftToEdit.getAssociatedLessons() != null) {
            for (int i = 0; i < shiftToEdit.getAssociatedLessons().size(); i++) {
                sp.getIAulaPersistente().simpleLockWrite(shiftToEdit.getAssociatedLessons().get(i));
                shiftToEdit.getAssociatedLessons().get(i).setTipo(infoShiftNew.getTipo());
                shiftToEdit.getAssociatedLessons().get(i).setShift(shiftToEdit);

            }
        }

        return InfoShift.newInfoFromDomain(shiftToEdit);
    }

    private void newShiftIsValid(InfoShift infoShiftOld, ShiftType newShiftType,
            InfoExecutionCourse newShiftExecutionCourse, Integer newShiftCapacity)
            throws FenixServiceException {

        // 1. Read shift lessons
        List shiftLessons = null;
        IShift shift = null;
        try {
            ISuportePersistente sp;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                    infoShiftOld.getIdInternal());
            shiftLessons = shift.getAssociatedLessons();
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        // 2. Count shift total duration and get maximum lesson room capacity
        Integer maxCapacity = new Integer(0);
        double shiftDuration = 0;
        for (int i = 0; i < shiftLessons.size(); i++) {
            ILesson lesson = ((ILesson) shiftLessons.get(i));
            shiftDuration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
            if (lesson.getRoomOccupation().getRoom().getCapacidadeNormal().intValue() > maxCapacity
                    .intValue()) {
                maxCapacity = lesson.getRoomOccupation().getRoom().getCapacidadeNormal();
            }
        }

        // 3a. If NEW shift type is diferent from CURRENT shift type check if
        // shift total duration exceeds new shift type duration
        if (!newShiftType.equals(infoShiftOld.getTipo())) {
            if (!newShiftTypeIsValid(shift, newShiftType, shiftDuration)) {
                throw new InvalidNewShiftType();
            }
        }

        // 3b. If NEW shift executionCourse is diferent from CURRENT shift
        // executionCourse check if shift total duration exceeds new
        // executionCourse duration
        if (!newShiftExecutionCourse.equals(infoShiftOld.getInfoDisciplinaExecucao())) {
            if (!newShiftExecutionCourseIsValid(shift, newShiftExecutionCourse, shiftDuration)) {
                throw new InvalidNewShiftExecutionCourse();
            }
        }

        // 4. Check if NEW shift capacity is bigger then maximum lesson room
        // capacity
        // if (newShiftCapacity.intValue() > maxCapacity.intValue()) {
        // throw new InvalidNewShiftCapacity();
        // }
    }

    private boolean newShiftTypeIsValid(IShift shift, ShiftType newShiftType, double shiftDuration) {
        // Verify if shift total duration exceeds new shift type duration
        if (newShiftType.equals(ShiftType.TEORICA)) {
            if (shiftDuration > shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(ShiftType.PRATICA)) {
            if (shiftDuration > shift.getDisciplinaExecucao().getPraticalHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(ShiftType.TEORICO_PRATICA)) {
            if (shiftDuration > shift.getDisciplinaExecucao().getTheoPratHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(ShiftType.LABORATORIAL)) {
            if (shiftDuration > shift.getDisciplinaExecucao().getLabHours().doubleValue()) {
                return false;
            }
        }
        return true;
    }

    private boolean newShiftExecutionCourseIsValid(IShift shift,
            InfoExecutionCourse newShiftExecutionCourse, double shiftDuration) {

        // Verify if shift total duration exceeds new executionCourse uration
        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            if (shiftDuration > newShiftExecutionCourse.getTheoreticalHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(ShiftType.PRATICA)) {
            if (shiftDuration > newShiftExecutionCourse.getPraticalHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
            if (shiftDuration > newShiftExecutionCourse.getTheoPratHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
            if (shiftDuration > newShiftExecutionCourse.getLabHours().doubleValue()) {
                return false;
            }
        }
        return true;
    }

    private Integer getLessonDurationInMinutes(ILesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;

        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    public class InvalidNewShiftType extends FenixServiceException {
        InvalidNewShiftType() {
            super();
        }
    }

    public class InvalidNewShiftExecutionCourse extends FenixServiceException {
        InvalidNewShiftExecutionCourse() {
            super();
        }
    }

    public class InvalidNewShiftCapacity extends FenixServiceException {
        InvalidNewShiftCapacity() {
            super();
        }
    }

    public class ExistingShiftException extends FenixServiceException {
        private ExistingShiftException() {
            super();
        }

        ExistingShiftException(Throwable cause) {
            super(cause);
        }
    }

    public class InvalidFinalAvailabilityException extends FenixServiceException {
        InvalidFinalAvailabilityException() {
            super();
        }
    }

}