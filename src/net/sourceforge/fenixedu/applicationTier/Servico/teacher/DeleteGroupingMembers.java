/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembers extends Service {

    public boolean run(Integer executionCourseCode, Integer groupingCode, List<String> studentUsernames)
            throws FenixServiceException{

        final Grouping grouping = rootDomainObject.readGroupingByOID(groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidSituationServiceException();
        }

        for (final String studentUsername : (List<String>) studentUsernames) {
            Attends attend = grouping.getStudentAttend(studentUsername);
            removeAttendFromStudentGroups(grouping, attend);
            grouping.removeAttends(attend);
        }        
        return true;
    }

    private void removeAttendFromStudentGroups(final Grouping grouping, Attends attend) {
        if (attend != null) {
            for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
                studentGroup.removeAttends(attend);
            }
        }
    }
}