<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="net.sourceforge.fenixedu.domain.Installation"%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="unitId" name="unit" property="externalId"/>
<bean:define id="listThesesActionPath" value="/department/theses.do" toScope="request"/>
<bean:define id="listThesesContext" value="<%= "selectedDepartmentUnitID=" + unitId %>" toScope="request"/>
<bean:define id="listThesesSchema" value="department.thesis.list.filter" toScope="request"/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <%= Installation.getInstance().getInstituitionURL() %>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="<%= String.format("/department/theses.do?method=showTheses&amp;selectedDepartmentUnitID=%s", unitId) %>">
	    <bean:message key="label.dissertations" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="degreeId" name="thesis" property="degree.externalId"/>
    <html:link page="<%= String.format("/department/theses.do?method=showTheses&amp;selectedDepartmentUnitID=%s&amp;degreeID=%s", unitId, degreeId) %>">
        <fr:view name="thesis" property="degree.sigla"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="executionYearId" name="thesis" property="enrolment.executionYear.externalId"/>
    <html:link page="<%= String.format("/department/theses.do?method=showTheses&amp;selectedDepartmentUnitID=%s&amp;degreeID=%s&amp;executionYearID=%s", unitId, degreeId, executionYearId) %>">
		<fr:view name="thesis" property="enrolment.executionYear.year"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.dissertation" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="label.dissertation" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="nameI18n"/>
</h1>

<jsp:include flush="true" page="/publico/showThesisDetails.jsp"/>
