package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import com.google.common.base.Strings;

@CustomGroupOperator("studentsConcluded")
public class PersistentStudentsConcludedInExecutionYearGroup extends PersistentStudentsConcludedInExecutionYearGroup_Base {
    protected PersistentStudentsConcludedInExecutionYearGroup(Degree degree, ExecutionYear conclusionYear) {
        super();
        setDegree(degree);
        setConclusionYear(conclusionYear);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentStudentsConcludedInExecutionYearGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentStudentsConcludedInExecutionYearGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<ExecutionYear> executionYearArgument() {
        return new SimpleArgument<ExecutionYear, PersistentStudentsConcludedInExecutionYearGroup>() {
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
            public String extract(PersistentStudentsConcludedInExecutionYearGroup group) {
                return group.getConclusionYear() != null ? group.getConclusionYear().getAcademicInterval()
                        .getResumedRepresentationInStringFormat() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegree().getPresentationName(), getConclusionYear().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        for (Registration registration : getDegree().getRegistrationsSet()) {
            if (registration.hasConcluded()) {
                LocalDate conclusionDate = getConclusionDate(getDegree(), registration);
                if (conclusionDate != null
                        && (conclusionDate.getYear() == getConclusionYear().getEndCivilYear() || conclusionDate.getYear() == getConclusionYear()
                                .getBeginCivilYear())) {
                    User user = registration.getPerson().getUser();
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
        if (user == null || user.getPerson().getStudent() == null) {
            return false;
        }
        for (final Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
            if (registration.isConcluded() && registration.getDegree().equals(getDegree())) {
                LocalDate conclusionDate = getConclusionDate(registration.getDegree(), registration);
                if (conclusionDate != null
                        && (conclusionDate.getYear() == getConclusionYear().getEndCivilYear() || conclusionDate.getYear() == getConclusionYear()
                                .getBeginCivilYear())) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    private LocalDate getConclusionDate(Degree degree, Registration registration) {
        for (StudentCurricularPlan scp : registration.getStudentCurricularPlansByDegree(degree)) {
            if (registration.isBolonha()) {
                if (scp.getLastConcludedCycleCurriculumGroup() != null) {
                    YearMonthDay conclusionDate =
                            registration.getConclusionDate(scp.getLastConcludedCycleCurriculumGroup().getCycleType());
                    if (conclusionDate != null) {
                        return conclusionDate.toLocalDate();
                    }
                }
                return null;
            } else {
                return registration.getConclusionDate() != null ? registration.getConclusionDate().toLocalDate() : null;
            }
        }
        return null;
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setDegree(null);
        setConclusionYear(null);
        super.gc();
    }

    public static PersistentStudentsConcludedInExecutionYearGroup getInstance(Degree degree, ExecutionYear conclusionYear) {
        PersistentStudentsConcludedInExecutionYearGroup instance = select(degree, conclusionYear);
        return instance != null ? instance : create(degree, conclusionYear);
    }

    private static PersistentStudentsConcludedInExecutionYearGroup create(Degree degree, ExecutionYear conclusionYear) {
        PersistentStudentsConcludedInExecutionYearGroup instance = select(degree, conclusionYear);
        return instance != null ? instance : new PersistentStudentsConcludedInExecutionYearGroup(degree, conclusionYear);
    }

    private static PersistentStudentsConcludedInExecutionYearGroup select(Degree degree, ExecutionYear conclusionYear) {
        Set<PersistentStudentsConcludedInExecutionYearGroup> candidates =
                conclusionYear.getStudentsConcludedInExecutionYearGroupSet();
        for (PersistentStudentsConcludedInExecutionYearGroup group : candidates) {
            if (group.getDegree().equals(degree)) {
                return group;
            }
        }
        return null;
    }
}
