/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 * 
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public boolean checkNumberOfGroups(IGroupProperties groupProperties, IShift shift) {
        boolean result = false;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
            List groups = new ArrayList();
            if (shift != null) {
                groups = persistentStudentGroup.readAllStudentGroupByAttendsSetAndShift(groupProperties
                        .getAttendsSet().getIdInternal(), shift.getIdInternal());
            } else {
                groups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();
            }

            int numberOfGroups = groups.size();
            if (groupProperties.getGroupMaximumNumber() == null)
                return true;

            if (numberOfGroups < groupProperties.getGroupMaximumNumber().intValue()) {
                result = true;
            }
        } catch (ExcepcaoPersistencia e) {
        }
        return result;
    }

    public boolean checkEnrolmentDate(IGroupProperties groupProperties, Calendar actualDate) {
        Long actualDateInMills = new Long(actualDate.getTimeInMillis());
        Long enrolmentBeginDayInMills = null;
        Long enrolmentEndDayInMills = null;

        if (groupProperties.getEnrolmentBeginDay() != null)
            enrolmentBeginDayInMills = new Long(groupProperties.getEnrolmentBeginDay().getTimeInMillis());

        if (groupProperties.getEnrolmentEndDay() != null)
            enrolmentEndDayInMills = new Long(groupProperties.getEnrolmentEndDay().getTimeInMillis());

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

    public boolean checkShiftType(IGroupProperties groupProperties, IShift shift) {
        boolean result = false;
        if (shift != null) {
            if (shift.getTipo().equals(groupProperties.getShiftType()))
                result = true;
        } else {
            if (groupProperties.getShiftType() == null)
                return true;
        }

        return result;
    }

    public List checkShiftsType(IGroupProperties groupProperties, List shifts) {
        if (groupProperties.getShiftType() != null) {
            Iterator iterShift = shifts.iterator();
            IShift shift = null;
            List allShifts = new ArrayList();
            while (iterShift.hasNext()) {
                shift = (IShift) iterShift.next();
                if (shift.getTipo().equals(groupProperties.getShiftType()))
                    allShifts.add(shift);
            }
            return allShifts;
        }
        return null;
    }

    public boolean checkAlreadyEnroled(IGroupProperties groupProperties, String username) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            IStudent student = persistentStudent.readByUsername(username);

            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
            boolean found = false;
            IAttends attend = null;
            while (iterAttends.hasNext() && !found) {
                attend = (IAttends) iterAttends.next();
                if (attend.getAluno().equals(student)) {
                    found = true;
                } else {
                    attend = null;
                }
            }

            List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();
            Iterator iterStudentGroup = allStudentGroup.iterator();
            IStudentGroup group = null;
            List allStudentGroupAttendByGroup = new ArrayList();
            while (iterStudentGroup.hasNext()) {
                group = (IStudentGroup) iterStudentGroup.next();
                allStudentGroupAttendByGroup = group.getStudentGroupAttends();
                Iterator iterStudentGroupAttend = allStudentGroupAttendByGroup.iterator();
                IStudentGroupAttend studentGroupAttend = null;
                while (iterStudentGroupAttend.hasNext()) {
                    studentGroupAttend = (IStudentGroupAttend) iterStudentGroupAttend.next();
                    if (attend.equals(studentGroupAttend.getAttend()))
                        return true;
                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean checkNotEnroledInGroup(IGroupProperties groupProperties, IStudentGroup studentGroup,
            String username) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            IStudent student = persistentStudent.readByUsername(username);

            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
            boolean found = false;
            IAttends attend = null;
            while (iterAttends.hasNext() && !found) {
                attend = (IAttends) iterAttends.next();
                if (attend.getAluno().equals(student)) {
                    found = true;
                } else {
                    attend = null;
                }
            }

            List allStudentGroupAttendByGroup = studentGroup.getStudentGroupAttends();
            Iterator iterStudentGroupAttend = allStudentGroupAttendByGroup.iterator();
            IStudentGroupAttend studentGroupAttend = null;
            while (iterStudentGroupAttend.hasNext()) {
                studentGroupAttend = (IStudentGroupAttend) iterStudentGroupAttend.next();
                if (studentGroupAttend.getAttend().equals(attend))
                    return false;
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public boolean checkPossibleToEnrolInExistingGroup(IGroupProperties groupProperties,
            IStudentGroup studentGroup, IShift shift) {
        boolean result = false;

        List listStudentGroupAttend = studentGroup.getStudentGroupAttends();
        int nrOfElements = listStudentGroupAttend.size();
        Integer maximumCapacity = groupProperties.getMaximumCapacity();
        if (maximumCapacity == null)
            return true;
        if (nrOfElements < maximumCapacity.intValue())
            return true;

        return result;

    }

    public boolean checkIfStudentGroupIsEmpty(IStudentGroupAttend studentGroupAttend,
            IStudentGroup studentGroup) throws ExcepcaoPersistencia {

        boolean result = false;
        List allStudentGroupAttend = studentGroup.getStudentGroupAttends();

        if (allStudentGroupAttend.size() == 1 && allStudentGroupAttend.contains(studentGroupAttend))
            result = true;

        return result;
    }

    public boolean checkStudentInAttendsSet(IGroupProperties groupProperties, String username) {
        boolean found = false;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            IStudent student = persistentStudent.readByUsername(username);

            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();

            IAttends attend = null;
            while (iterAttends.hasNext() && !found) {
                attend = (IAttends) iterAttends.next();
                if (attend.getAluno().equals(student)) {
                    found = true;
                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return found;
    }

    public boolean checkStudentsInAttendsSet(List studentCodes, IGroupProperties groupProperties) {
        boolean found = true;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            List attends = groupProperties.getAttendsSet().getAttends();
            List students = new ArrayList();
            Iterator iterAttends = attends.iterator();
            while (iterAttends.hasNext()) {
                IAttends frequenta = (IAttends) iterAttends.next();
                students.add(frequenta.getAluno());
            }
            Iterator iterator = studentCodes.iterator();
            while (iterator.hasNext()) {
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class,
                        (Integer) iterator.next());
                if (!students.contains(student))
                    return false;
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return found;
    }

    public boolean checkStudentsUserNamesInAttendsSet(List studentUsernames,
            IGroupProperties groupProperties) {
        boolean found = true;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            List attends = groupProperties.getAttendsSet().getAttends();
            List students = new ArrayList();
            Iterator iterAttends = attends.iterator();
            while (iterAttends.hasNext()) {
                IAttends frequenta = (IAttends) iterAttends.next();
                students.add(frequenta.getAluno());
            }
            Iterator iterator = studentUsernames.iterator();
            while (iterator.hasNext()) {
                String userName = (String) iterator.next();
                IStudent student = persistentStudent.readByUsername(userName);
                if (!students.contains(student))
                    return false;
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return found;
    }

    public boolean checkHasShift(IGroupProperties groupProperties) {
        if (groupProperties.getShiftType() != null) {
            return true;
        }
        return false;
    }

    public abstract Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,
            int numberOfStudentsToEnrole, IShift shift);

    public abstract boolean checkNumberOfGroupElements(IGroupProperties groupProperties,
            IStudentGroup studentGroup) throws ExcepcaoPersistencia;

}
