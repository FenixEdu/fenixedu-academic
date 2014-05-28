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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>

<ul>
	<li>
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteAssignCoordinator&amp;processId=" + processId.toString() %>'>
			<bean:message key="label.erasmus.erasmus.coordinator.insert" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
</ul>


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<p><bean:message key="title.mobility.mobilityCoordinators" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>


<logic:empty name="process" property="coordinators">
	<em>There's no coordinators assigned</em>
</logic:empty>

<logic:notEmpty name="process" property="coordinators">
	<fr:view name="process" property="coordinators" schema="MobilityCoordinator.list">
				<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width45em,width40em,width10em,width10em"/>
		        <fr:property name="sortBy" value="degree.presentationName=asc,teacher.person.name=asc" />
	
				<fr:property name="linkFormat(remove)" value="<%= String.format("/caseHandlingMobilityApplicationProcess.do?method=executeRemoveTeacherFromCoordinators&amp;processId=%s&amp;erasmusCoordinatorExternalId=${externalId}", processId.toString()) %>" />
				<fr:property name="key(remove)" value="label.erasmus.coordinator.removal" />
				<fr:property name="bundle(remove)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="confirmationKey(remove)" value="message.erasmus.coordinator.removal.confirmation" />
				<fr:property name="confirmationBundle(remove)" value="ACADEMIC_OFFICE_RESOURCES" />
		        
			</fr:layout>		
	</fr:view>
</logic:notEmpty>
