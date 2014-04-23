package net.sourceforge.fenixedu.domain.accessControl.academicAdministration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.LeafGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentAcademicOperationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Access control group for academic operation use cases.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
public class AcademicAuthorizationGroup extends LeafGroup {
    /**
     * 
     */
    private static final long serialVersionUID = 9181155093461555982L;

    private final AcademicOperationType operation;

    private final Set<AcademicProgram> programs;

    private final Set<AdministrativeOffice> offices;

    private final Scope scope;

    public AcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        this.operation = operation;
        this.programs = programs;
        this.offices = offices;
        this.scope = scope;
    }

    public AcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        this(operation, programs, offices, null);
    }

    public AcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> programs) {
        this(operation, programs, new HashSet<AdministrativeOffice>());
    }

    public AcademicAuthorizationGroup(AcademicOperationType operation) {
        this(operation, new HashSet<AcademicProgram>());
    }

    public AcademicAuthorizationGroup(AcademicOperationType operation, AcademicProgram program) {
        this(operation, Collections.singleton(program));
    }

    public AcademicAuthorizationGroup(Scope scope) {
        this(null, new HashSet<AcademicProgram>(), new HashSet<AdministrativeOffice>(), scope);
    }

    @Override
    public Set<Person> getElements() {
        if (scope != null) {
            return PersistentAcademicAuthorizationGroup.getElements(scope);
        }
        return PersistentAcademicAuthorizationGroup.getElements(operation, programs, offices);
    }

    @Override
    public boolean isMember(Person person) {
        if (person == null) {
            return false;
        }
        if (scope != null) {
            return PersistentAcademicAuthorizationGroup.isMember(person, scope);
        }
        return PersistentAcademicAuthorizationGroup.isMember(person, operation, programs, offices);
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

    @Override
    protected Argument[] getExpressionArguments() {
        List<Argument> arguments = new ArrayList<Argument>();
        if (scope != null) {
            arguments.add(new StaticArgument(scope.name()));
        }

        if (operation != null) {
            arguments.add(new StaticArgument(operation.name()));
        }

        for (AcademicProgram program : programs) {
            arguments.add(new OidOperator(program));
        }
        for (AdministrativeOffice office : offices) {
            arguments.add(new OidOperator(office));
        }
        return arguments.toArray(new Argument[0]);
    }

    public static class AcademicAuthorizationGroupBuilder implements GroupBuilder {
        @Override
        public Group build(Object[] arguments) {
            AcademicOperationType operation = null;
            Set<AcademicProgram> programs = new HashSet<AcademicProgram>();
            Set<AdministrativeOffice> offices = new HashSet<AdministrativeOffice>();
            Scope scope = null;
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if (argument instanceof String) {
                    try {
                        scope = Scope.valueOf((String) argument);
                    } catch (IllegalArgumentException e) {
                        operation = (AcademicOperationType.valueOf((String) argument));
                    }
                } else if (argument instanceof AcademicProgram) {
                    programs.add((AcademicProgram) argument);
                } else if (argument instanceof AdministrativeOffice) {
                    offices.add((AdministrativeOffice) argument);
                } else {
                    throw new WrongTypeOfArgumentException(i, AcademicOperationType.class, arguments[i].getClass());
                }
            }
            return new AcademicAuthorizationGroup(operation, programs, offices, scope);
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public PersistentAcademicOperationGroup convert() {
        return PersistentAcademicOperationGroup.getInstance(operation, programs, offices, scope);
    }
}
