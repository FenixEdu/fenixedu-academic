/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.beanutils.BeanComparator;

import dml.runtime.RelationAdapter;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    public static final Comparator<StudentGroup> COMPARATOR_BY_GROUP_NUMBER = new BeanComparator("groupNumber");
    static {
        StudentGroupAttend.addListener(new StudentGroupAttendListener());
    }

    private static class StudentGroupAttendListener extends RelationAdapter<StudentGroup, Attends> {
        @Override
        public void beforeRemove(StudentGroup studentGroup, Attends attends) {
            if (!studentGroup.getProjectSubmissions().isEmpty()
                    && !studentGroup.getGrouping().isPersonTeacher(AccessControl.getPerson())) {
                throw new DomainException("error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions");
            }

            super.beforeRemove(studentGroup, attends);
        }

    }

    public boolean wasDeleted() {
        return !this.getValid();
    }

    public StudentGroup() {
        super();
        super.setValid(true);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentGroup(Integer groupNumber, Grouping grouping) {
        this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
    }

    public StudentGroup(Integer groupNumber, Grouping grouping, Shift shift) {
        this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
        super.setShift(shift);
    }

    public void delete() {
        List<ExecutionCourse> ecs = getGrouping().getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.group.removed", getGroupNumber().toString(), getGrouping()
                            .getName(), ec.getNome(), ec.getDegreePresentationString());

        }
        // teacher type of deletion after project submission
        if (hasAnyProjectSubmissions() && this.getGrouping().isPersonTeacher(AccessControl.getPerson())) {
            this.setValid(false);
        } else if (!hasAnyProjectSubmissions() && !hasAnyAttends()) {
            setShift(null);
            setGrouping(null);
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("student.group.cannot.be.deleted");
        }
    }

    public void editShift(Shift shift) {
        if (this.getGrouping().getShiftType() == null || (!shift.containsType(this.getGrouping().getShiftType()))) {
            throw new DomainException(this.getClass().getName(), "");
        }
        this.setShift(shift);
    }

    public boolean isPersonInStudentGroup(Person person) {

        for (Attends attend : getAttends()) {
            if (attend.getRegistration().getStudent().getPerson().equals(person)) {
                return true;
            }
        }
        return false;
    }
}
