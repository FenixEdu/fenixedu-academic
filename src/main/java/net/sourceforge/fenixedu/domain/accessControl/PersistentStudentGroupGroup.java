package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.StudentGroup;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentStudentGroupGroup instance = studentGroup.getStudentGroupGroup();
        return instance != null ? instance : create(studentGroup);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentGroupGroup create(final StudentGroup studentGroup) {
        PersistentStudentGroupGroup instance = studentGroup.getStudentGroupGroup();
        return instance != null ? instance : new PersistentStudentGroupGroup(studentGroup);
    }
}
