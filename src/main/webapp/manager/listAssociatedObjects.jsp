<%@ page import="pt.ist.fenixframework.DomainObject" %>
<%@ page import="org.fenixedu.academic.domain.Department" %>
<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="if" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:xhtml/>

<h2><bean:message key="title.manage.associated.objects" bundle="MANAGER_RESOURCES"/></h2>

<h3><bean:message key="title.manage.associated.objects.degreeTypes" bundle="MANAGER_RESOURCES"/></h3>
<ul>
    <logic:iterate id="type" name="degreeTypes">
        <li><c:out value="${type.name.content}"/> - <html:link page="/manageAssociatedObjects.do?method=prepareCreateDegreeType&degreeTypeId=${type.externalId}"><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:link><br/>
</li>
    </logic:iterate>
</ul>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateDegreeType"><bean:message key="button.create" bundle="MANAGER_RESOURCES"/></html:link><br/>

<h3><bean:message key="title.manage.associated.objects.departments" bundle="MANAGER_RESOURCES"/></h3>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateDepartment"><bean:message key="button.create" bundle="MANAGER_RESOURCES"/></html:link><br/>
<logic:present name="departments">
    <ul>
        <logic:iterate id="department" name="departments">
            <li><bean:write name="department" property="name"/> - <html:link
                    page="<%= "/manageAssociatedObjects.do?method=prepareEditDepartment&oid=" + ((Department) department).getExternalId() %>"><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:link><br/>
            </li>
        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="departments">
    <bean:message key="info.manage.associated.objects.no.departments" bundle="MANAGER_RESOURCES"/>
</logic:notPresent>

<h3><bean:message key="title.manage.associated.objects.administrativeOffices" bundle="MANAGER_RESOURCES"/></h3>
<html:link page="/manageAssociatedObjects.do?method=prepareAcademicOffice"><bean:message key="button.create" bundle="MANAGER_RESOURCES"/></html:link><br/>
<logic:present name="offices">
    <ul>
        <logic:iterate id="office" name="offices" type="org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice">
            <li><bean:write name="office" property="name.content"/></li>
        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="offices">
    <bean:message key="info.manage.associated.objects.no.administrativeOffices" bundle="MANAGER_RESOURCES"/>
</logic:notPresent>

<h3><bean:message key="title.manage.associated.objects.create.emptyDegree" bundle="MANAGER_RESOURCES"/></h3>
<logic:notEmpty name="emptyDegree">
    <bean:define id="emptyDegree" type="org.fenixedu.academic.domain.EmptyDegree" name="emptyDegree"></bean:define>
    <bean:message key="info.manage.associated.objects.emptyDegree.associated.with" bundle="MANAGER_RESOURCES" /> <%= emptyDegree.getAdministrativeOffice().getName().getContent() %></br>
    <html:link page="/manageAssociatedObjects.do?method=prepareEmptyDegree"><bean:message key="button.change" bundle="MANAGER_RESOURCES"/></html:link><br/>
</logic:notEmpty>
<logic:empty name="emptyDegree">
    <bean:message key="info.manage.associated.objects.no.emptyDegree" bundle="MANAGER_RESOURCES"/></br>
    <html:link page="/manageAssociatedObjects.do?method=prepareEmptyDegree"><bean:message key="button.create" bundle="MANAGER_RESOURCES"/></html:link><br/>
</logic:empty>

<h3><bean:message key="title.manage.associated.objects.create.scientificArea" bundle="MANAGER_RESOURCES"/></h3>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateScientificArea"><bean:message key="button.create" bundle="MANAGER_RESOURCES"/></html:link><br/>
<logic:present name="departments">
    <ul>
        <logic:iterate id="department" name="departments">
            <li><bean:write name="department" property="name"/></li>

            <logic:present name="department" property="departmentUnit">
                <bean:define id="depunit" name="department" property="departmentUnit"/>
                <ul>
                    <logic:iterate id="unit" name="depunit" property="scientificAreaUnits">
                        <li><bean:write name="unit" property="name"></bean:write> - <html:link
                                page="<%= "/manageAssociatedObjects.do?method=prepareCreateCompetenceCourseGroup&oid=" + ((DomainObject) unit)
                .getExternalId()%>"><bean:message key="title.manage.associated.objects.create.competenceCourseGroup" bundle="MANAGER_RESOURCES"/></html:link></li>

                        <logic:present name="unit" property="competenceCourseGroupUnits">
                            <ul>
                                <logic:iterate id="ccg" name="unit" property="competenceCourseGroupUnits">
                                    <li><bean:write name="ccg" property="name"/></li>
                                </logic:iterate>
                            </ul>
                        </logic:present>

                    </logic:iterate>
                </ul>
            </logic:present>

        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="departments">
    <bean:message key="info.manage.associated.objects.no.departments" bundle="MANAGER_RESOURCES"/>
</logic:notPresent>


