package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeStudentsShift extends Service {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId)
            throws ExcepcaoPersistencia, FenixServiceException {

        final Shift oldShift = rootDomainObject.readShiftByOID(oldShiftId);
        final Shift newShift = rootDomainObject.readShiftByOID(newShiftId);

        if (oldShift == null || newShift == null || !oldShift.getTipo().equals(newShift.getTipo())
                || !oldShift.getDisciplinaExecucao().getIdInternal().equals(newShift.getDisciplinaExecucao().getIdInternal())) {
            throw new UnableToTransferStudentsException();
        }

        SendMail sendMail = new SendMail();
        List<String> emptyList = new ArrayList<String>();
        List<String> toMails = new ArrayList<String>();

        final List<Registration> oldStudents = oldShift.getStudents();
        final List<Registration> newStudents = newShift.getStudents();
        while (!oldStudents.isEmpty()) {
            final Registration registration = oldStudents.get(0);
            if (!newStudents.contains(registration)) {
                newStudents.add(registration);

                Person person = registration.getPerson();
                if (person.getEmail() != null && person.getEmail().length() > 0) {
                    toMails.add(person.getEmail());
                }
            } else {
                oldStudents.remove(registration);
            }
        }

            
        Person person = Person.readPersonByUsername(userView.getUtilizador());
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