/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */
public class GroupEnrolment extends Service {

    public boolean run(Integer groupingID, Integer shiftID, Integer groupNumber, List studentUsernames,
            String studentUsername) throws FenixServiceException, ExcepcaoPersistencia {
        final Grouping grouping = rootDomainObject.readGroupingByOID(groupingID);
        if (grouping == null) {
            throw new NonExistingServiceException();
        }

        final Registration userStudent = Registration.readByUsername(studentUsername);

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (grouping.getStudentAttend(studentUsername) == null) {
            throw new NoChangeMadeServiceException();
        }
        Shift shift = null;
        if (shiftID != null) {
            shift = rootDomainObject.readShiftByOID(shiftID);
        }
        Integer result = strategy.enrolmentPolicyNewGroup(grouping, studentUsernames.size() + 1, shift);

        if (result.equals(Integer.valueOf(-1))) {
            throw new InvalidArgumentsServiceException();
        }
        if (result.equals(Integer.valueOf(-2))) {
            throw new NonValidChangeServiceException();
        }
        if (result.equals(Integer.valueOf(-3))) {
            throw new NotAuthorizedException();
        }

        final Attends userAttend = grouping.getStudentAttend(userStudent);
        if (userAttend == null) {
            throw new InvalidStudentNumberServiceException();
        }
        if (strategy.checkAlreadyEnroled(grouping, studentUsername)) {
            throw new InvalidSituationServiceException();
        }

        StudentGroup newStudentGroup = grouping.readStudentGroupBy(groupNumber);
        if (newStudentGroup != null) {
            throw new FenixServiceException();
        }

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidStudentNumberServiceException();
        }
        checkStudentUsernamesAlreadyEnroledInStudentGroup(strategy, studentUsernames, grouping);

        newStudentGroup = new StudentGroup(groupNumber, grouping, shift);
        for (final String studentUsernameIter : (List<String>) studentUsernames) {
            Attends attend = grouping.getStudentAttend(studentUsernameIter);
            attend.addStudentGroups(newStudentGroup);
        }
        userAttend.addStudentGroups(newStudentGroup);
        grouping.addStudentGroups(newStudentGroup);

        return true;
    }

    private void checkStudentUsernamesAlreadyEnroledInStudentGroup(
            final IGroupEnrolmentStrategy strategy, final List<String> studentUsernames,
            final Grouping grouping) throws FenixServiceException {

        for (final String studentUsername : studentUsernames) {
            if (strategy.checkAlreadyEnroled(grouping, studentUsername)) {
                throw new InvalidSituationServiceException();
            }
        }
    }
}