package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

@CustomGroupOperator("coordinator")
public class PersistentCoordinatorGroup extends PersistentCoordinatorGroup_Base {
    protected PersistentCoordinatorGroup(DegreeType degreeType, Degree degree) {
        super();
        setDegreeType(degreeType);
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @CustomGroupArgument(index = 1)
    public static Argument<DegreeType> degreeTypeArgument() {
        return new SimpleArgument<DegreeType, PersistentCoordinatorGroup>() {
            @Override
            public DegreeType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : DegreeType.valueOf(argument);
            }

            @Override
            public Class<? extends DegreeType> getType() {
                return DegreeType.class;
            }

            @Override
            public String extract(PersistentCoordinatorGroup group) {
                return group.getDegreeType() != null ? group.getDegreeType().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentCoordinatorGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentCoordinatorGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (getDegreeType() != null) {
            return new String[] { getDegreeType().getFilteredName() };
        } else if (getDegree() != null) {
            return new String[] { getDegree().getPresentationName() };
        }
        return new String[0];
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        if (getDegreeType() != null) {
            ExecutionYear year = ExecutionYear.readCurrentExecutionYear();
            for (final ExecutionDegree executionDegree : year.getExecutionDegreesSet()) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                final Degree degree = degreeCurricularPlan.getDegree();
                if (degree.getDegreeType().equals(getDegreeType())) {
                    for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                        User user = coordinator.getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                        }
                    }
                }
            }
//            for (Degree degree : Degree.readAllByDegreeType(getDegreeType())) {
//                users.addAll(getCoordinators(degree));
//            }
        }
        if (getDegree() != null) {
            users.addAll(getCoordinators(getDegree()));
        }
        if (getDegree() == null && getDegreeType() == null) {
            final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
            for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
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

    private static Set<User> getCoordinators(Degree degree) {
        Set<User> users = new HashSet<>();
        for (Coordinator coordinator : degree.getCurrentCoordinators()) {
            User user = coordinator.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getCoordinatorsSet().isEmpty()) {
            return false;
        }
        for (Coordinator coordinator : user.getPerson().getCoordinatorsSet()) {
            ExecutionDegree executionDegree = coordinator.getExecutionDegree();
            if (executionDegree.getExecutionYear().isCurrent()) {
                if (getDegreeType() != null && getDegreeType() != executionDegree.getDegree().getDegreeType()) {
                    continue;
                }
                if (getDegree() != null && !executionDegree.getDegree().equals(getDegree())) {
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

    public static PersistentCoordinatorGroup getInstance() {
        return getInstance(filter(PersistentCoordinatorGroup.class), null, null);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType) {
        return getInstance(filter(PersistentCoordinatorGroup.class), degreeType, null);
    }

    public static PersistentCoordinatorGroup getInstance(Degree degree) {
        return getInstance(FluentIterable.from(degree.getCoordinatorGroupSet()), null, degree);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType, Degree degree) {
        if (degreeType != null) {
            return getInstance(degreeType);
        }
        if (degree != null) {
            return getInstance(degree);
        }
        return getInstance();
    }

    private static PersistentCoordinatorGroup getInstance(FluentIterable<PersistentCoordinatorGroup> options,
            DegreeType degreeType, Degree degree) {
        Optional<PersistentCoordinatorGroup> instance = select(options, degreeType, degree);
        return instance.isPresent() ? instance.get() : create(options, degreeType, degree);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCoordinatorGroup create(FluentIterable<PersistentCoordinatorGroup> options, DegreeType degreeType,
            Degree degree) {
        Optional<PersistentCoordinatorGroup> instance = select(options, degreeType, degree);
        return instance.isPresent() ? instance.get() : new PersistentCoordinatorGroup(degreeType, degree);
    }

    private static Optional<PersistentCoordinatorGroup> select(FluentIterable<PersistentCoordinatorGroup> options,
            final DegreeType degreeType, final Degree degree) {
        return options.firstMatch(new Predicate<PersistentCoordinatorGroup>() {
            @Override
            public boolean apply(PersistentCoordinatorGroup group) {
                return Objects.equal(group.getDegreeType(), degreeType) && Objects.equal(group.getDegree(), degree);
            }
        });
    }
}
