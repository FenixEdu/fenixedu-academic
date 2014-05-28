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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.externalUnits.createExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mvert2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<logic:notEmpty name="externalCurricularCourseEnrolmentBeans">

	<bean:define id="contextInformation" name="contextInformation" />
	<bean:define id="parameters" name="parameters" />
	<logic:notEmpty name="parameters">
		<bean:define id="parameters">&amp;<bean:write name="parameters"/></bean:define>
	</logic:notEmpty>

	<bean:define id="registrationId" name="registration" property="externalId" />
	<fr:form action="<%= contextInformation.toString() + "registrationId=" + registrationId + parameters %>">
		<html:hidden property="method" value="createExternalEnrolments"/>
		
		<bean:define id="externalUnitId" name="externalUnit" property="externalId" />
		<html:hidden property="externalUnitId" value="<%= externalUnitId.toString() %>"/>
		
		<fr:hasMessages for="externalCurricularCourseEnrolmentBeans" type="validation">
			<span class="error0">
				<br />
				<fr:messages for="externalCurricularCourseEnrolmentBeans" type="validation">
					<fr:message for="externalCurricularCourseEnrolmentBeans" type="validation"/>
				</fr:messages>
			</span>
		</fr:hasMessages>
		
		<fr:edit id="externalCurricularCourseEnrolmentBeans"
				 name="externalCurricularCourseEnrolmentBeans"
				 schema="ExternalCurricularCourseEnrolmentBean.edit">
				 
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
			<fr:destination name="invalid" path="<%= contextInformation.toString() + "method=createExternalEnrolmentsInvalid&registrationId=" + registrationId + parameters +"&externalUnitId=" + externalUnitId %>" />
		</fr:edit>
		<html:submit><bean:message key="button.choose" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='chooseExternalCurricularCourses';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</fr:form>
</logic:notEmpty>
