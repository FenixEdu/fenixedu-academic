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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml />

<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<h3>
	<bean:write name="auditProcessBean" property="executionCourse.name"/>
	<bean:define id="executionSemester" name="auditProcessBean" property="executionCourse.executionPeriod"/>
	(<bean:write name="executionSemester" property="semester"/>º Semestre <bean:write name="executionSemester" property="executionYear.year"/>)
</h3>

<p class="mvert05"><bean:message key="message.audit.selectPersons" bundle="INQUIRIES_RESOURCES"/></p>

<html:messages id="messages" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error"><bean:write name="messages" filter="false"/></span></p>
</html:messages>

<fr:edit id="auditProcessBean" name="auditProcessBean" action="/qucAudit.do?method=selectPersons">
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditSelectPersonsECBean" bundle="APPLICATION_RESOURCES">
		<fr:slot name="teacher" layout="autoComplete" key="teacher.docente" required="true">
			<fr:property name="size" value="80"/>
			<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${name} / ${istUsername}"/>
			<fr:property name="args" value="slot=name"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchEmployeesAndTeachers"/>
			<fr:property name="indicatorShown" value="true"/>		
			<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person"/>
			<fr:property name="errorStyleClass" value="error0"/>
		</fr:slot>
		<fr:slot name="student" layout="autoComplete" key="student" required="true">
			<fr:property name="size" value="80"/>
			<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${name} / ${istUsername}"/>
			<fr:property name="args" value="slot=name,size=30"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchPeopleByNameOrISTID"/>
			<fr:property name="indicatorShown" value="true"/>		
			<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person"/>
			<fr:property name="errorStyleClass" value="error0"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle5 thlight thright"/>
       	<fr:property name="columnClasses" value=",,noborder"/>
	</fr:layout>
	<fr:destination name="cancel" path="/qucAudit.do?method=searchExecutionCourse"/>
</fr:edit>
