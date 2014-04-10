package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.CoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class CoordinatorSender extends CoordinatorSender_Base {
    private Recipient createRecipient(Group group) {
        return new Recipient(null, group);
    }

    public CoordinatorSender(Degree degree) {
        super();
        setDegree(degree);
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(CoordinatorGroup.get(degree));
        Group current = CoordinatorGroup.get(degree);
        Group teachers = TeacherGroup.get(degree);
        Group students = StudentGroup.get(degree, null);
        for (CycleType cycleType : degree.getDegreeType().getCycleTypes()) {
            addRecipients(createRecipient(StudentGroup.get(degree, cycleType)));
        }
        addRecipients(createRecipient(current));
        addRecipients(createRecipient(teachers));
        addRecipients(createRecipient(students));
        addRecipients(createRecipient(RoleGroup.get(RoleType.TEACHER)));
        addRecipients(createRecipient(StudentGroup.get()));
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s: %s)", Unit.getInstitutionAcronym(), getDegree().getSigla(), "Coordenação");
    }

    @Override
    public void delete() {
        setDegree(null);
        super.delete();
    }

    @Atomic
    public static CoordinatorSender newInstance(Degree degree) {
        CoordinatorSender sender = degree.getSender();
        return sender == null ? new CoordinatorSender(degree) : sender;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}
