package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

@CustomGroupOperator("teacher")
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

    @CustomGroupArgument(index = 1)
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentTeacherGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentTeacherGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<ExecutionCourse> executionCourseArgument() {
        return new SimpleArgument<ExecutionCourse, PersistentTeacherGroup>() {
            @Override
            public ExecutionCourse parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<ExecutionCourse> getDomainObject(argument);
            }

            @Override
            public Class<? extends ExecutionCourse> getType() {
                return ExecutionCourse.class;
            }

            @Override
            public String extract(PersistentTeacherGroup group) {
                return group.getExecutionCourse() != null ? group.getExecutionCourse().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 3)
    public static Argument<Campus> campusArgument() {
        return new SimpleArgument<Campus, PersistentTeacherGroup>() {
            @Override
            public Campus parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Campus> getDomainObject(argument);
            }

            @Override
            public Class<? extends Campus> getType() {
                return Campus.class;
            }

            @Override
            public String extract(PersistentTeacherGroup group) {
                return group.getCampus() != null ? group.getCampus().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 4)
    public static Argument<Department> departmentArgument() {
        return new SimpleArgument<Department, PersistentTeacherGroup>() {
            @Override
            public Department parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Department> getDomainObject(argument);
            }

            @Override
            public Class<? extends Department> getType() {
                return Department.class;
            }

            @Override
            public String extract(PersistentTeacherGroup group) {
                return group.getDepartment() != null ? group.getDepartment().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 5)
    public static Argument<ExecutionYear> executionYearArgument() {
        return new SimpleArgument<ExecutionYear, PersistentTeacherGroup>() {
            @Override
            public ExecutionYear parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : ExecutionYear.readByAcademicInterval(AcademicInterval
                        .getAcademicIntervalFromResumedString(argument));
            }

            @Override
            public Class<? extends ExecutionYear> getType() {
                return ExecutionYear.class;
            }

            @Override
            public String extract(PersistentTeacherGroup group) {
                return group.getExecutionYear() != null ? group.getExecutionYear().getAcademicInterval()
                        .getResumedRepresentationInStringFormat() : "";
            }
        };
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return super.getExecutionYear() != null ? super.getExecutionYear() : ExecutionYear.readCurrentExecutionYear();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (getDegree() != null) {
            return new String[] { getDegree().getName() };
        } else if (getCampus() != null) {
            return new String[] { getCampus().getName() };
        } else if (getDepartment() != null) {
            return new String[] { getDepartment().getName() };
        } else if (getExecutionCourse() != null) {
            return new String[] { getExecutionCourse().getName() };
        }
        return new String[0];
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        //by degree
        if (getDegree() != null) {
            for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
                for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
                    for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionYear().isCurrent()) {
                            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                                User user = professorship.getPerson().getUser();
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
            }
        }
        //by campus
        if (getCampus() != null) {
            for (Person person : Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersonsSet()) {
                if (person.getTeacher().teachesAt(getCampus())) {
                    User user = person.getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        //by department
        if (getDepartment() != null) {
            for (Teacher teacher : getDepartment().getAllTeachers(getExecutionYear().getBeginDateYearMonthDay(),
                    getExecutionYear().getEndDateYearMonthDay())) {
                User user = teacher.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        //by execution course
        if (getExecutionCourse() != null) {
            for (Professorship professorship : getExecutionCourse().getProfessorshipsSet()) {
                User user = professorship.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
            // also include coordinators, this is hidden from the documented semantic of the group and should be taken away in the future
            for (ExecutionDegree executionDegree : getExecutionCourse().getExecutionDegrees()) {
                for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    User user = coordinator.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        if (user.getPerson().getTeacher() != null) {
            for (final Professorship professorship : user.getPerson().getTeacher().getProfessorshipsSet()) {
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                if (getDegree() != null) {
                    for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                        if (curricularCourse.getDegree().equals(getDegree())) {
                            return true;
                        }
                    }
                }
                if (getExecutionCourse() != null) {
                    if (executionCourse.equals(getExecutionCourse())) {
                        return true;
                    }
                }
                if (getCampus() != null) {
                    if (executionCourse.functionsAt(getCampus())) {
                        return true;
                    }
                }
            }
            if (getDepartment() != null) {
                if (getDepartment().equals(
                        user.getPerson()
                                .getTeacher()
                                .getLastWorkingDepartment(getExecutionYear().getBeginDateYearMonthDay(),
                                        getExecutionYear().getEndDateYearMonthDay()))) {
                    return true;
                }
            }
        }
        if (getExecutionCourse() != null) {
            // also include coordinators, this is hidden from the documented semantic of the group and should be taken away in the future
            if (!user.getPerson().getCoordinatorsSet().isEmpty()) {
                Set<ExecutionDegree> degrees = getExecutionCourse().getExecutionDegrees();
                for (Coordinator coordinator : user.getPerson().getCoordinatorsSet()) {
                    if (degrees.contains(coordinator.getExecutionDegree())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
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
