package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeStudentsShift extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(IUserView userView, Integer oldShiftId, Integer newShiftId, final Set<Registration> registrations)
	    throws FenixServiceException {

	final Shift oldShift = rootDomainObject.readShiftByOID(oldShiftId);
	final Shift newShift = rootDomainObject.readShiftByOID(newShiftId);

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

	final String subject = RenderUtils.getResourceString("SOP_RESOURCES", "changeStudentsShift.email.subject");
	final String groupName = RenderUtils.getResourceString("SOP_RESOURCES", "changeStudentsShift.email.groupName",
		new Object[] { oldShift.getNome() });
	final String messagePrefix = RenderUtils.getResourceString("SOP_RESOURCES", "changeStudentsShift.email.body",
		new Object[] { oldShift.getNome() });

	final String messagePosfix = newShift == null ? RenderUtils.getResourceString("SOP_RESOURCES",
		"changeStudentsShift.email.notNewShift") : RenderUtils.getResourceString("SOP_RESOURCES",
		"changeStudentsShift.email.newShift", new Object[] { oldShift.getNome() });
	final String message = messagePrefix + messagePosfix;

	Collection<ReplyTo> replyTos = new HashSet<ReplyTo>();
	Collection<Recipient> recipients = Collections.singletonList(new Recipient(groupName, new FixedSetGroup(recievers)));
	replyTos.add(new ConcreteReplyTo("gop@ist.utl.pt"));
	Sender sender = rootDomainObject.getSystemSender();
	new Message(sender, replyTos, recipients, subject, message, "");
    }

    public static class UnableToTransferStudentsException extends FenixServiceException {
    }

}