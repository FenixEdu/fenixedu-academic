package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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

public class UnEnrollStudentInGroup extends Service {

    public String mailServer() {
        final String server = ResourceBundle.getBundle("SMTPConfiguration").getString("mail.smtp.host");
        return (server != null) ? server : "mail.adm";
    }

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/GlobalResources");

    public Boolean run(String userName, Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

        final List<String> emails = new ArrayList<String>();
        for (final Attends attends : studentGroup.getAttends()) {
            emails.add(attends.getRegistration().getPerson().getEmail());
        }

        if (studentGroup == null) {
            throw new InvalidSituationServiceException();
        }

        Registration registration = Registration.readByUsername(userName);

        Grouping groupProperties = studentGroup.getGrouping();

        Attends attend = groupProperties.getStudentAttend(registration);

        if (attend == null) {
            throw new NotAuthorizedException();
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();

        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

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
        EMail.send(
                mailServer(), 
                "Fenix System", 
                messages.getMessage("suporte.mail"), 
                messages.getMessage("message.subject.grouping.change"), 
                emails, 
                new ArrayList(),
                new ArrayList(), 
                messages.getMessage("message.body.grouping.change.unenrolment", 
                registration.getNumber().toString(), studentGroup.getGroupNumber().toString()));

        return Boolean.TRUE;
    }

}