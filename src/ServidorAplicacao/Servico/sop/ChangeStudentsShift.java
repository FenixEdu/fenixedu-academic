/*
 * 
 * Created on 2004/09/16
 */
package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Turno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.commons.SendMail;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

        ITurno oldShift = (ITurno) persistentShift.readByOID(Turno.class, oldShiftId);
        ITurno newShift = (ITurno) persistentShift.readByOID(Turno.class, newShiftId);

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

                    IPessoa person = student.getPerson();
                    if (person.getEmail() != null && person.getEmail().length() > 0) {
                        toMails.add(person.getEmail());
                    }
                } else {
                    persistentShiftStudent.delete(shiftStudent);
                }
            }

            IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
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