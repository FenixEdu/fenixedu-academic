package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeStudentsShift implements IService {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

        Shift oldShift = (Shift) persistentShift.readByOID(Shift.class, oldShiftId);
        Shift newShift = (Shift) persistentShift.readByOID(Shift.class, newShiftId);

        if (oldShift == null || newShift == null || !oldShift.getTipo().equals(newShift.getTipo())
                || !oldShift.getDisciplinaExecucao().getIdInternal().equals(newShift.getDisciplinaExecucao().getIdInternal())) {
            throw new UnableToTransferStudentsException();
        }

        SendMail sendMail = new SendMail();
        List<String> emptyList = new ArrayList<String>();
        List<String> toMails = new ArrayList<String>();

        final List<Student> oldStudents = oldShift.getStudents();
        final List<Student> newStudents = newShift.getStudents();
        while (!oldStudents.isEmpty()) {
            final Student student = oldStudents.get(0);
            if (!newStudents.contains(student)) {
                newStudents.add(student);

                Person person = student.getPerson();
                if (person.getEmail() != null && person.getEmail().length() > 0) {
                    toMails.add(person.getEmail());
                }
            } else {
                oldStudents.remove(student);
            }
        }

            
        Person person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
        sendMail.run(emptyList, emptyList, toMails, person.getNome(), person.getEmail(), 
                "Alteração de turnos",
                "Devido a alterações nos horários, a sua reserva no turno "
                + oldShift.getNome() 
                + " foi transferida para o turno "
                + newShift.getNome());
    }

    public class UnableToTransferStudentsException extends FenixServiceException {
    }

}