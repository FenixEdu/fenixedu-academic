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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear.EnrolmentModelFactoryEditor" %>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.manageEnrolmentModel" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>


<p>
	<html:link page="/student.do?method=visualizeRegistration"
		paramId="registrationID" paramName="enrolmentModelBean"
		paramProperty="registration.externalId">
		<bean:message key="link.student.back"
			bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<bean:define id="registrationId" name="enrolmentModelBean" property="registration.externalId" />
<fr:form action="<%= "/manageEnrolmentModel.do?method=setEnrolmentModel" %>">
	<fr:edit id="enrolmentModelBean" name="enrolmentModelBean" schema="student.manageEnrolmentModel">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/student.do?method=visualizeRegistration&registrationID=" + registrationId %>"/>
		<fr:destination name="postback" path="<%= "/manageEnrolmentModel.do?method=postback" %>"/>
	</fr:edit>
	
	<html:submit><bean:message bundle="APPLICATION_RESOURCES" key="button.submit" /></html:submit>
	<html:cancel><bean:message bundle="APPLICATION_RESOURCES" key="button.cancel" /></html:cancel>
</fr:form>
