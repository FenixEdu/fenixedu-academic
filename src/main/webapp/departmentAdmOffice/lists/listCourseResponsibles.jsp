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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>

<h2><bean:message key="link.listCourseResponsibles" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/listCourseResponsibles.do?method=chooseExecutionYearPostBack">
	<fr:edit id="searchBean" name="searchBean" schema="student.list.searchByCurricularCourse.chooseDegree">
		<fr:destination name="executionYearPostBack" path="/listCourseResponsibles.do?method=chooseExecutionYearPostBack"/>
		<fr:destination name="invalid" path="/listCourseResponsibles.do?method=prepareByCurricularCourse"/>	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
</fr:form>

<p/>

<br/>
<logic:present name="searchBean" property="executionYear">
	<bean:define id="statsUrl">/listCourseResponsibles.do?method=downloadStatistics&executionYearId=<bean:write name="searchBean" property="executionYear.externalId"/></bean:define>
	<html:link action="<%= statsUrl %>">
		<html:img border="0" src='<%= request.getContextPath() + "/images/excel.gif"%>' altKey="excel" bundle="IMAGE_RESOURCES"/>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.download.statistics"/>
	</html:link>
</logic:present>

<logic:present name="courseResponsibles">
	<fr:view name="courseResponsibles">
		<fr:schema type="org.fenixedu.academic.dto.administrativeOffice.lists.SearchCourseResponsiblesParametersBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="curricularCourse.name" key="label.curricular.course.from.curriculum"/>
			<fr:slot name="competenceCourse.name" key="label.competence.course.name"/>
			<fr:slot name="degree.sigla" key="degree"/>
			<fr:slot name="campus.name" key="campus"/>
			<fr:slot name="responsible.name" key="label.responsible" layout="link">
				<fr:property name="useParent" value="true"/>
				<fr:property name="linkFormat" value="https://fenix.ist.utl.pt/homepage/${responsible.user.username}"/>
			</fr:slot>
			<fr:slot name="executionSemester.semester" key="label.semester"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thleft thlight"></fr:property>
			<fr:property name="sortBy" value="curricularCourse.name,competenceCourse.name,executionSemester.semester,degree.sigla,campus.name,responsible.name"/>
		</fr:layout>
	</fr:view>
</logic:present>