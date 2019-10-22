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
package org.fenixedu.academic.domain.person;

import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

@Deprecated
public enum RoleType implements IPresentableEnum {

    MESSAGING("logged"),

    PERSON("logged"),

    STUDENT("activeStudents"),

    TEACHER("activeTeachers"),

    RESEARCHER("#researcher"),

    DEPARTMENT_MEMBER("activeTeachers"),

    RESOURCE_ALLOCATION_MANAGER("#resourceAllocationManager"),

    /**
     * @deprecated Use {@link RoleType}.ACADEMIC_ADMINISTRATIVE_OFFICE instead
     */
    MASTER_DEGREE_ADMINISTRATIVE_OFFICE("#masterDegreeAdmOffice"),

    COORDINATOR("allCoordinators"),

    /**
     * @deprecated Use {@link Group#managers()} instead
     */
    @Deprecated
    MANAGER("#managers"),

    /**
     * @deprecated Use {@link org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup#get()} with
     *             {@link org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType#MANAGE_DEGREE_CURRICULAR_PLANS }instead
     */
    DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER("#degreeAdmOfficeSudo"),

    SCIENTIFIC_COUNCIL("#scientificCouncil"),

    OPERATOR("#operator"),

    GEP("#gep"),

    DIRECTIVE_COUNCIL("#directiveCouncil"),

    BOLONHA_MANAGER("#bolonhaManager"),

    SPACE_MANAGER("#spaceManager"),

    SPACE_MANAGER_SUPER_USER("#spaceManagerSudo"),

    ALUMNI("allAlumni"),

    PEDAGOGICAL_COUNCIL("#pedagogicalCouncil"),

    CANDIDATE("candidate"),

    ACADEMIC_ADMINISTRATIVE_OFFICE("#academicAdmOffice"),

    LIBRARY("#library"),

    INTERNATIONAL_RELATION_OFFICE("#internationalRelationsOffice"),

    EXTERNAL_SUPERVISOR("externalSupervisor"),

    PUBLIC_RELATIONS_OFFICE("#publicRelationsOffice"),

    NAPE("#nape"),

    RESIDENCE_MANAGER("#residenceManager"),

    RECTORATE("#rectorate"),

    HTML_CAPABLE_SENDER("#htmlCapableSender")

    ;

    private static final Logger logger = LoggerFactory.getLogger(RoleType.class);

    public String getName() {
        return name();
    }

    private final Group actualGroup;

    private RoleType(String underlyingGroup) {
        this.actualGroup = Group.parse(underlyingGroup);
    }

    public Group actualGroup() {
        return actualGroup;
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, name());
    }

    public static void grant(RoleType roleType, User user) {
        Group group = roleType.actualGroup();
        if (group instanceof DynamicGroup) {
            DynamicGroup dynamic = (DynamicGroup) group;
            dynamic.mutator().changeGroup(dynamic.underlyingGroup().grant(user));
        } else {
            logger.warn("RoleType '{}' is not manageable!", roleType.name());
        }
    }

    public static void revoke(RoleType roleType, User user) {
        Group group = roleType.actualGroup();
        if (group instanceof DynamicGroup) {
            DynamicGroup dynamic = (DynamicGroup) group;
            dynamic.mutator().changeGroup(dynamic.underlyingGroup().revoke(user));
        } else {
            logger.warn("RoleType '{}' is not manageable!", roleType.name());
        }
    }

    public boolean isMember(User user) {
        return actualGroup.isMember(user) || PermissionService.isMember(actualGroup.getExpression().replace("#", ""), user);
    }

}
