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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<logic:present role="role(MANAGER)">
	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
	<bean:define id="registrationId" name="registrationConclusionBean" property="registration.externalId" />
	 
	<fr:view name="registrationConclusionBean"
		schema="RegistrationConclusionBean.viewForRegistrationWithConclusionProcessedInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight" />
			<fr:property name="rowClasses" value=",,tdhl1,,,,,," />
		</fr:layout>
	</fr:view>
	
	
	<logic:equal name="registrationConclusionBean" property="registration.registrationConclusionProcessed" value="true">
		<html:link action="<%="/registrationConclusion.do?method=prepareEditForRegistration" %>" paramId="registrationId" paramName="registrationConclusionBean" paramProperty="registration.externalId">
			<bean:message  key="label.edit" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</logic:equal>
	
	<br/>
	<br/>
	<bean:define id="studentId" name="registrationConclusionBean" property="registration.student.externalId" />
	<fr:form action="<%="/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans&amp;studentId=" + studentId%>">
		<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
			<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
		</html:cancel>
	</fr:form>

</logic:present>


