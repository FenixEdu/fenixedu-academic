/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 * 
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public boolean checkNumberOfGroups(IGrouping grouping, IShift shift) {

        if (grouping.getGroupMaximumNumber() == null) {
            return true;
        }
        int numberOfGroups = 0;
        if (shift != null) {
            numberOfGroups = grouping.readAllStudentGroupsBy(shift).size();
        } else {
            numberOfGroups = grouping.getStudentGroupsWithoutShift().size();
        }
        if (numberOfGroups < grouping.getGroupMaximumNumber()) {
            return true;
        }
        return false;
    }

    public boolean checkEnrolmentDate(IGrouping grouping, Calendar actualDate) {
        Long actualDateInMills = new Long(actualDate.getTimeInMillis());
        Long enrolmentBeginDayInMills = null;
        Long enrolmentEndDayInMills = null;

        if (grouping.getEnrolmentBeginDay() != null)
            enrolmentBeginDayInMills = new Long(grouping.getEnrolmentBeginDay().getTimeInMillis());

        if (grouping.getEnrolmentEndDay() != null)
            enrolmentEndDayInMills = new Long(grouping.getEnrolmentEndDay().getTimeInMillis());

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills == null)
            return true;

        if (enrolmentBeginDayInMills != null && enrolmentEndDayInMills == null) {
            if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0)
                return true;
        }

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills != null) {
            if (actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
                return true;
        }

        if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0
                && actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
            return true;

        return false;
    }

    public boolean checkShiftType(IGrouping grouping, IShift shift) {
        if (shift != null) {
            return shift.getTipo().equals(grouping.getShiftType());
        } else {
            return grouping.getShiftType() == null;
        }
    }

    public List checkShiftsType(IGrouping grouping, List shifts) {
        List result = new ArrayList();
        if (grouping.getShiftType() != null) {
            for (final IShift shift : (List<IShift>) shifts) {
                if (shift.getTipo().equals(grouping.getShiftType())) {
                    result.add(shift);
                }
            }
        }
        return result;
    }

    public boolean checkAlreadyEnroled(IGrouping grouping, String studentUsername) {

        final IAttends studentAttend = grouping.getStudentAttend(studentUsername);

        if (studentAttend != null) {
            List<IStudentGroup> groupingStudentGroups = grouping.getStudentGroups();
            for (final IStudentGroup studentGroup : groupingStudentGroups) {
                List<IAttends> studentGroupAttends = studentGroup.getAttends();
                for (final IAttends attend : studentGroupAttends) {
                    if (attend == studentAttend) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkNotEnroledInGroup(IGrouping grouping, IStudentGroup studentGroup,
            String studentUsername) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IStudent student = persistentSupport.getIPersistentStudent().readByUsername(
                studentUsername);
        final IAttends studentAttend = grouping.getStudentAttend(student);

        if (studentAttend != null) {
            List<IAttends> studentGroupAttends = studentGroup.getAttends();
            for (final IAttends attend : studentGroupAttends) {
                if (attend == studentAttend) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPossibleToEnrolInExistingGroup(IGrouping grouping, IStudentGroup studentGroup,
            IShift shift) {

        final int numberOfElements = studentGroup.getAttendsCount();
        final Integer maximumCapacity = grouping.getMaximumCapacity();
        if (maximumCapacity == null)
            return true;
        if (numberOfElements < maximumCapacity)
            return true;

        return false;
    }

    public boolean checkIfStudentGroupIsEmpty(IAttends attend, IStudentGroup studentGroup) {

        final List allStudentGroupAttends = studentGroup.getAttends();
        if (allStudentGroupAttends.size() == 1 && allStudentGroupAttends.contains(attend)) {
            return true;
        }
        return false;
    }

    public boolean checkStudentInGrouping(IGrouping grouping, String username)
            throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        final IStudent student = persistentStudent.readByUsername(username);
        final List<IStudent> groupingStudents = getGroupingStudents(grouping);

        return groupingStudents.contains(student);
    }

    public boolean checkStudentsInGrouping(List<Integer> studentIDs, IGrouping grouping)
            throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        final List<IStudent> groupingStudents = getGroupingStudents(grouping);
        for (final Integer studentID : studentIDs) {
            final IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
            if (groupingStudents.contains(student)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, IGrouping grouping) {
        for (final String studentUsername : studentUsernames) {
            if (grouping.getStudentAttend(studentUsername) == null) {
                return false;
            }
        }
        return true;
    }

    private List<IStudent> getGroupingStudents(final IGrouping grouping) {

        final List<IStudent> result = new ArrayList();
        for (final IAttends attend : grouping.getAttends()) {
            result.add(attend.getAluno());
        }
        return result;
    }

    public boolean checkHasShift(IGrouping grouping) {
        return grouping.getShiftType() != null;
    }

    public abstract Integer enrolmentPolicyNewGroup(IGrouping grouping, int numberOfStudentsToEnrole,
            IShift shift);

    public abstract boolean checkNumberOfGroupElements(IGrouping grouping, IStudentGroup studentGroup)
            throws ExcepcaoPersistencia;

}
