package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

@CustomGroupOperator("student")
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

    @CustomGroupArgument(index = 1)
    public static Argument<DegreeType> degreeTypeArgument() {
        return new SimpleArgument<DegreeType, PersistentStudentGroup>() {
            @Override
            public DegreeType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : DegreeType.valueOf(argument);
            }

            @Override
            public Class<? extends DegreeType> getType() {
                return DegreeType.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getDegreeType() != null ? group.getDegreeType().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentStudentGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @CustomGroupArgument(index = 3)
    public static Argument<CycleType> cycleArgument() {
        return new SimpleArgument<CycleType, PersistentStudentGroup>() {
            @Override
            public CycleType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : CycleType.valueOf(argument);
            }

            @Override
            public Class<? extends CycleType> getType() {
                return CycleType.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getCycle() != null ? group.getCycle().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 4)
    public static Argument<Campus> campusArgument() {
        return new SimpleArgument<Campus, PersistentStudentGroup>() {
            @Override
            public Campus parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Campus> getDomainObject(argument);
            }

            @Override
            public Class<? extends Campus> getType() {
                return Campus.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getCampus() != null ? group.getCampus().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 5)
    public static Argument<ExecutionCourse> executionCourseArgument() {
        return new SimpleArgument<ExecutionCourse, PersistentStudentGroup>() {
            @Override
            public ExecutionCourse parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<ExecutionCourse> getDomainObject(argument);
            }

            @Override
            public Class<? extends ExecutionCourse> getType() {
                return ExecutionCourse.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getExecutionCourse() != null ? group.getExecutionCourse().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 6)
    public static Argument<CurricularYear> curricularYearArgument() {
        return new SimpleArgument<CurricularYear, PersistentStudentGroup>() {
            @Override
            public CurricularYear parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : CurricularYear.readByYear(Integer.valueOf(argument));
            }

            @Override
            public Class<? extends CurricularYear> getType() {
                return CurricularYear.class;
            }

            @Override
            public String extract(PersistentStudentGroup group) {
                return group.getCurricularYear() != null ? group.getCurricularYear().getYear().toString() : "";
            }
        };
    }

    @CustomGroupArgument(index = 7)
    public static Argument<ExecutionYear> executionYearArgument() {
        return new SimpleArgument<ExecutionYear, PersistentStudentGroup>() {
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
            public String extract(PersistentStudentGroup group) {
                return group.getExecutionYear() != null ? group.getExecutionYear().getAcademicInterval()
                        .getResumedRepresentationInStringFormat() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        List<String> parts = new ArrayList<>();
        if (getDegreeType() != null) {
            parts.add(getDegreeType().getFilteredName());
        }
        if (getDegree() != null) {
            parts.add(getDegree().getNameI18N().getContent());
        }
        if (getCycle() != null) {
            parts.add(getCycle().getDescription());
        }
        if (getCampus() != null) {
            parts.add(getCampus().getName());
        }
        if (getExecutionCourse() != null) {
            parts.add(getExecutionCourse().getName());
        }
        if (getCurricularYear() != null) {
            parts.add(getCurricularYear().getYear().toString());
        }
        if (super.getExecutionYear() != null) {
            parts.add(super.getExecutionYear().getName());
        }
        return new String[] { Joiner.on(", ").join(parts) };
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return super.getExecutionYear() != null ? super.getExecutionYear() : ExecutionYear.readCurrentExecutionYear();
    }

    @Override
    public Set<User> getMembers() {
        if (getExecutionCourse() != null) {
            if (getDegree() == null && getDegreeType() == null && getCampus() == null) {
                return registrationsToUsers(getCourseBasedRegistrations(getExecutionCourse()));
            } else {
                return registrationsToUsers(Sets.intersection(getCourseBasedRegistrations(getExecutionCourse()),
                        getDegreeBasedRegistrations()));
            }
        } else if (getCampus() != null) {
            return registrationsToUsers(getCampusBasedRegistrations());
        } else {
            return registrationsToUsers(getDegreeBasedRegistrations());
        }
    }

    private Set<Registration> getDegreeBasedRegistrations() {
        FluentIterable<Registration> registrations;
        if (getDegreeType() != null) {
            registrations = getRegistrations(getDegreeType());
        } else if (getDegree() != null) {
            registrations = getRegistrations(getDegree());
        } else {
            registrations = getRegistrations();
        }
        registrations = filterDegreeType(registrations, getDegreeType());
        registrations = filterDegree(registrations, getDegree());
        registrations = filterCycle(registrations, getCycle(), getExecutionYear());
        registrations = filterCurricularYear(registrations, getCurricularYear(), getExecutionYear());
        return registrations.toSet();
    }

    private FluentIterable<Registration> filterDegreeType(FluentIterable<Registration> registrations, final DegreeType degreeType) {
        if (degreeType == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return registration.getDegreeType().equals(degreeType);
            }
        });
    }

    private FluentIterable<Registration> filterDegree(FluentIterable<Registration> registrations, final Degree degree) {
        if (degree == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                if (registration.getActiveStudentCurricularPlan() != null) {
                    return registration.getActiveStudentCurricularPlan().getDegree().equals(degree);
                }
                return false;
            }
        });
    }

    private static FluentIterable<Registration> filterCycle(FluentIterable<Registration> registrations,
            final CycleType cycleType, final ExecutionYear executionYear) {
        if (cycleType == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return Objects.equal(registration.getCycleType(executionYear), cycleType);
            }
        });
    }

    private static FluentIterable<Registration> filterCurricularYear(FluentIterable<Registration> registrations,
            final CurricularYear curricularYear, final ExecutionYear executionYear) {
        if (curricularYear == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return registration.getCurricularYear(executionYear) == curricularYear.getYear();
            }
        });
    }

    private static FluentIterable<Registration> getRegistrations(DegreeType type) {
        Set<Registration> registrations = new HashSet<>();
        for (Person person : Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersonsSet()) {
            Registration registration = person.getStudentByType(type);
            if (registration != null && registration.isActive()) {
                registrations.add(registration);
            }
        }
        return FluentIterable.from(registrations);
    }

    private static FluentIterable<Registration> getRegistrations(Degree degree) {
        Set<Registration> registrations = new HashSet<>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
            for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.isActive()) {
                    registrations.add(studentCurricularPlan.getRegistration());
                }
            }
        }
        return FluentIterable.from(registrations);
    }

    private static FluentIterable<Registration> getRegistrations() {
        Set<Registration> registrations = new HashSet<>();
        for (Person person : Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersonsSet()) {
            registrations.addAll(person.getStudent().getActiveRegistrations());
        }
        return FluentIterable.from(registrations);
    }

    private static Set<User> registrationsToUsers(Set<Registration> registrations) {
        Set<User> users = new HashSet<>();
        for (Registration registration : registrations) {
            User user = registration.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    private static Set<Registration> getCourseBasedRegistrations(ExecutionCourse executionCourse) {
        Set<Registration> registrations = new HashSet<>();
        for (Attends attends : executionCourse.getAttendsSet()) {
            registrations.add(attends.getRegistration());
        }
        return registrations;
    }

    private Set<Registration> getCampusBasedRegistrations() {
        Set<Registration> registrations = new HashSet<>();
        for (final ExecutionDegree executionDegree : ExecutionYear.readCurrentExecutionYear().getExecutionDegreesSet()) {
            if (executionDegree.getCampus().equals(getCampus())) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                    final Registration registration = studentCurricularPlan.getRegistration();
                    if (registration != null && registration.isActive()) {
                        registrations.add(registration);
                    }
                }
            }
        }
        return registrations;
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
        if (user.getPerson().getStudent() == null) {
            return false;
        }
        for (final Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
            if (registration.isActive()) {
                if (getDegreeType() != null && registration.getDegree().getDegreeType() != getDegreeType()) {
                    continue;
                }
                if (getDegree() != null && registration.getActiveStudentCurricularPlan() != null
                        && !registration.getActiveStudentCurricularPlan().getDegree().equals(getDegree())) {
                    continue;
                }
                if (getCycle() != null && registration.getCurrentCycleType() != getCycle()) {
                    continue;
                }
                if (getCampus() != null && registration.getCampus() != getCampus()) {
                    continue;
                }
                if (getExecutionCourse() != null
                        && !registration.getAttendingExecutionCoursesFor().contains(getExecutionCourse())) {
                    continue;
                }
                if (getCurricularYear() != null && registration.getCurricularYear() != getCurricularYear().getYear()) {
                    continue;
                }
                return true;
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
