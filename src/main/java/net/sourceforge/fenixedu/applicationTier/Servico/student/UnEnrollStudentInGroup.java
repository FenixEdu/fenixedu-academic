/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.util.MessageResources;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class UnEnrollStudentInGroup {

    public static String mailServer() {
        final String server = FenixConfigurationManager.getConfiguration().getMailSmtpHost();
        return (server != null) ? server : "mail.adm";
    }

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.GLOBAL);

    @Atomic
    public static Boolean run(String userName, String studentGroupCode) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        ServiceMonitoring.logService(UnEnrollStudentInGroup.class, userName, studentGroupCode);

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new InvalidSituationServiceException();
        }

        final List<String> emails = new ArrayList<String>();
        final Collection<Person> people = new ArrayList<Person>();
        for (final Attends attends : studentGroup.getAttends()) {
            final Person person = attends.getRegistration().getPerson();
            people.add(person);
        }
        final Group fixedSetGroup = UserGroup.of(Person.convertToUsers(people));
        final Recipient recipient = new Recipient("", fixedSetGroup);
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);

        Registration registration = Registration.readByUsername(userName);

        Grouping groupProperties = studentGroup.getGrouping();

        Attends attend = groupProperties.getStudentAttend(registration);

        if (attend == null) {
            throw new NotAuthorizedException();
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();

        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        boolean resultEmpty = strategy.checkIfStudentGroupIsEmpty(attend, studentGroup);

        studentGroup.removeAttends(attend);

        if (resultEmpty) {
            studentGroup.delete();
            return Boolean.FALSE;
        }

        final StringBuilder executionCourseNames = new StringBuilder();
        for (final ExecutionCourse executionCourse : groupProperties.getExecutionCourses()) {
            if (executionCourseNames.length() > 0) {
                executionCourseNames.append(", ");
            }
            executionCourseNames.append(executionCourse.getNome());
        }

        final String message =
                messages.getMessage("message.body.grouping.change.unenrolment", registration.getNumber().toString(), studentGroup
                        .getGroupNumber().toString(), attend.getExecutionCourse().getNome());

        SystemSender systemSender = Bennu.getInstance().getSystemSender();
        new Message(systemSender, systemSender.getConcreteReplyTos(), recipients,
                messages.getMessage("message.subject.grouping.change"), message, "");

        return Boolean.TRUE;
    }

}