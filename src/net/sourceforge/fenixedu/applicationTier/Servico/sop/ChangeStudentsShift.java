/*
 * 
 * Created on 2004/09/16
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ChangeStudentsShift implements IService {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
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

            List newShiftStudents = newShift.getStudents();
            for (int i = 0; i < newShiftStudents.size(); i++) {
                IStudent student = (IStudent) newShiftStudents.get(i);
                studentsInNewShiftSet.add(student.getIdInternal());
            }
            
            List shiftStudents = oldShift.getStudents();
            Iterator<IStudent> iter = shiftStudents.iterator();
            while (iter.hasNext()) {
                IStudent student = iter.next();
                if (!studentsInNewShiftSet.contains(student.getIdInternal())) {
                    newShift.getStudents().add(student);
                    iter.remove();
                                        
                    IPerson person = student.getPerson();
                    if (person.getEmail() != null && person.getEmail().length() > 0) {
                        toMails.add(person.getEmail());
                    }
                } else {
                    iter.remove();
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