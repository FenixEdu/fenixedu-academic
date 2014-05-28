/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@GroupOperator("academic")
public class AcademicAuthorizationGroup extends FenixGroup {
    private static final long serialVersionUID = -3178215706386635516L;

    @GroupArgument("")
    private AcademicOperationType operation;

    @GroupArgument
    private Set<AcademicProgram> programs;

    @GroupArgument
    private Set<AdministrativeOffice> offices;

    @GroupArgument
    private Scope scope;

    private AcademicAuthorizationGroup() {
        super();
    }

    private AcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        this();
        this.operation = operation;
        this.programs = programs;
        this.offices = offices;
        this.scope = scope;
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation) {
        return new AcademicAuthorizationGroup(operation, null, null, null);
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation, AcademicProgram program) {
        return new AcademicAuthorizationGroup(operation, Collections.singleton(program), null, null);
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        return new AcademicAuthorizationGroup(operation, programs, offices, scope);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { operation.getLocalizedName() };
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
        if (scope != null) {
            return PersistentAcademicAuthorizationGroup.getElements(scope);
        }
        return PersistentAcademicAuthorizationGroup.getElements(operation, programs, offices);
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
        if (scope != null) {
            return PersistentAcademicAuthorizationGroup.isMember(user.getPerson(), scope);
        }
        return PersistentAcademicAuthorizationGroup.isMember(user.getPerson(), operation, programs, offices);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentAcademicOperationGroup.getInstance(operation, programs, offices, scope);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AcademicAuthorizationGroup) {
            AcademicAuthorizationGroup other = (AcademicAuthorizationGroup) object;
            return Objects.equal(operation, other.operation) && Objects.equal(programs, other.programs)
                    && Objects.equal(offices, other.offices) && Objects.equal(scope, other.scope);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operation, programs, offices, scope);
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
