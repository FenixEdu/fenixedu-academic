package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeStudentsShift extends Service {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId, final Set<Registration> registrations)
            throws ExcepcaoPersistencia, FenixServiceException {

        final Shift oldShift = rootDomainObject.readShiftByOID(oldShiftId);
        final Shift newShift = rootDomainObject.readShiftByOID(newShiftId);

        if (newShift != null) {
            if (oldShift == null || !oldShift.getTipo().equals(newShift.getTipo())
                    || !oldShift.getDisciplinaExecucao().getIdInternal().equals(newShift.getDisciplinaExecucao().getIdInternal())) {
                throw new UnableToTransferStudentsException();
            }
        }

        final List<String> emptyList = new ArrayList<String>();
        final List<String> toMails = new ArrayList<String>();

        oldShift.getStudentsSet().removeAll(registrations);
        if (newShift != null) {
            newShift.getStudentsSet().addAll(registrations);
        }
        for (final Registration registration : registrations) {
            final Person person = registration.getPerson();
            if (person.getEmail() != null && person.getEmail().length() > 0) {
                toMails.add(person.getEmail());
            }
        }

        final String subject = "Alteração de turnos";
        final String messagePrefix = "Devido a alterações nos horários, a sua reserva no turno " + oldShift.getNome(); 
        final String messagePosfix = newShift == null ? " foi removida. Deverá efectuar uma nova reserva no turno que pretende."
                : " foi transferida para o turno " + newShift.getNome();
        final String message = messagePrefix + messagePosfix;
        final Person person = Person.readPersonByUsername(userView.getUtilizador());
        new Email("GOP", "gop@ist.utl.pt", null, emptyList, emptyList, toMails, subject, message);
    }

    public class UnableToTransferStudentsException extends FenixServiceException {
    }

}