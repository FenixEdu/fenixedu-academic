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
<h2><bean:write name="process" property="displayName" /></h2>

<bean:define id="process" name="process" type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess" />
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="childProcessName" name="childProcessName" />
<bean:define id="processName" name="processName" />

<%-- show child processes --%>
<logic:present name="childsWithMissingRequiredDocuments" >
	<fr:view name="childsWithMissingRequiredDocuments" schema="SecondCycleIndividualCandidacyProcess.missing.required.documents.list">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
			<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

			<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
			<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
			<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
						
			<fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
   	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
		</fr:layout>
	</fr:view>
	<bean:size id="childProcessesSize" name="childsWithMissingRequiredDocuments" />
	
	<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize"/></strong></p>
	
</logic:present>
