package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.BundleUtil;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class NotifyStudentGroup {

    @Atomic
    public static void run(ProjectSubmission submission, ExecutionCourse course, Person person) {
        check(RolePredicates.TEACHER_PREDICATE);

        Set<Person> recievers = new HashSet<Person>();

        for (Attends attend : submission.getStudentGroup().getAttends()) {
            recievers.add(attend.getRegistration().getStudent().getPerson());
        }

        final String groupName =
                BundleUtil.getStringFromResourceBundle("resources.GlobalResources", "label.group", new String[] { submission
                        .getStudentGroup().getGroupNumber().toString() });
        Sender sender = ExecutionCourseSender.newInstance(course);
        Recipient recipient = new Recipient(groupName, new FixedSetGroup(recievers));
        new Message(sender, sender.getConcreteReplyTos(), recipient.asCollection(), submission.getProject().getName(),
                submission.getTeacherObservation(), "");
    }
}