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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="student" name="registration" property="student" />

<h2><bean:message key="label.student.manageExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<p class="mvert2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />
<logic:notEmpty name="parameters">
	<bean:define id="parameters">&amp;<bean:write name="parameters"/></bean:define>
</logic:notEmpty>

<ul>
	<li>
		<bean:define id="url1"><bean:write name="contextInformation"/>method=chooseExternalUnit&amp;registrationId=<bean:write name="registration" property="externalId" /><bean:write name="parameters"/></bean:define>
		<html:link action='<%= url1 %>'><bean:message key="label.student.create.external.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
</ul>

<html:messages property="error" message="true" id="errMsg" bundle="STUDENT_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<bean:define id="registrationId" name="registration" property="externalId" />

<fr:form action="<%= contextInformation.toString() + "registrationId=" + registrationId + parameters.toString()  %>">

	<html:hidden property="method" value="deleteExternalEnrolments"/>

	<logic:notEmpty name="registration" property="externalEnrolments">
		<p class="mtop15 mbottom05"><strong><bean:message key="label.student.existingExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<fr:view name="registration" property="externalEnrolments" schema="ExternalEnrolment.view-externalCurricularCourse">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0 acenter,inobullet ulmvert0,acenter,acenter,acenter,acenter" />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="externalEnrolmentsToDelete" />
				<fr:property name="checkboxValue" value="externalId" />	
				
				<fr:property name="linkFormat(edit)" value="<%= contextInformation.toString() + "method=prepareEditExternalEnrolment&externalEnrolmentId=${externalId}" %>" />
				<fr:property name="key(edit)" value="label.edit"/>
				<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="registration" property="externalEnrolments">
		<p class="mvert15">
			<em><bean:message key="label.student.enrollment.no.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		</p>
	</logic:empty>
	
	<html:cancel onclick="this.form.method.value='cancelExternalEnrolment';"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
