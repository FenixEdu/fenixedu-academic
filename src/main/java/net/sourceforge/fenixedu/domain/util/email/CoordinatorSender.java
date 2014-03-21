package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeAllCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsCycleGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
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
        setMembers(new DegreeAllCoordinatorsGroup(degree));
        Group current = new CurrentDegreeCoordinatorsGroup(degree);
        Group teachers = new DegreeTeachersGroup(degree);
        Group students = new DegreeStudentsGroup(degree);
        for (CycleType cycleType : degree.getDegreeType().getCycleTypes()) {
            Group studentsCycle = new DegreeStudentsCycleGroup(degree, cycleType);
            addRecipients(createRecipient(studentsCycle));
        }
        addRecipients(createRecipient(current));
        addRecipients(createRecipient(teachers));
        addRecipients(createRecipient(students));
        addRecipients(createRecipient(new AllTeachersGroup()));
        addRecipients(createRecipient(new AllStudentsGroup()));
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
