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
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AcademicAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accessControl.rules.AccessTarget;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

public class AuthorizationGroupBean implements Serializable, Comparable<AuthorizationGroupBean> {

    private static final long serialVersionUID = -8809011815711452960L;

    private AcademicAccessRule rule;

    private Group whoCanAccess;

    private Set<AcademicProgram> programs;

    private Set<AdministrativeOffice> offices;

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
        if (rule != null) {
            this.whoCanAccess = rule.getWhoCanAccess();
            this.programs = new HashSet<AcademicProgram>(rule.getProgramSet());
            this.offices = new HashSet<AdministrativeOffice>(rule.getOfficeSet());
        }
    }

    public Group getWhoCanAccess() {
        return whoCanAccess;
    }

    public void setWhoCanAccess(Group whoCanAccess) {
        this.whoCanAccess = whoCanAccess;
    }

    public boolean getNewObject() {
        return rule == null;
    }

    public Party getParty() {
        if (whoCanAccess != null) {
            if (whoCanAccess instanceof UnitGroup) {
                return ((UnitGroup) whoCanAccess).getUnit();
            }
            if (whoCanAccess instanceof UserGroup) {
                return ((UserGroup) whoCanAccess).getMembers().iterator().next().getPerson();
            }
        }
        return null;
    }

    public void setParty(Party party) {
        if (party instanceof Unit) {
            whoCanAccess = UnitGroup.workers((Unit) party);
        } else {
            whoCanAccess = ((Person) party).getPersonGroup();
        }
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
        rule.changeWhoCanAccess(whoCanAccess);
    }

    @Atomic
    public <T extends AccessTarget> void create(AcademicOperationType operation, Set<AcademicAccessTarget> whatCanAffect) {
        setRule(operation.grant(whoCanAccess, whatCanAffect).orElse(null));
    }

    @Atomic
    public <T extends AccessTarget> void editAuthorizationPrograms(AcademicOperationType operation,
            Set<AcademicAccessTarget> whatCanAffect) {
        setRule((AcademicAccessRule) rule.changeWhatCanAffect(whatCanAffect).orElse(null));
    }

    @Atomic
    public void revoke() {
        rule.revoke();
        setRule(null);
    }

    @Override
    public int compareTo(AuthorizationGroupBean o) {
        if (getId() == "-1") {
            return -1;
        }
        return rule.compareTo(o.rule);
    }
}
