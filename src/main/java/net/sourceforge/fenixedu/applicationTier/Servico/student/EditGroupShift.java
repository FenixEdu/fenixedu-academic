/*
 * Created on 07/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.struts.util.MessageResources;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class EditGroupShift {

    private static final MessageResources messages = MessageResources.getMessageResources("resources/GlobalResources");

    @Atomic
    public static Boolean run(String studentGroupID, String groupingID, String newShiftID, String username)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        ServiceMonitoring.logService(EditGroupShift.class, studentGroupID, groupingID, newShiftID, username);

        final Grouping grouping = FenixFramework.getDomainObject(groupingID);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Shift shift = FenixFramework.getDomainObject(newShiftID);
        if (grouping.getShiftType() == null || !shift.containsType(grouping.getShiftType())) {
            throw new InvalidStudentNumberServiceException();
        }

        final Registration registration = Registration.readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentInGrouping(grouping, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(registration, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        boolean result = strategy.checkNumberOfGroups(grouping, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        studentGroup.setShift(shift);

        informStudents(studentGroup, registration, grouping);

        return true;
    }

    private static boolean checkStudentInStudentGroup(Registration registration, StudentGroup studentGroup)
            throws FenixServiceException {
        boolean found = false;
        Collection studentGroupAttends = studentGroup.getAttends();
        Attends attend = null;
        Iterator iterStudentGroupAttends = studentGroupAttends.iterator();
        while (iterStudentGroupAttends.hasNext() && !found) {
            attend = ((Attends) iterStudentGroupAttends.next());
            if (attend.getRegistration().equals(registration)) {
                found = true;
            }
        }
        return found;
    }

    private static void informStudents(final StudentGroup studentGroup, final Registration registration, final Grouping grouping) {
        final Set<Person> recievers = new HashSet<Person>();
        for (final Attends attends : studentGroup.getAttends()) {
            recievers.add(attends.getRegistration().getPerson());
        }

        final StringBuilder executionCourseNames = new StringBuilder();
        for (final ExecutionCourse executionCourse : grouping.getExecutionCourses()) {
            if (executionCourseNames.length() > 0) {
                executionCourseNames.append(", ");
            }
            executionCourseNames.append(executionCourse.getNome());
        }
        final String message =
                messages.getMessage("message.body.grouping.change.shift", registration.getNumber().toString(), studentGroup
                        .getGroupNumber().toString(), executionCourseNames.toString());
        final String groupName = messages.getMessage("message.group.name", studentGroup.getGroupNumber());
        final Collection<Recipient> recipients =
                Collections.singletonList(new Recipient(groupName, new FixedSetGroup(recievers)));

        SystemSender systemSender = Bennu.getInstance().getSystemSender();
        new Message(systemSender, systemSender.getConcreteReplyTos(), recipients,
                messages.getMessage("message.subject.grouping.change"), message, "");
    }
}