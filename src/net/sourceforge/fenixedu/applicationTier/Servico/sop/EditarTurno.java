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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoAula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditarTurno implements IService {

    /**
     * The actor of this class.
     */
    public EditarTurno() {
    }

    public Object run(InfoShift infoShiftOld, InfoShift infoShiftNew) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoShift infoShift = null;

        try {
            newShiftIsValid(infoShiftOld, infoShiftNew.getTipo(), infoShiftNew
                    .getInfoDisciplinaExecucao(), infoShiftNew.getLotacao());
        } catch (InvalidNewShiftExecutionCourse ex) {
            throw new InvalidNewShiftExecutionCourse();
        } catch (InvalidNewShiftType ex) {
            throw new InvalidNewShiftType();
        } catch (InvalidNewShiftCapacity ex) {
            throw new InvalidNewShiftCapacity();
        }

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IShift shiftToEdit = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShiftOld.getIdInternal());

        int capacityDiference = infoShiftNew.getLotacao().intValue()
                - shiftToEdit.getLotacao().intValue();

        if (shiftToEdit.getAvailabilityFinal().intValue() + capacityDiference < 0) {
            throw new InvalidFinalAvailabilityException();
        }

        final IExecutionCourse executionCourse = Cloner
                .copyInfoExecutionCourse2ExecutionCourse(infoShiftNew.getInfoDisciplinaExecucao());

        IShift otherShiftWithSameNewName = sp.getITurnoPersistente().readByNameAndExecutionCourse(
                infoShiftNew.getNome(), executionCourse);

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


        shiftToEdit.setDisciplinaExecucao(executionCourse);

        // Also change the type of associated lessons and lessons execution
        // course
        if (shiftToEdit.getAssociatedLessons() != null) {
            for (int i = 0; i < shiftToEdit.getAssociatedLessons().size(); i++) {
                sp.getIAulaPersistente().simpleLockWrite(
                        (IDomainObject) shiftToEdit.getAssociatedLessons().get(i));
                ((ILesson) shiftToEdit.getAssociatedLessons().get(i)).setTipo(infoShiftNew.getTipo());
                ((ILesson) shiftToEdit.getAssociatedLessons().get(i)).setShift(shiftToEdit);
                //((ILesson)
                // shift.getAssociatedLessons().get(i)).setDisciplinaExecucao(executionCourse);
            }
        }

        return Cloner.copyShift2InfoShift(shiftToEdit);

        // NOTE: changed the lock twice strategy to see if the new turn exists
        //        catch (ExcepcaoPersistencia ex)
        //        {
        //            throw new FenixServiceException(ex);
        //        }

    }

    private void newShiftIsValid(InfoShift infoShiftOld, TipoAula newShiftType,
            InfoExecutionCourse newShiftExecutionCourse, Integer newShiftCapacity)
            throws FenixServiceException {

        // 1. Read shift lessons
        List shiftLessons = null;
        IShift shift = null;
        try {
            ISuportePersistente sp;
            sp = SuportePersistenteOJB.getInstance();
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

        // 3a. If NEW shift type is diferent from CURRENT shift type
        //     check if shift total duration exceeds new shift type duration
        if (!newShiftType.equals(infoShiftOld.getTipo())) {
            if (!newShiftTypeIsValid(shift, newShiftType, shiftDuration)) {
                throw new InvalidNewShiftType();
            }
        }

        // 3b. If NEW shift executionCourse is diferent from CURRENT shift
        // executionCourse
        //     check if shift total duration exceeds new executionCourse duration
        if (!newShiftExecutionCourse.equals(infoShiftOld.getInfoDisciplinaExecucao())) {
            if (!newShiftExecutionCourseIsValid(shift, newShiftExecutionCourse, shiftDuration)) {
                throw new InvalidNewShiftExecutionCourse();
            }
        }

        // 4. Check if NEW shift capacity is bigger then maximum lesson room
        // capacity
        //if (newShiftCapacity.intValue() > maxCapacity.intValue()) {
        //	throw new InvalidNewShiftCapacity();
        //}

    }

    private boolean newShiftTypeIsValid(IShift shift, TipoAula newShiftType, double shiftDuration) {
        // Verify if shift total duration exceeds new shift type duration
        if (newShiftType.equals(new TipoAula(TipoAula.TEORICA))) {
            if (shiftDuration > shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(new TipoAula(TipoAula.PRATICA))) {
            if (shiftDuration > shift.getDisciplinaExecucao().getPraticalHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
            if (shiftDuration > shift.getDisciplinaExecucao().getTheoPratHours().doubleValue()) {
                return false;
            }
        }
        if (newShiftType.equals(new TipoAula(TipoAula.LABORATORIAL))) {
            if (shiftDuration > shift.getDisciplinaExecucao().getLabHours().doubleValue()) {
                return false;
            }
        }
        return true;
    }

    private boolean newShiftExecutionCourseIsValid(IShift shift,
            InfoExecutionCourse newShiftExecutionCourse, double shiftDuration) {

        // Verify if shift total duration exceeds new executionCourse uration
        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
            if (shiftDuration > newShiftExecutionCourse.getTheoreticalHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
            if (shiftDuration > newShiftExecutionCourse.getPraticalHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
            if (shiftDuration > newShiftExecutionCourse.getTheoPratHours().doubleValue()) {
                return false;
            }
        }
        if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
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

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidNewShiftType extends FenixServiceException {

        /**
         *  
         */
        InvalidNewShiftType() {
            super();
        }

    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidNewShiftExecutionCourse extends FenixServiceException {

        /**
         *  
         */
        InvalidNewShiftExecutionCourse() {
            super();
        }

    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidNewShiftCapacity extends FenixServiceException {

        /**
         *  
         */
        InvalidNewShiftCapacity() {
            super();
        }

    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class ExistingShiftException extends FenixServiceException {

        /**
         *  
         */
        private ExistingShiftException() {
            super();
        }

        /**
         * @param cause
         */
        ExistingShiftException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidFinalAvailabilityException extends FenixServiceException {

        /**
         *  
         */
        InvalidFinalAvailabilityException() {
            super();
        }

    }

}