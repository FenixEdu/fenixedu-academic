package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@CustomGroupOperator("academic")
public class PersistentAcademicOperationGroup extends PersistentAcademicOperationGroup_Base {
    protected PersistentAcademicOperationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        super();
        setOperation(operation);
        if (programs != null) {
            getProgramSet().addAll(programs);
        }
        if (offices != null) {
            getOfficeSet().addAll(offices);
        }
        setScope(scope);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<AcademicOperationType> academicOperationTypeArgument() {
        return new SimpleArgument<AcademicOperationType, PersistentAcademicOperationGroup>() {
            @Override
            public AcademicOperationType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : AcademicOperationType.valueOf(argument);
            }

            @Override
            public Class<? extends AcademicOperationType> getType() {
                return AcademicOperationType.class;
            }

            @Override
            public String extract(PersistentAcademicOperationGroup group) {
                return group.getOperation() != null ? group.getOperation().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<AcademicOperationType.Scope> academicOperationTypeScopeArgument() {
        return new SimpleArgument<AcademicOperationType.Scope, PersistentAcademicOperationGroup>() {
            @Override
            public AcademicOperationType.Scope parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : AcademicOperationType.Scope.valueOf(argument);
            }

            @Override
            public Class<? extends AcademicOperationType.Scope> getType() {
                return AcademicOperationType.Scope.class;
            }

            @Override
            public String extract(PersistentAcademicOperationGroup group) {
                return group.getScope() != null ? group.getScope().name() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getOperation().getLocalizedName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : getPeople()) {
            if (person.getUser() != null) {
                users.add(person.getUser());
            }
        }
        return users;
    }

    private Set<Person> getPeople() {
        if (getScope() != null) {
            return PersistentAcademicAuthorizationGroup.getElements(getScope());
        }
        return PersistentAcademicAuthorizationGroup.getElements(getOperation(), getProgramSet(), getOfficeSet());
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
        if (getScope() != null) {
            return PersistentAcademicAuthorizationGroup.isMember(user.getPerson(), getScope());
        }
        return PersistentAcademicAuthorizationGroup.isMember(user.getPerson(), getOperation(), getProgramSet(), getOfficeSet());
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
        getProgramSet().clear();
        getOfficeSet().clear();
        super.gc();
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation, final Scope scope) {
        return getInstance(operation, null, null, scope);
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        Optional<PersistentAcademicOperationGroup> instance = select(operation, programs, offices, scope);
        return instance.isPresent() ? instance.get() : create(operation, programs, offices, scope);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentAcademicOperationGroup create(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        Optional<PersistentAcademicOperationGroup> instance = select(operation, programs, offices, scope);
        return instance.isPresent() ? instance.get() : new PersistentAcademicOperationGroup(operation, programs, offices, scope);
    }

    private static Optional<PersistentAcademicOperationGroup> select(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        return filter(PersistentAcademicOperationGroup.class).firstMatch(new Predicate<PersistentAcademicOperationGroup>() {
            @Override
            public boolean apply(PersistentAcademicOperationGroup group) {
                return Objects.equal(group.getOperation(), operation) && collectionEquals(group.getProgramSet(), programs)
                        && collectionEquals(group.getOfficeSet(), offices) && Objects.equal(group.getScope(), scope);
            }
        });
    }

    private static boolean collectionEquals(Set<?> one, Set<?> another) {
        //This could be made more efficient once issue #187 is fixed.
        return Sets.symmetricDifference(Objects.firstNonNull(one, Collections.emptySet()),
                Objects.firstNonNull(another, Collections.emptySet())).isEmpty();
    }

    public static Set<DegreeType> getDegreeTypesForOperation(Party party, AcademicOperationType operation) {
        return getFromGroups(party, operation, new Function<PersistentAcademicAuthorizationGroup, Collection<DegreeType>>() {
            @Override
            public Collection<DegreeType> apply(PersistentAcademicAuthorizationGroup group) {
                Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
                for (AcademicProgram program : group.getFullProgramSet()) {
                    degreeTypes.add(program.getDegreeType());
                }
                degreeTypes.removeAll(Collections.singleton(null));
                return degreeTypes;
            }
        });
    }

    public static Set<ServiceAgreementTemplate> getServiceAgreementTemplatesForOperation(Person person,
            AcademicOperationType operation) {
        return getFromGroups(person, operation,
                new Function<PersistentAcademicAuthorizationGroup, Collection<ServiceAgreementTemplate>>() {
                    @Override
                    public Collection<ServiceAgreementTemplate> apply(PersistentAcademicAuthorizationGroup group) {
                        Set<ServiceAgreementTemplate> templates = new HashSet<ServiceAgreementTemplate>();
                        for (AcademicProgram program : group.getFullProgramSet()) {
                            templates.add(program.getAdministrativeOffice().getServiceAgreementTemplate());
                        }
                        return templates;
                    }
                });
    }

    public static Set<AcademicServiceRequest> getAcademicServiceRequests(Party party, Integer year,
            AcademicServiceRequestSituationType situation) {
        return getAcademicServiceRequests(party, year, situation, null);
    }

    public static Set<AcademicServiceRequest> getAcademicServiceRequests(Party party, Integer year,
            AcademicServiceRequestSituationType situation, Interval interval) {
        Set<AcademicServiceRequest> serviceRequests = new HashSet<AcademicServiceRequest>();
        Set<AcademicProgram> programs = getProgramsForOperation(party, AcademicOperationType.SERVICE_REQUESTS);
        Collection<AcademicServiceRequest> possible = null;
        if (year != null) {
            possible = AcademicServiceRequestYear.getAcademicServiceRequests(year);
        } else {
            possible = Bennu.getInstance().getAcademicServiceRequestsSet();
        }
        for (AcademicServiceRequest request : possible) {
            if (!programs.contains(request.getAcademicProgram())) {
                continue;
            }
            if (situation != null && !request.getAcademicServiceRequestSituationType().equals(situation)) {
                continue;
            }
            if (interval != null && !interval.contains(request.getActiveSituationDate())) {
                continue;
            }
            serviceRequests.add(request);
        }
        return serviceRequests;
    }

    public static boolean isAuthorized(Party party, AcademicServiceRequest request) {
        return isAuthorized(party, request, AcademicOperationType.SERVICE_REQUESTS);
    }

    public static boolean isAuthorized(Party party, AcademicServiceRequest request, AcademicOperationType operation) {
        Set<AcademicProgram> programs = getProgramsForOperation(party, operation);
        return programs.contains(request.getAcademicProgram());
    }

    public static Set<Degree> getDegreesForOperation(Person person, AcademicOperationType operation) {
        return Sets.newHashSet(Iterables.filter(getProgramsForOperation(person, operation), Degree.class));
    }

    public static Set<PhdProgram> getPhdProgramsForOperation(Person person, AcademicOperationType operation) {
        return Sets.newHashSet(Iterables.filter(getProgramsForOperation(person, operation), PhdProgram.class));
    }

    public static Set<AdministrativeOffice> getOfficesForOperation(Party party, AcademicOperationType operation) {
        return getFromGroups(party, operation,
                new Function<PersistentAcademicAuthorizationGroup, Collection<AdministrativeOffice>>() {
                    @Override
                    public Collection<AdministrativeOffice> apply(PersistentAcademicAuthorizationGroup group) {
                        return group.getOffice();
                    }
                });
    }

    public static Set<AcademicProgram> getProgramsForOperation(Party party, AcademicOperationType operation) {
        return getFromGroups(party, operation, new Function<PersistentAcademicAuthorizationGroup, Collection<AcademicProgram>>() {
            @Override
            public Collection<AcademicProgram> apply(PersistentAcademicAuthorizationGroup group) {
                return group.getFullProgramSet();
            }
        });
    }

    static <T> Set<T> getFromGroups(Party party, AcademicOperationType operation,
            Function<PersistentAcademicAuthorizationGroup, Collection<T>> function) {
        Set<T> results = new HashSet<T>();
        Set<PersistentAcademicAuthorizationGroup> groups =
                PersistentAcademicAuthorizationGroup.getGroupsFor(party, operation, Collections.<AcademicProgram> emptySet(),
                        Collections.<AdministrativeOffice> emptySet());
        for (PersistentAcademicAuthorizationGroup group : groups) {
            results.addAll(function.apply(group));
        }
        return results;
    }
}
