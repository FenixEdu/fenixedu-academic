/*
 * 
 * Created on 2004/09/16
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ITurnoAluno;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ChangeStudentsShift implements IService {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
        ITurnoAlunoPersistente persistentShiftStudent = persistentSupport.getITurnoAlunoPersistente();
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

        IShift oldShift = (IShift) persistentShift.readByOID(Shift.class, oldShiftId);
        IShift newShift = (IShift) persistentShift.readByOID(Shift.class, newShiftId);

        SendMail sendMail = new SendMail();
        List emptyList = new ArrayList();
        List toMails = new ArrayList();

        if (oldShift != null
                && newShift != null
                && oldShift.getDisciplinaExecucao().getIdInternal().equals(
                        newShift.getDisciplinaExecucao().getIdInternal())
                && oldShift.getTipo().equals(newShift.getTipo())) {
            Set studentsInNewShiftSet = new HashSet();

            List newShiftStudents = persistentShiftStudent.readStudentShiftByShift(newShift);
            for (int i = 0; i < newShiftStudents.size(); i++) {
                ITurnoAluno shiftStudent = (ITurnoAluno) newShiftStudents.get(i);
                studentsInNewShiftSet.add(shiftStudent.getStudent().getIdInternal());
            }

            List shiftStudents = persistentShiftStudent.readStudentShiftByShift(oldShift);
            for (int i = 0; i < shiftStudents.size(); i++) {
                ITurnoAluno shiftStudent = (ITurnoAluno) shiftStudents.get(i);
                IStudent student = shiftStudent.getStudent();
                if (!studentsInNewShiftSet.contains(student.getIdInternal())) {
                    persistentShiftStudent.simpleLockWrite(shiftStudent);
                    shiftStudent.setShift(newShift);

                    IPerson person = student.getPerson();
                    if (person.getEmail() != null && person.getEmail().length() > 0) {
                        toMails.add(person.getEmail());
                    }
                } else {
                    persistentShiftStudent.delete(shiftStudent);
                }
            }

            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            sendMail
                    .run(emptyList, emptyList, toMails, person.getNome(), person.getEmail(),
                            "Alteração de turnos",
                            "Devido a alterações nos horários, a sua reserva no turno "
                                    + oldShift.getNome() + " foi transferida para o turno "
                                    + newShift.getNome());
        } else {
            throw new UnableToTransferStudentsException();
        }
    }

    public class UnableToTransferStudentsException extends FenixServiceException {

        /**
         *  
         */
        public UnableToTransferStudentsException() {
            super();
        }

        /**
         * @param errorType
         */
        public UnableToTransferStudentsException(int errorType) {
            super(errorType);
        }

        /**
         * @param s
         */
        public UnableToTransferStudentsException(String s) {
            super(s);
        }

        /**
         * @param cause
         */
        public UnableToTransferStudentsException(Throwable cause) {
            super(cause);
        }

        /**
         * @param message
         * @param cause
         */
        public UnableToTransferStudentsException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}