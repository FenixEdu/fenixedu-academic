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
package net.sourceforge.fenixedu.domain.accessControl.academicAdministration;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Rules for authorizations for academic operations.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
public class PersistentAcademicAuthorizationGroup extends PersistentAcademicAuthorizationGroup_Base {

    static public Comparator<PersistentAcademicAuthorizationGroup> COMPARATOR_BY_LOCALIZED_NAME =
            new Comparator<PersistentAcademicAuthorizationGroup>() {
                @Override
                public int compare(final PersistentAcademicAuthorizationGroup p1, final PersistentAcademicAuthorizationGroup p2) {
                    String operationName1 = p1.getOperation().getLocalizedName();
                    String operationName2 = p2.getOperation().getLocalizedName();
                    int res = Collator.getInstance().compare(operationName1, operationName2);
                    return res;
                }
            };

    public PersistentAcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> targetPrograms,
            Set<AdministrativeOffice> administrativeOffices) {
        super();
        setOperation(operation);
        if (isProgramAllowedAsTarget()) {
            getProgramSet().addAll(targetPrograms);
        }
        if (isOfficeAllowedAsTarget()) {
            getOffice().addAll(administrativeOffices);
        }
    }

    public PersistentAcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> targetPrograms,
            Set<AdministrativeOffice> administrativeOffices, Set<Party> members) {
        this(operation, targetPrograms, administrativeOffices);
        getMemberSet().addAll(members);
    }

    private PersistentAcademicAuthorizationGroup() {
        super();
    }

    @Override
    protected PersistentAcademicAuthorizationGroup instantiate() {
        return new PersistentAcademicAuthorizationGroup();
    }

    @Override
    protected void copy(PersistentAccessGroup old) {
        super.copy(old);
        if (old instanceof PersistentAcademicAuthorizationGroup) {
            PersistentAcademicAuthorizationGroup oldAcademic = (PersistentAcademicAuthorizationGroup) old;
            setOperation(oldAcademic.getOperation());
            getProgramSet().addAll(oldAcademic.getProgramSet());
            getOffice().addAll(oldAcademic.getOffice());
        }
    }

    /**
     * Grants access to a person or unit.
     * 
     * @param member
     *            instance of {@link Party} or {@link Unit} to give access to.
     * @return group with access granted to this person.
     */
    @Override
    public PersistentAcademicAuthorizationGroup grant(Party member) {
        return (PersistentAcademicAuthorizationGroup) super.grant(member);
    }

    /**
     * Revokes access to a person or unit.
     * 
     * @param member
     *            instance of {@link Party} or {@link Unit} to revoke access.
     * @return group with access revoked to this person.
     */
    @Override
    public PersistentAcademicAuthorizationGroup revoke(Party member) {
        return (PersistentAcademicAuthorizationGroup) super.revoke(member);
    }

    /**
     * Override current members.
     * 
     * @param members
     *            the set of new members.
     * @return group with changed members.
     */
    @Override
    public PersistentAcademicAuthorizationGroup changeMembers(Set<Party> members) {
        return (PersistentAcademicAuthorizationGroup) super.changeMembers(members);
    }

    /**
     * Override available operations.
     * 
     * @param operations
     *            the set of new operations.
     * @return group with changed operations.
     */
    public PersistentAcademicAuthorizationGroup changeOperation(AcademicOperationType operation) {
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.setOperation(operation);
        return newGroup;
    }

    /**
     * Add a new office to be target for this group.
     * 
     * @param office
     *            The {@link AdministrativeOffice} to add.
     * @return group with augmented offices.
     */
    public PersistentAcademicAuthorizationGroup augment(AdministrativeOffice office) {
        if (!isOfficeAllowedAsTarget()) {
            throw new DomainException("error.persistent.authorization.group.does.not.allow.offices");
        }
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.addOffice(office);
        return newGroup;
    }

    /**
     * Remove a office from the target offices for this group.
     * 
     * @param office
     *            The {@link AdministrativeOffice} to remove.
     * @return group with degraded offices.
     */
    public PersistentAcademicAuthorizationGroup degrade(AdministrativeOffice office) {
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.removeOffice(office);
        return newGroup;
    }

    /**
     * Override target offices.
     * 
     * @param offices
     *            the set of new offices.
     * @return group with changed offices.
     */
    public PersistentAcademicAuthorizationGroup changeOffices(Set<AdministrativeOffice> offices) {
        if (!isOfficeAllowedAsTarget()) {
            throw new DomainException("error.persistent.authorization.group.does.not.allow.offices");
        }
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.getOfficeSet().clear();
        newGroup.getOfficeSet().addAll(offices);
        return newGroup;
    }

    /**
     * Add a new program to be target for this group.
     * 
     * @param program
     *            The {@link AcademicProgram} to add.
     * @return group with augmented programs.
     */
    public PersistentAcademicAuthorizationGroup augment(AcademicProgram program) {
        if (!isProgramAllowedAsTarget()) {
            throw new DomainException("error.persistent.authorization.group.does.not.allow.programs");
        }
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.addProgram(program);
        return newGroup;
    }

    /**
     * Remove a program from the target programs for this group.
     * 
     * @param program
     *            The {@link AcademicProgram} to remove.
     * @return group with degraded programs.
     */
    public PersistentAcademicAuthorizationGroup degrade(AcademicProgram program) {
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.removeProgram(program);
        return newGroup;
    }

    /**
     * Override target programs.
     * 
     * @param programs
     *            the set of new programs.
     * @return group with changed programs.
     */
    public PersistentAcademicAuthorizationGroup changePrograms(Set<AcademicProgram> programs) {
        if (!isProgramAllowedAsTarget()) {
            throw new DomainException("error.persistent.authorization.group.does.not.allow.programs");
        }
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.getProgramSet().clear();
        newGroup.getProgramSet().addAll(programs);
        return newGroup;
    }

    /**
     * Override target programs and offices
     * 
     * @param programs
     *            the set of new programs.
     * @param offices
     *            the set of new offices.
     * @return group with changed programs and offices.
     */
    public PersistentAcademicAuthorizationGroup changeProgramsAndOffices(Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        PersistentAcademicAuthorizationGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.getProgramSet().clear();
        if (isProgramAllowedAsTarget()) {
            newGroup.getProgramSet().addAll(programs);
        }

        newGroup.getOfficeSet().clear();
        if (isOfficeAllowedAsTarget()) {
            newGroup.getOfficeSet().addAll(offices);
        }
        return newGroup;
    }

    public Set<AcademicProgram> getFullProgramSet() {
        Set<AcademicProgram> programs = new HashSet<AcademicProgram>(getProgramSet());
        for (AdministrativeOffice office : getOfficeSet()) {
            programs.addAll(office.getManagedAcademicProgramSet());
        }
        programs.add(Degree.readEmptyDegree());
        return programs;
    }

    public static Set<PersistentAcademicAuthorizationGroup> getGroupsFor(Party party, AcademicOperationType operation,
            Set<AcademicProgram> programs, Set<AdministrativeOffice> offices) {
        Set<PersistentAcademicAuthorizationGroup> groups = new HashSet<PersistentAcademicAuthorizationGroup>();
        for (PersistentAccessGroup group : party.getPersistentAccessGroupSet()) {
            if (group.isActive() && group instanceof PersistentAcademicAuthorizationGroup) {
                PersistentAcademicAuthorizationGroup academicGroup = (PersistentAcademicAuthorizationGroup) group;
                if (academicGroup.getOperation().equals(operation)
                        && academicGroup.getFullProgramSet().containsAll(nullToEmptySet(programs))
                        && academicGroup.getOfficeSet().containsAll(nullToEmptySet(offices))) {
                    groups.add(academicGroup);
                }
            }
        }
        Collection<Unit> parents = null;
        if (party instanceof Person) {
            Person person = (Person) party;
            parents = person.getCurrentParentUnits(AccountabilityTypeEnum.WORKING_CONTRACT);
        } else if (party instanceof Unit) {
            Unit unit = (Unit) party;
            parents = unit.getCurrentParentUnits(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        }
        if (parents != null) {
            for (Unit parent : parents) {
                groups.addAll(getGroupsFor(parent, operation, programs, offices));
            }
        }
        return groups;
    }

    private static <T> Set<T> nullToEmptySet(Set<T> set) {
        return set == null ? Collections.<T> emptySet() : set;
    }

    public static Set<PersistentAcademicAuthorizationGroup> getGroupsFor(Party party, Scope scope) {
        Set<PersistentAcademicAuthorizationGroup> groups = new HashSet<PersistentAcademicAuthorizationGroup>();
        for (PersistentAccessGroup group : party.getPersistentAccessGroupSet()) {
            if (group.isActive() && group instanceof PersistentAcademicAuthorizationGroup) {
                PersistentAcademicAuthorizationGroup academicGroup = (PersistentAcademicAuthorizationGroup) group;
                if (academicGroup.getOperation().isOfScope(scope)) {
                    groups.add(academicGroup);
                }
            }
        }
        Collection<Unit> parents = null;
        if (party instanceof Person) {
            Person person = (Person) party;
            parents = person.getCurrentParentUnits(AccountabilityTypeEnum.WORKING_CONTRACT);
        } else if (party instanceof Unit) {
            Unit unit = (Unit) party;
            parents = unit.getCurrentParentUnits(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        }
        if (parents != null) {
            for (Unit parent : parents) {
                groups.addAll(getGroupsFor(parent, scope));
            }
        }
        return groups;
    }

    public static Set<Person> getElements(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        Set<Person> members = new HashSet<Person>();
        for (PersistentAccessGroup group : Bennu.getInstance().getPersistentAccessGroupSet()) {
            if (group instanceof PersistentAcademicAuthorizationGroup) {
                PersistentAcademicAuthorizationGroup academicGroup = (PersistentAcademicAuthorizationGroup) group;
                if (academicGroup.getOperation().equals(operation) && academicGroup.getFullProgramSet().containsAll(programs)
                        && academicGroup.getOfficeSet().containsAll(offices)) {
                    members.addAll(academicGroup.getElements());
                }
            }
        }
        return members;
    }

    public static Set<Person> getElements(Scope scope) {
        Set<Person> members = new HashSet<Person>();
        for (PersistentAccessGroup group : Bennu.getInstance().getPersistentAccessGroupSet()) {
            if (group instanceof PersistentAcademicAuthorizationGroup) {
                PersistentAcademicAuthorizationGroup academicGroup = (PersistentAcademicAuthorizationGroup) group;
                if (academicGroup.getOperation().isOfScope(scope)) {
                    members.addAll(academicGroup.getElements());
                }
            }
        }
        return members;
    }

    public static boolean isMember(Party party, AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return !getGroupsFor(party, operation, programs, offices).isEmpty();
    }

    public static boolean isMember(Party party, Scope scope) {
        return !getGroupsFor(party, scope).isEmpty();
    }

    private boolean isOfficeAllowedAsTarget() {
        return getOperation().isOfficeAllowedAsTarget();
    }

    private boolean isProgramAllowedAsTarget() {
        return getOperation().isProgramAllowedAsTarget();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.AcademicProgram> getProgram() {
        return getProgramSet();
    }

    @Deprecated
    public boolean hasAnyProgram() {
        return !getProgramSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice> getOffice() {
        return getOfficeSet();
    }

    @Deprecated
    public boolean hasAnyOffice() {
        return !getOfficeSet().isEmpty();
    }

    @Deprecated
    public boolean hasOperation() {
        return getOperation() != null;
    }

}
