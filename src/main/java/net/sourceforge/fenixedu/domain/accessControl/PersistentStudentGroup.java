package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class PersistentStudentGroup extends PersistentStudentGroup_Base {
    protected PersistentStudentGroup(DegreeType degreeType, Degree degree, CycleType cycle, Campus campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear) {
        super();
        setDegreeType(degreeType);
        setDegree(degree);
        setCycle(cycle);
        setCampus(campus);
        setExecutionCourse(executionCourse);
        setCurricularYear(curricularYear);
        setExecutionYear(executionYear);
        if (degree != null || executionCourse != null || campus != null || curricularYear != null && executionYear != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return StudentGroup.get(getDegreeType(), getDegree(), getCycle(), getCampus(), getExecutionCourse(), getCurricularYear(),
                getExecutionYear());
    }

    @Override
    protected void gc() {
        setCampus(null);
        setDegree(null);
        setExecutionCourse(null);
        setCurricularYear(null);
        setExecutionYear(null);
        super.gc();
    }

    public static PersistentStudentGroup getInstance() {
        return getInstance(filter(PersistentStudentGroup.class), null, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType) {
        return getInstance(filter(PersistentStudentGroup.class), degreeType, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(CycleType cycle) {
        return getInstance(filter(PersistentStudentGroup.class), null, null, cycle, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CycleType cycle) {
        return getInstance(FluentIterable.from(degree.getStudentGroupSet()), null, degree, cycle, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Campus campus) {
        return getInstance(FluentIterable.from(campus.getStudentGroupSet()), null, null, null, campus, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CurricularYear curricularYear, ExecutionYear executionYear) {
        return getInstance(FluentIterable.from(curricularYear.getStudentGroupSet()), null, degree, null, null, null,
                curricularYear, executionYear);
    }

    public static PersistentStudentGroup getInstance(ExecutionCourse executionCourse) {
        return getInstance(FluentIterable.from(executionCourse.getStudentGroupSet()), null, null, null, null, executionCourse,
                null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType, Degree degree, CycleType cycle, Campus campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear) {
        if (curricularYear != null) {
            return getInstance(FluentIterable.from(curricularYear.getStudentGroupSet()), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear);
        }
        if (executionCourse != null) {
            return getInstance(FluentIterable.from(executionCourse.getStudentGroupSet()), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear);
        }
        if (campus != null) {
            return getInstance(FluentIterable.from(campus.getStudentGroupSet()), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear);
        }
        if (degree != null) {
            return getInstance(FluentIterable.from(degree.getStudentGroupSet()), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear);
        }
        return getInstance(filter(PersistentStudentGroup.class), degreeType, degree, cycle, campus, executionCourse,
                curricularYear, executionYear);
    }

    private static PersistentStudentGroup getInstance(FluentIterable<PersistentStudentGroup> options, DegreeType degreeType,
            Degree degree, CycleType cycle, Campus campus, ExecutionCourse executionCourse, CurricularYear curricularYear,
            ExecutionYear executionYear) {
        Optional<PersistentStudentGroup> instance =
                select(options, degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear);
        return instance.isPresent() ? instance.get() : create(options, degreeType, degree, cycle, campus, executionCourse,
                curricularYear, executionYear);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentGroup create(FluentIterable<PersistentStudentGroup> options, DegreeType degreeType,
            Degree degree, CycleType cycle, Campus campus, ExecutionCourse executionCourse, CurricularYear curricularYear,
            ExecutionYear executionYear) {
        Optional<PersistentStudentGroup> instance =
                select(options, degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear);
        return instance.isPresent() ? instance.get() : new PersistentStudentGroup(degreeType, degree, cycle, campus,
                executionCourse, curricularYear, executionYear);
    }

    private static Optional<PersistentStudentGroup> select(FluentIterable<PersistentStudentGroup> options,
            final DegreeType degreeType, final Degree degree, final CycleType cycle, final Campus campus,
            final ExecutionCourse executionCourse, final CurricularYear curricularYear, final ExecutionYear executionYear) {
        return options.firstMatch(new Predicate<PersistentStudentGroup>() {
            @Override
            public boolean apply(PersistentStudentGroup group) {
                return Objects.equal(group.getDegreeType(), degreeType) && Objects.equal(group.getDegree(), degree)
                        && Objects.equal(group.getCycle(), cycle) && Objects.equal(group.getCampus(), campus)
                        && Objects.equal(group.getExecutionCourse(), executionCourse)
                        && Objects.equal(group.getCurricularYear(), curricularYear)
                        && Objects.equal(group.getExecutionYear(), executionYear);
            }
        });
    }
}
