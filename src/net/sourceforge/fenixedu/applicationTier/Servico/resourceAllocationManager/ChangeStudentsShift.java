package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeStudentsShift {

    static private RootDomainObject getRootDomainObject() {
        return RootDomainObject.getInstance();
    }

    @Service
    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    public static void run(IUserView userView, Integer oldShiftId, Integer newShiftId, final Set<Registration> registrations)
            throws FenixServiceException {

        final Shift oldShift = getRootDomainObject().readShiftByOID(oldShiftId);
        final Shift newShift = getRootDomainObject().readShiftByOID(newShiftId);

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
        Sender sender = getRootDomainObject().getSystemSender();
        new Message(sender, new ConcreteReplyTo("gop@ist.utl.pt").asCollection(), recipient.asCollection(), subject, message, "");
    }

    public static class UnableToTransferStudentsException extends FenixServiceException {
    }

}