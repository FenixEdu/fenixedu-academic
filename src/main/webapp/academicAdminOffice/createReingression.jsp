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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.registrationIngression.mark.reingression" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="success">
	<p><span class="success0"><bean:write name="message" /></span></p>
</html:messages>

<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration" />
<bean:define id="registrationId" name="registration" property="externalId" />

<fr:form action="<%= "/manageIngression.do?method=createReingression&registrationId=" + registrationId %>">
	<fr:edit visible="false" id="bean" name="bean" />
	
	<fr:edit id="bean-edit" name="bean" schema="RegistrationReingression.create">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/manageIngression.do?method=createReingressionInvalid&registrationId=" + registrationId %>" />
		<fr:destination name="cancel" path="<%= "/manageIngression.do?method=prepare&registrationId=" + registrationId %>" />
		
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		
</fr:form>
