package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;

@CustomGroupOperator("professorship")
public class PersistentProfessorshipsGroup extends PersistentProfessorshipsGroup_Base {

    public PersistentProfessorshipsGroup(Boolean externalAuthorizations, AcademicPeriod period) {
        super();
        setExternalAuthorizations(externalAuthorizations);
        setOnCurrentPeriod(period);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Boolean> externalAuthorizationsArgument() {
        return new SimpleArgument<Boolean, PersistentProfessorshipsGroup>() {
            @Override
            public Boolean parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? Boolean.FALSE : Boolean.valueOf(argument);
            }

            @Override
            public Class<? extends Boolean> getType() {
                return Boolean.class;
            }

            @Override
            public String extract(PersistentProfessorshipsGroup group) {
                return group.getExternalAuthorizations().toString();
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<AcademicPeriod> periodArgument() {
        return new SimpleArgument<AcademicPeriod, PersistentProfessorshipsGroup>() {
            @Override
            public AcademicPeriod parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : AcademicPeriod.getAcademicPeriodFromString(argument);
            }

            @Override
            public Class<? extends AcademicPeriod> getType() {
                return AcademicPeriod.class;
            }

            @Override
            public String extract(PersistentProfessorshipsGroup group) {
                return group.getOnCurrentPeriod().getRepresentationInStringFormat();
            }
        };
    }

    @Override
    public String getPresentationNameKey() {
        if (getExternalAuthorizations()) {
            return super.getPresentationNameKey() + ".external";
        }
        return super.getPresentationNameKey();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getOnCurrentPeriod().getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        //TODO: select active 'when'
        ExecutionInterval interval =
                ExecutionInterval.getExecutionInterval(AcademicInterval.readDefaultAcademicInterval(getOnCurrentPeriod()));
        if (interval instanceof ExecutionSemester) {
            ExecutionSemester semester = (ExecutionSemester) interval;
            fillMembers(users, semester);
        } else if (interval instanceof ExecutionYear) {
            for (ExecutionSemester semester : ((ExecutionYear) interval).getExecutionPeriodsSet()) {
                fillMembers(users, semester);
            }
        }
        return users;
    }

    private void fillMembers(Set<User> users, ExecutionSemester semester) {
        if (getExternalAuthorizations()) {
            for (TeacherAuthorization authorization : semester.getAuthorizationSet()) {
                if (authorization instanceof ExternalTeacherAuthorization) {
                    User user = authorization.getTeacher().getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } else {
            for (final ExecutionCourse executionCourse : semester.getAssociatedExecutionCoursesSet()) {
                for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                    User user = professorship.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, DateTime.now());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        //TODO: select active 'when'
        AcademicInterval interval = AcademicInterval.readDefaultAcademicInterval(getOnCurrentPeriod());
        if (getExternalAuthorizations()) {
            for (ExternalTeacherAuthorization authorization : user.getPerson().getTeacherAuthorizationsAuthorizedSet()) {
                final ExternalTeacherAuthorization externalAuthorization = authorization;
                if (interval.contains(externalAuthorization.getExecutionSemester().getAcademicInterval())) {
                    return true;
                }
            }
        } else {
            for (final Professorship professorship : user.getPerson().getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                if (interval.contains(executionCourse.getAcademicInterval())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentProfessorshipsGroup getInstance(Boolean externalAuthorization, AcademicPeriod period) {
        Optional<PersistentProfessorshipsGroup> instance = select(externalAuthorization, period);
        return instance.isPresent() ? instance.get() : create(externalAuthorization, period);
    }

    private static Optional<PersistentProfessorshipsGroup> select(final Boolean externalAuthorization, final AcademicPeriod period) {
        return filter(PersistentProfessorshipsGroup.class).firstMatch(new Predicate<PersistentProfessorshipsGroup>() {
            @Override
            public boolean apply(PersistentProfessorshipsGroup group) {
                return group.getExternalAuthorizations().equals(externalAuthorization)
                        && group.getOnCurrentPeriod().equals(period);
            }
        });
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentProfessorshipsGroup create(Boolean externalAuthorization, AcademicPeriod period) {
        Optional<PersistentProfessorshipsGroup> instance = select(externalAuthorization, period);
        return instance.isPresent() ? instance.get() : new PersistentProfessorshipsGroup(externalAuthorization, period);
    }
}
