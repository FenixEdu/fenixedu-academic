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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AcademicAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AcademicProgramAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AdministrativeOfficeAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Splitter;

public class AuthorizationsManagementBean implements Serializable {
    private static final long serialVersionUID = 604369029723208403L;

    private AcademicOperationType operation;

    private List<AuthorizationGroupBean> groups;

    public AuthorizationsManagementBean() {
        super();
    }

    public AuthorizationsManagementBean(AcademicOperationType operation) {
        this.operation = operation;
        if (operation != null) {
            groups =
                    AcademicAccessRule.accessRules().filter(r -> r.getOperation().equals(operation))
                            .map(AuthorizationGroupBean::new).collect(Collectors.toList());
        }
    }

    public AcademicOperationType getOperation() {
        return operation;
    }

    public boolean getHasNewObject() {
        for (AuthorizationGroupBean bean : groups) {
            if (bean.getNewObject()) {
                return true;
            }
        }
        return false;
    }

    public List<Degree> getDegrees() {

        List<Degree> degrees = new ArrayList<Degree>(Bennu.getInstance().getDegreesSet());

        Collections.sort(degrees);

        return degrees;
    }

    public List<PhdProgram> getPhdPrograms() {

        List<PhdProgram> programs = new ArrayList<PhdProgram>(Bennu.getInstance().getPhdProgramsSet());

        Collections.sort(programs, PhdProgram.COMPARATOR_BY_NAME);

        return programs;

    }

    public List<AuthorizationGroupBean> getGroups() {
        return this.groups;
    }

    public Set<DegreeType> getDegreeTypes() {
        return DegreeType.all().filter(type -> !type.isEmpty()).collect(Collectors.toSet());
    }

    public Set<AdministrativeOffice> getAdministrativeOffices() {
        return Bennu.getInstance().getAdministrativeOfficesSet();
    }

    public void removeAuthorization(String parameter) {
        AuthorizationGroupBean bean = getBeanByOid(parameter);
        if (bean != null) {
            if (bean.getRule() != null) {
                bean.revoke();
            }
            getGroups().remove(bean);
        }
    }

    public void editAuthorization(String parameter) {
        AuthorizationGroupBean bean = getBeanByOid(parameter);
        if (bean != null) {
            bean.edit();
        }
    }

    public void addNewAuthorization() {
        groups.add(0, new AuthorizationGroupBean());
    }

    private AuthorizationGroupBean getBeanByOid(String parameter) {
        for (AuthorizationGroupBean bean : getGroups()) {
            if (bean.getId().equals(parameter)) {
                return bean;
            }
        }
        return null;
    }

    public void createAuthorization(String courses, String officesStr) {
        AuthorizationGroupBean bean = getBeanByOid("-1");
        if (bean != null) {
            bean.create(operation, extractTargets(courses, officesStr));
        }
    }

    private static final Splitter SPLITTER = Splitter.on(';');

    public void editAuthorizationPrograms(String oid, String courses, String officesStr) {
        AuthorizationGroupBean bean = getBeanByOid(oid);
        if (bean != null) {
            bean.editAuthorizationPrograms(operation, extractTargets(courses, officesStr));
        }
    }

    private Set<AcademicAccessTarget> extractTargets(String courses, String officesStr) {
        Set<AcademicAccessTarget> targets = new HashSet<>();
        if (!courses.trim().isEmpty()) {
            for (String course : SPLITTER.split(courses)) {
                AcademicProgram program = FenixFramework.getDomainObject(course);
                targets.add(new AcademicProgramAccessTarget(program));
            }
        }
        if (!officesStr.trim().isEmpty()) {
            for (String officeStr : SPLITTER.split(officesStr)) {
                AdministrativeOffice office = FenixFramework.getDomainObject(officeStr);
                targets.add(new AdministrativeOfficeAccessTarget(office));
            }
        }
        return targets;
    }

    public Set<User> getMembers() {
        if (operation != null) {
            SortedSet<User> members = new TreeSet<>(User.COMPARATOR_BY_NAME);
            members.addAll(AcademicAccessRule.accessRules().filter(r -> r.getOperation().equals(operation))
                    .map(r -> r.getWhoCanAccess()).reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get)
                    .getMembers());
            return members;
        }
        return Collections.emptySet();
    }
}
