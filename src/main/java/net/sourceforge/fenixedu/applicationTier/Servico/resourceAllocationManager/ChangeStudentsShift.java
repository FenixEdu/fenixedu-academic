package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeStudentsShift {

    @Atomic
    public static void run(User userView, String oldShiftId, String newShiftId, final Set<Registration> registrations)
            throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final Shift oldShift = FenixFramework.getDomainObject(oldShiftId);
        final Shift newShift = FenixFramework.getDomainObject(newShiftId);

        if (newShift != null) {
            if (oldShift == null || !oldShift.getTypes().containsAll(newShift.getTypes())
                    || !newShift.getTypes().containsAll(oldShift.getTypes())
                    || !oldShift.getExecutionCourse().equals(newShift.getExecutionCourse())) {
                throw new UnableToTransferStudentsException();
            }
        }

        final Set<Person> recievers = new HashSet<Person>();

        oldShift.getStudentsSet().removeAll(registrations);
        if (newShift != null) {
            newShift.getStudentsSet().addAll(registrations);
        }
        for (final Registration registration : registrations) {
            recievers.add(registration.getPerson());
        }

        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ResourceManagerResources");

        final String subject = bundle.getString("changeStudentsShift.email.subject");

        final String groupName =
                MessageFormat.format(bundle.getString("changeStudentsShift.email.groupName"), oldShift.getNome());

        final String messagePrefix = MessageFormat.format(bundle.getString("changeStudentsShift.email.body"), oldShift.getNome());

        final String messagePosfix =
                newShift == null ? bundle.getString("changeStudentsShift.email.body.notNewShift") : MessageFormat.format(
                        bundle.getString("changeStudentsShift.email.body.newShift"), oldShift.getNome());

        final String message = messagePrefix + messagePosfix;

        Recipient recipient = new Recipient(groupName, new FixedSetGroup(recievers));
        Sender sender = Bennu.getInstance().getSystemSender();
        String gopEmailAddress = Instalation.getInstance().getInstituitionalEmailAddress("gop");
        new Message(sender, new ConcreteReplyTo(gopEmailAddress).asCollection(), recipient.asCollection(), subject, message, "");
    }

    public static class UnableToTransferStudentsException extends FenixServiceException {
    }

}