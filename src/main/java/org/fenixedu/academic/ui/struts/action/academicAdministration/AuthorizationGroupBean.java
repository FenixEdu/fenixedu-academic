/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

public class AuthorizationGroupBean implements Serializable, Comparable<AuthorizationGroupBean> {

    private static final long serialVersionUID = -8809011815711452960L;

    private AcademicAccessRule rule;

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

    public AuthorizationGroupBean(AcademicAccessRule rule) {
        super();
        setRule(rule);
    }

    public String getId() {
        return rule == null ? "-1" : rule.getExternalId();
    }

    public AcademicAccessRule getRule() {
        return rule;
    }

    private void setRule(AcademicAccessRule rule) {
        this.rule = rule;
        this.operation = rule.getOperation();
        this.programs = new HashSet<AcademicProgram>(rule.getProgramSet());
        this.offices = new HashSet<AdministrativeOffice>(rule.getOfficeSet());
    }

    public AcademicOperationType getOperation() {
        return operation;
    }

    public void setOperation(AcademicOperationType operation) {
        this.operation = operation;
    }

    public boolean getNewObject() {
        return rule == null;
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
        rule.changeOperation(operation);
    }

    @Atomic
    public void create(Party party, Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        setRule(operation.grant(makeGroup(party), newPrograms, newOffices).orElse(null));
    }

    private Group makeGroup(Party party) {
        if (party instanceof Person) {
            return UserGroup.of(((Person) party).getUser());
        }
        return UnitGroup.recursiveWorkers((Unit) party);
    }

    @Atomic
    public void editAuthorizationPrograms(Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        setRule(rule.changeProgramsAndOffices(newPrograms, newOffices));
    }

    @Atomic
    public void delete(Party party) {
        setRule((AcademicAccessRule) rule.changeWhoCanAccess(rule.getWhoCanAccess().minus(makeGroup(party))).orElse(null));
    }

    @Override
    public int compareTo(AuthorizationGroupBean bean) {
        if (this.rule == bean.rule) {
            return 0;
        }
        if (this.rule == null) {
            return 1;
        }
        if (bean.rule == null) {
            return -1;
        }
        return this.operation.compareTo(bean.operation);
    }

}
