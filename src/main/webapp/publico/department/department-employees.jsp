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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
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
    <bean:define id="unitId" name="unit" property="externalId"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.employees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="label.employees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="nameI18n"/>
</h1>

<logic:iterate id="area" name="areas" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit">
	<h2 class="greytxt mtop2">
		<fr:view name="area" property="nameI18n"/>
	</h2>

	<logic:iterate id="_e" name="employees" property="<%= area.getExternalId().toString() %>" type="net.sourceforge.fenixedu.domain.Employee">
		<bean:define id="employee" name="_e" toScope="request"/>
		
		<jsp:include page="department-employee-card.jsp"/>
	</logic:iterate>
</logic:iterate>

<logic:notEmpty name="employeesNoArea">
	<logic:notPresent name="ignoreAreas">
		<h2 class="greytxt mtop2">
			<bean:message key="link.teacher.area.noArea" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
		</h2>
	</logic:notPresent>

	<logic:present name="ignoreAreas">
		<div class="mtop2"></div>
	</logic:present>
	
	<logic:iterate id="_e" name="employeesNoArea" type="net.sourceforge.fenixedu.domain.Employee">
		<bean:define id="employee" name="_e" toScope="request"/>
		<jsp:include page="department-employee-card.jsp"/>
	</logic:iterate>
</logic:notEmpty>
