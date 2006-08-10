/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
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
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa and rmalo
 * 
 */

public class UnEnrollGroupShift extends Service {

    public boolean run(Integer studentGroupCode, Integer groupPropertiesCode, String username)
            throws FenixServiceException, ExcepcaoPersistencia {
        Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

        if (studentGroup == null)
            throw new InvalidArgumentsServiceException();

        if (!(studentGroup.getShift() != null && groupProperties.getShiftType() == null)
                || studentGroup.getShift() == null) {
            throw new InvalidStudentNumberServiceException();
        }

        Registration student = Registration.readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(student, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        Shift shift = null;
        boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        studentGroup.setShift(shift);

        return true;
    }

    private boolean checkStudentInStudentGroup(Registration student, StudentGroup studentGroup)
            throws ExcepcaoPersistencia {
        boolean found = false;

        List studentGroupAttends = studentGroup.getAttends();
        Attends attend = null;
        Iterator iterStudentGroupAttends = studentGroupAttends.iterator();
        while (iterStudentGroupAttends.hasNext() && !found) {
            attend = ((Attends) iterStudentGroupAttends.next());
            if (attend.getAluno().equals(student)) {
                found = true;
            }
        }
        return found;
    }

}