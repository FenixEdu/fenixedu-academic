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
<h2><bean:message key="label.candidacy.degreeChange" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%= "/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>
	<html:hidden property="method" value="listProcessAllowedActivities" />

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h3>
	<br/>
	<logic:notEmpty name="individualCandidaciesByDegree">
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>
	<br/>
	<br/>
	<logic:iterate id="entry" name="individualCandidaciesByDegree">
	
		<bean:define id="degree" name="entry" property="key"/>
		<bean:define id="individualCandidacyProcesses" name="entry" property="value"/>
		
		<bean:define id="degreeId" name="degree" property="externalId" />
		<strong><bean:write name="degree" property="presentationName" /></strong>:
		<html:link action='<%= "/caseHandling" + processName + ".do?method=prepareExecuteIntroduceCandidacyResultsForDegree&amp;processId=" + processId.toString() + "&amp;degreeId=" + degreeId.toString() %>' ><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES" /></html:link>
		<br/>
		<fr:view name="individualCandidacyProcesses" schema="DegreeChangeIndividualCandidacyResultBean.introduce.result.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop025"/>
			</fr:layout>
		</fr:view>
		<br/>
	</logic:iterate>

	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
