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
package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum RoleType implements IPresentableEnum {

    MESSAGING("Messaging"),

    PERSON("Person"),

    STUDENT("Registration"),

    TEACHER("Teacher"),

    RESEARCHER("Researcher"),

    DEPARTMENT_MEMBER("Department Member"),

    RESOURCE_ALLOCATION_MANAGER("Rersource Allocation Management"),

    RESOURCE_MANAGER("Resource Management"),

    @Deprecated
    MASTER_DEGREE_CANDIDATE("Master Degree Candidate"),

    /**
     * @deprecated Use {@link RoleType}.ACADEMIC_ADMINISTRATIVE_OFFICE instead
     */
    MASTER_DEGREE_ADMINISTRATIVE_OFFICE("Master Degree Administrative Office"),

    @Deprecated
    TREASURY("Treasury"),

    COORDINATOR("Coordinator"),

    EMPLOYEE("Employee"),

    PERSONNEL_SECTION("Personnel Section"),

    MANAGER("Management"),

    /**
     * @deprecated Use {@link RoleType}.ACADEMIC_ADMINISTRATIVE_OFFICE instead
     */
    DEGREE_ADMINISTRATIVE_OFFICE("Degree Administrative Office"),

    CREDITS_MANAGER("Credits Management"),

    DEPARTMENT_CREDITS_MANAGER("Department Credits Management"),

    /**
     * @deprecated Use {@link RoleType}.ACADEMIC_ADMINISTRATIVE_OFFICE instead
     */
    DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER("Degree Administrative Office (Super User)"),

    SCIENTIFIC_COUNCIL("Scientific Council"),

    /**
     * @deprecated Use {@link RoleType}.MANAGER instead
     */
    ADMINISTRATOR("Administration"),

    OPERATOR("Operator"),

    @Deprecated
    SEMINARIES_COORDINATOR("Seminaries Coordination"),

    WEBSITE_MANAGER("Website Management"),

    GRANT_OWNER("Grant Owner"),

    DEPARTMENT_ADMINISTRATIVE_OFFICE("Department Administrative Office"),

    GEP("Planning and Studies Office"),

    DIRECTIVE_COUNCIL("Directive Council"),

    DELEGATE("Delegate"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    PROJECTS_MANAGER("Projects Management"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    IT_PROJECTS_MANAGER("IT Projects Management"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    INSTITUCIONAL_PROJECTS_MANAGER("Institutional Projects Management"),

    BOLONHA_MANAGER("Bolonha Process Management"),

    CMS_MANAGER("Content Management"),

    SPACE_MANAGER("Space Management"),

    SPACE_MANAGER_SUPER_USER("Space Management (Super User)"),

    ALUMNI("Alumni"),

    PEDAGOGICAL_COUNCIL("Pedagogical Council"),

    CANDIDATE("Candidate"),

    EXAM_COORDINATOR("Exam Coordinator"),

    ACADEMIC_ADMINISTRATIVE_OFFICE("Academic Administrative Office"),

    PARKING_MANAGER("Parking Manager"),

    LIBRARY("Library"),

    INTERNATIONAL_RELATION_OFFICE("International Relation Office"),

    IDENTIFICATION_CARD_MANAGER("Gestão de Cartões"),

    TUTORSHIP("Tutorship"),

    EXTERNAL_SUPERVISOR("External Supervisor"),

    PUBLIC_RELATIONS_OFFICE("Public Relations Office"),

    NAPE("NAPE"),

    RESIDENCE_MANAGER("Residence Manager"),

    RECTORATE("Rectorate"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    ISTID_PROJECTS_MANAGER("IST-ID Projects Management"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    ISTID_INSTITUCIONAL_PROJECTS_MANAGER("IST-ID Institutional Projects Management"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    ADIST_PROJECTS_MANAGER("ADIST Projects Management"),

    /**
     * @deprecated This is discontinued, don't use it!
     */
    ADIST_INSTITUCIONAL_PROJECTS_MANAGER("ADIST Institutional Projects Management"),

    HTML_CAPABLE_SENDER("Html capable sender"),

    CONTACT_ADMIN("Contact Admin"),

    DEVELOPER("External Application Developer")

    ;

    public String getName() {
        return name();
    }

    public static List<RoleType> getRolesImportance() {
        List<RoleType> rolesImportance = new ArrayList<RoleType>();
        rolesImportance.add(RoleType.TEACHER);
        rolesImportance.add(RoleType.EMPLOYEE);
        rolesImportance.add(RoleType.STUDENT);
        rolesImportance.add(RoleType.GRANT_OWNER);
        rolesImportance.add(RoleType.ALUMNI);
        rolesImportance.add(RoleType.CANDIDATE);
        rolesImportance.add(RoleType.MESSAGING);
        rolesImportance.add(RoleType.PERSON);
        rolesImportance.add(RoleType.EXAM_COORDINATOR);
        return rolesImportance;
    }

    private String defaultLabel = null;

    private RoleType(String defaultLabel) {
        setDefaultLabel(defaultLabel);
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    private void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, name());
    }

}
