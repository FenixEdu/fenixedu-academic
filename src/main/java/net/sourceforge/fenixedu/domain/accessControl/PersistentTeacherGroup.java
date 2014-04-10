package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class PersistentTeacherGroup extends PersistentTeacherGroup_Base {
    protected PersistentTeacherGroup(Degree degree, ExecutionCourse executionCourse, Campus campus, Department department,
            ExecutionYear executionYear) {
        super();
        setDegree(degree);
        setExecutionCourse(executionCourse);
        setCampus(campus);
        setDepartment(department);
        setExecutionYear(executionYear);
    }

    @Override
    public Group toGroup() {
        return TeacherGroup.get(getDegree(), getExecutionCourse(), getCampus(), getDepartment(), getExecutionYear());
    }

    @Override
    protected void gc() {
        setDegree(null);
        setExecutionCourse(null);
        setCampus(null);
        setDepartment(null);
        setExecutionYear(null);
        super.gc();
    }

    public static PersistentTeacherGroup getInstance(Degree degree) {
        return getInstance(FluentIterable.from(degree.getTeacherGroupSet()), degree, null, null, null, null);
    }

    public static PersistentTeacherGroup getInstance(Campus campus) {
        return getInstance(FluentIterable.from(campus.getTeacherGroupSet()), null, null, campus, null, null);
    }

    public static PersistentTeacherGroup getInstance(Department department, ExecutionYear executionYear) {
        return getInstance(FluentIterable.from(department.getTeacherGroupSet()), null, null, null, department, executionYear);
    }

    public static PersistentTeacherGroup getInstance(ExecutionCourse executionCourse) {
        return getInstance(FluentIterable.from(executionCourse.getTeacherGroupSet()), null, executionCourse, null, null, null);
    }

    public static PersistentTeacherGroup getInstance(Degree degree, ExecutionCourse executionCourse, Campus campus,
            Department department, ExecutionYear executionYear) {
        if (degree != null) {
            return getInstance(degree);
        }
        if (campus != null) {
            return getInstance(campus);
        }
        if (executionCourse != null) {
            return getInstance(executionCourse);
        }
        if (department != null) {
            return getInstance(department, executionYear);
        }
        return null;
    }

    private static PersistentTeacherGroup getInstance(FluentIterable<PersistentTeacherGroup> options, Degree degree,
            ExecutionCourse executionCourse, Campus campus, Department department, ExecutionYear executionYear) {
        Optional<PersistentTeacherGroup> instance = select(options, degree, executionCourse, campus, department, executionYear);
        return instance.isPresent() ? instance.get() : create(options, degree, executionCourse, campus, department, executionYear);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentTeacherGroup create(FluentIterable<PersistentTeacherGroup> options, Degree degree,
            ExecutionCourse executionCourse, Campus campus, Department department, ExecutionYear executionYear) {
        Optional<PersistentTeacherGroup> instance = select(options, degree, executionCourse, campus, department, executionYear);
        return instance.isPresent() ? instance.get() : new PersistentTeacherGroup(degree, executionCourse, campus, department,
                executionYear);
    }

    private static Optional<PersistentTeacherGroup> select(FluentIterable<PersistentTeacherGroup> options, final Degree degree,
            final ExecutionCourse executionCourse, final Campus campus, final Department department,
            final ExecutionYear executionYear) {
        return options.firstMatch(new Predicate<PersistentTeacherGroup>() {
            @Override
            public boolean apply(PersistentTeacherGroup group) {
                return Objects.equal(group.getDegree(), degree) && Objects.equal(group.getExecutionCourse(), executionCourse)
                        && Objects.equal(group.getCampus(), campus) && Objects.equal(group.getDepartment(), department)
                        && Objects.equal(group.getExecutionYear(), executionYear);
            }
        });
    }
}
