package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;

import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
    }

    public ExecutionCourseForum(MultiLanguageString name, MultiLanguageString description) {
        this();
        init(name, description);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (this.getExecutionCourse() != null) {
            getExecutionCourse().checkIfCanAddForum(name);
        }

        super.setName(name);
    }

    @Override
    public Group getReadersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getWritersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getAdminGroup() {
        return TeacherGroup.get(getExecutionCourse());
    }

    private Group getExecutionCourseMembersGroup() {
        ExecutionCourse executionCourse = getExecutionCourse();
        return TeacherGroup.get(executionCourse).or(StudentGroup.get(executionCourse));
    }
}
