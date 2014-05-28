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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>
<h3><bean:message key="label.candidacy.createRegistrations" bundle="APPLICATION_RESOURCES"/></h3>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName"/>

<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeCreateRegistrations" />
 	
 	<logic:notEmpty name="candidacyDegreeBeans">
 		<fr:view name="candidacyDegreeBeans" schema='<%= processName.toString() + ".candidacyDegreeBean" %>'>
 			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop025"/>
			</fr:layout>
 		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="candidacyDegreeBeans">
		<strong><em><bean:message key="label.candidacy.no.accepted.candidacies" bundle="APPLICATION_RESOURCES" /></em></strong>
	</logic:empty>
	
 	<br/>
 	
	<logic:notEmpty name="candidacyDegreeBeans">
		<strong><bean:message key="label.candidacy.create.registrations.question" bundle="APPLICATION_RESOURCES" /></strong>
		<br/>
		<br/>
		<html:submit><bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>
	<logic:empty name="candidacyDegreeBeans">
		<br/>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:empty>
</fr:form>
