/*
 * Created on 23/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */

public class InsertStudentGroupMembers extends Service {

    public Boolean run(Integer executionCourseID, Integer studentGroupID, Integer groupPropertiesID,
            List studentUsernames) throws FenixServiceException, ExcepcaoPersistencia {

        final StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
                StudentGroup.class, studentGroupID);
        if (studentGroup == null) {
            throw new ExistingServiceException();
        }

        final Grouping grouping = studentGroup.getGrouping();
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidArgumentsServiceException("error.editStudentGroupMembers");
        }

        for (final String studentUsername : (List<String>) studentUsernames) {
            if (strategy.checkAlreadyEnroled(grouping, studentUsername))
                throw new InvalidSituationServiceException();
            final Attends attend = grouping.getStudentAttend(studentUsername);
            studentGroup.addAttends(attend);
        }
        return Boolean.TRUE;
    }
}