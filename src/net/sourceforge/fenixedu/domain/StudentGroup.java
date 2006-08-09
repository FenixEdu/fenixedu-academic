/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    static {
        StudentGroupAttend.addListener(new StudentGroupAttendListener());
    }

    private static class StudentGroupAttendListener extends RelationAdapter<StudentGroup, Attends> {
        @Override
        public void beforeRemove(StudentGroup studentGroup, Attends attends) {
            if (!studentGroup.getProjectSubmissions().isEmpty()) {
                throw new DomainException(
                        "error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions");
            }

            super.beforeRemove(studentGroup, attends);
        }

    }

    public StudentGroup() {
        super();
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
        if (!hasAnyAttends() && !hasAnyProjectSubmissions()) {
            removeShift();
            removeGrouping();
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("student.group.cannot.be.deleted");
        }
    }

    public void editShift(Shift shift) {
        if (this.getGrouping().getShiftType() == null
                || (!this.getGrouping().getShiftType().equals(shift.getTipo()))) {
            throw new DomainException(this.getClass().getName(), "");
        }

        this.setShift(shift);
    }

    public boolean checkStudentUsernames(List<String> studentUsernames) {
        boolean found;
        for (final String studentUsername : studentUsernames) {
            found = false;
            for (final Attends attend : this.getAttends()) {
                if (attend.getAluno().getPerson().getUsername() == studentUsername) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return true;
            }
        }
        return false;
    }

}
