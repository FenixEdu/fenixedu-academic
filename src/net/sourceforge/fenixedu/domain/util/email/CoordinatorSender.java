package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeAllCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.ist.fenixWebFramework.services.Service;

public class CoordinatorSender extends CoordinatorSender_Base {
    private Recipient createRecipient(Group group) {
	return new Recipient(null, group);
    }

    public CoordinatorSender(Degree degree) {
	super();
	setDegree(degree);
	setFromAddress("noreply@ist.utl.pt");
	addReplyTos(new CurrentUserReplyTo());
	setMembers(new DegreeAllCoordinatorsGroup(degree));
	setFromName(getMembers().getName());
	Group current = new CurrentDegreeCoordinatorsGroup(degree);
	Group teachers = new DegreeTeachersGroup(degree);
	Group students = new DegreeStudentsGroup(degree);
	addRecipients(createRecipient(current));
	addRecipients(createRecipient(teachers));
	addRecipients(createRecipient(students));
	addRecipients(createRecipient(new AllTeachersGroup()));
	addRecipients(createRecipient(new AllStudentsGroup()));
    }

    @Service
    public static CoordinatorSender newInstance(Degree degree) {
	CoordinatorSender sender = degree.getSender();
	return sender == null ? new CoordinatorSender(degree) : sender;
    }
}
