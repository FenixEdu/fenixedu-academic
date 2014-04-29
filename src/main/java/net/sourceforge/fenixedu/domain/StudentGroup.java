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
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    public static final Comparator<StudentGroup> COMPARATOR_BY_GROUP_NUMBER = new BeanComparator("groupNumber");
    static {
        getRelationStudentGroupAttend().addListener(new StudentGroupAttendListener());
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
        setRootDomainObject(Bennu.getInstance());
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
        if (getStudentGroupGroup() != null) {
            throw new DomainException("error.studentGroup.cannotDeleteStudentGroupUsedInAccessControl");
        }
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmission> getProjectSubmissions() {
        return getProjectSubmissionsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissions() {
        return !getProjectSubmissionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmissionLog> getProjectSubmissionLogs() {
        return getProjectSubmissionLogsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissionLogs() {
        return !getProjectSubmissionLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAttends() {
        return getAttendsSet();
    }

    @Deprecated
    public boolean hasAnyAttends() {
        return !getAttendsSet().isEmpty();
    }

    @Deprecated
    public boolean hasGroupNumber() {
        return getGroupNumber() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

    @Deprecated
    public boolean hasValid() {
        return getValid() != null;
    }

    @Deprecated
    public boolean hasGrouping() {
        return getGrouping() != null;
    }

}
