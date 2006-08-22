/*
 * Created on 27/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EMail;

import org.apache.struts.util.MessageResources;

/**
 * @author asnr and scpo
 * 
 */
public class GroupStudentEnrolment extends Service {

    public String mailServer() {
        final String server = ResourceBundle.getBundle("SMTPConfiguration").getString("mail.smtp.host");
        return (server != null) ? server : "mail.adm";
    }

    private static final MessageResources messages = MessageResources.getMessageResources("resources/GlobalResources");

    public Boolean run(Integer studentGroupCode, String username) throws FenixServiceException,
            ExcepcaoPersistencia {
        
        final StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }
        final Registration registration = Registration.readByUsername(username);
        if (registration == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Grouping grouping = studentGroup.getGrouping();
        final Attends studentAttend = grouping.getStudentAttend(registration);
        if (studentAttend == null) {
            throw new NotAuthorizedException();
        }
        if (studentGroup.getAttends().contains(studentAttend)) {
            throw new InvalidSituationServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        boolean result = strategy.checkPossibleToEnrolInExistingGroup(grouping, studentGroup);
        if (!result) {
            throw new InvalidArgumentsServiceException();
        }

        checkIfStudentIsNotEnrolledInOtherGroups(grouping.getStudentGroups(), studentGroup,
                studentAttend);
        
        studentGroup.addAttends(studentAttend);      

        informStudents(studentGroup, registration, grouping);

        return Boolean.TRUE;
    }

    private void informStudents(final StudentGroup studentGroup, final Registration registration, final Grouping grouping) {
        final List<String> emails = new ArrayList<String>();
        for (final Attends attends : studentGroup.getAttends()) {
            emails.add(attends.getAluno().getPerson().getEmail());
        }

        final StringBuilder executionCourseNames = new StringBuilder();
        for (final ExecutionCourse executionCourse : grouping.getExecutionCourses()) {
            if (executionCourseNames.length() > 0) {
                executionCourseNames.append(", ");
            }
            executionCourseNames.append(executionCourse.getNome());
        }
        EMail.send(mailServer(), "Fenix System", messages.getMessage("suporte.mail"),
                messages.getMessage("message.subject.grouping.change"), emails, new ArrayList(), new ArrayList(),
                messages.getMessage("message.body.grouping.change.enrolment", registration.getNumber().toString(),
                        studentGroup.getGroupNumber().toString()));
    }

    private void checkIfStudentIsNotEnrolledInOtherGroups(final List<StudentGroup> studentGroups,
            final StudentGroup studentGroupEnrolled, final Attends studentAttend)
            throws InvalidSituationServiceException {
        
        for (final StudentGroup studentGroup : studentGroups) {
            if (studentGroup != studentGroupEnrolled
                    && studentGroup.getAttends().contains(studentAttend)) {
                throw new InvalidSituationServiceException();
            }
        }
    }
}