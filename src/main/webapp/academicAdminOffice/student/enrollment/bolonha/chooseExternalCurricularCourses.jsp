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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.student.enrollment.choose.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<p class="mvert15">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<p><strong><bean:message key="label.student.enrollment.externalUnit" bundle="ACADEMIC_OFFICE_RESOURCES" />:</strong> <bean:write name="externalUnit" property="name" /></p>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />

<fr:form action="<%= contextInformation.toString() + parameters.toString() %>">
	<html:hidden property="method" value="prepareCreateExternalEnrolments"/>
	
	<bean:define id="registrationId" name="registration" property="externalId" />
	<html:hidden property="registrationId" value="<%= registrationId.toString() %>"/>
	
	<bean:define id="externalUnitId" name="externalUnit" property="externalId" />
	<html:hidden property="externalUnitId" value="<%= externalUnitId.toString() %>"/>

	<logic:notEmpty name="externalCurricularCourseBeans">	
		<fr:view name="externalCurricularCourseBeans" schema="ExternalCurricularCourseResultBean.view-fullName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="selectedExternalCurricularCourses" />
				<fr:property name="checkboxValue" value="externalCurricularCourse.externalId" />	
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="externalCurricularCourseBeans">
		<p class="mvert15"><em><bean:message key="label.student.enrollment.no.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>	

	<html:cancel onclick="this.form.method.value='chooseExternalUnit';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
