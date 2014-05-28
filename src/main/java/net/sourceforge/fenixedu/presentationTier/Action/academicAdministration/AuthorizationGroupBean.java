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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixframework.Atomic;

public class AuthorizationGroupBean implements Serializable, Comparable<AuthorizationGroupBean> {

    private static final long serialVersionUID = -8809011815711452960L;

    private PersistentAcademicAuthorizationGroup group;

    private AcademicOperationType operation;

    private Set<AcademicProgram> programs;

    private Set<AdministrativeOffice> offices;

    static public Comparator<AuthorizationGroupBean> COMPARATOR_BY_LOCALIZED_NAME = new Comparator<AuthorizationGroupBean>() {
        @Override
        public int compare(final AuthorizationGroupBean p1, final AuthorizationGroupBean p2) {
            String operationName1 = p1.getOperation().getLocalizedName();
            String operationName2 = p2.getOperation().getLocalizedName();
            int res = operationName1.compareTo(operationName2);
            return res;
        }
    };

    public AuthorizationGroupBean() {
        super();
        this.programs = new HashSet<AcademicProgram>();
        this.offices = new HashSet<AdministrativeOffice>();
    }

    public AuthorizationGroupBean(PersistentAcademicAuthorizationGroup group) {
        super();
        setGroup(group);
    }

    public String getId() {
        return group == null ? "-1" : group.getExternalId();
    }

    public PersistentAcademicAuthorizationGroup getGroup() {
        return group;
    }

    private void setGroup(PersistentAcademicAuthorizationGroup group) {
        this.group = group;
        this.operation = group.getOperation();
        this.programs = new HashSet<AcademicProgram>(group.getProgramSet());
        this.offices = new HashSet<AdministrativeOffice>(group.getOfficeSet());
    }

    public AcademicOperationType getOperation() {
        return operation;
    }

    public void setOperation(AcademicOperationType operation) {
        this.operation = operation;
    }

    public boolean getNewObject() {
        return group == null;
    }

    public Set<AcademicProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<AcademicProgram> programs) {
        this.programs = programs;
    }

    public Set<AdministrativeOffice> getOffices() {
        return offices;
    }

    public void setOffices(Set<AdministrativeOffice> offices) {
        this.offices = offices;
    }

    @Atomic
    public void edit() {
        if (!group.hasDeletedBennu()) {
            setGroup(group.changeOperation(operation));
        }
    }

    @Atomic
    public void create(Party party, Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        for (PersistentAccessGroup accessGroup : party.getPersistentAccessGroup()) {
            if (accessGroup instanceof PersistentAcademicAuthorizationGroup && !accessGroup.hasDeletedBennu()) {
                if (((PersistentAcademicAuthorizationGroup) accessGroup).getOperation().equals(operation)) {
                    throw new DomainException("error.person.already.has.permission.of.type", operation.getLocalizedName());
                }
            }
        }
        setGroup(new PersistentAcademicAuthorizationGroup(operation, newPrograms, newOffices));
        party.addPersistentAccessGroup(group);
    }

    @Atomic
    public void editAuthorizationPrograms(Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        if (!group.hasDeletedBennu()) {
            setGroup(group.changeProgramsAndOffices(newPrograms, newOffices));
        }
    }

    @Atomic
    public void delete(Party party) {
        if (group.getMember().size() > 1) {
            group.revoke(party);
        } else {
            group.delete();
        }
    }

    @Override
    public int compareTo(AuthorizationGroupBean bean) {
        if (this.group == bean.group) {
            return 0;
        }
        if (this.group == null) {
            return 1;
        }
        if (bean.group == null) {
            return -1;
        }
        return this.operation.compareTo(bean.operation);
    }

}
