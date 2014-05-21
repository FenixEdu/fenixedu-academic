package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.StudentGroup;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentStudentGroupGroup extends PersistentStudentGroupGroup_Base {
    protected PersistentStudentGroupGroup(StudentGroup studentGroup) {
        super();
        setStudentGroup(studentGroup);
    }

    @Override
    public Group toGroup() {
        return StudentGroupGroup.get(getStudentGroup());
    }

    @Override
    protected void gc() {
        setStudentGroup(null);
        super.gc();
    }

    public static PersistentStudentGroupGroup getInstance(final StudentGroup studentGroup) {
        return singleton(() -> Optional.ofNullable(studentGroup.getStudentGroupGroup()), () -> new PersistentStudentGroupGroup(
                studentGroup));
    }
}
