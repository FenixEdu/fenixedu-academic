<%@ page import="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.EditEnrolmentBean" %><%--

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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.edit.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:hasMessages>
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>
<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>
<%
	String actionPath = "/studentEnrolments.do?scpID=" + request.getParameter("scpID") + "&executionPeriodId=" + request.getParameter("executionPeriodId");
	String postbackActionPath = "/studentEnrolments.do?method=prepareEditEnrolmentPostback&enrolmentId=" + request.getParameter("enrolmentId") + "&scpID=" + request.getParameter("scpID") + "&executionPeriodId=" + request.getParameter("executionPeriodId");
%>
<bean:define id="editEnrolmentBean" name="enrolmentBean" type="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.EditEnrolmentBean"/>

<fr:form action="<%= actionPath %>">
    <html:hidden property="method" value="editEnrolment" />
	<%--<bean:define id="registrationId" name="registration" property="externalId" />--%>
	<%--<html:hidden property="registrationId" value="<%= registrationId.toString() %>"/>--%>
	
	<fr:edit id="editEnrolmentBean" name="enrolmentBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.EditEnrolmentBean">
            <fr:slot name="studentCurricularPlan.student.number" readOnly="true" key="label.number" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
            <fr:slot name="studentCurricularPlan.student.name" readOnly="true" key="label.name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			<fr:slot name="enrolment.executionPeriod.qualifiedName" readOnly="true" key="label.executionPeriod"/>
			<fr:slot name="enrolment.curricularCourse.name" readOnly="true" key="label.externalUnits.externalCurricularCourse"/>
			<fr:slot name="enrolment.grade" readOnly="true" key="label.grade.simple"/>
			
			<fr:slot name="credits" key="label.ects.credits" readOnly="true" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			<fr:slot name="weight" key="label.set.evaluation.enrolment.weight" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			<fr:slot name="ectsConversionTable" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.grade.comparability.table" bundle="APPLICATION_RESOURCES">
				<fr:property name="providerClass" value="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.EditEnrolmentBean$ConversionTableProvider" />
				<fr:property name="format" value="\${presentationName.content}"/>
				<fr:property name="destination" value="postback"/>
			</fr:slot>
			<% if (editEnrolmentBean.isEmptyTable())  { %>
				<fr:slot name="normalizedGrade" layout="grade-input" key="label.normalized.grade" >
					<fr:property name="maxLength" value="2" />
					<fr:property name="size" value="2" />
				</fr:slot>
			<% } else { %>
				<fr:slot name="normalizedGrade" readOnly="true" key="label.normalized.grade" />
			<% } %>
		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:destination name="postback" path="<%=postbackActionPath%>"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='prepare';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>

</fr:form>
