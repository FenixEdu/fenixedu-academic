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

<h2><bean:message key="title.candidacy.transfer.application" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<br/>
<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<p>
	<em><bean:message key="message.candidacy.transfer.application.instruction" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</p>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= String.format("/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeCopyIndividualCandidacyToNextCandidacyProcess&processId=%s&parentProcessId=%s", processId, parentProcessId) %>">
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<fr:edit id="individualCandidacyProcessBean.choose" name="individualCandidacyProcessBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="copyDestinationProcess" required="true" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle.SecondCycleNextCandidacyProcesses" />
				<fr:property name="format" value="${candidacyPeriod.presentationName}" />
			</fr:slot>
			
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= String.format("/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeCopyIndividualCandidacyToNextCandidacyProcessInvalid&processId=%s&parentProcessId=%s", processId, parentProcessId) %>" />
		<fr:destination name="cancel" path="<%="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=listProcessAllowedActivities&processId=" + processId.toString() %>" />
		
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="label.transfer" bundle="ACADEMIC_OFFICE_RESOURCES" /> </html:submit>
		<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /> </html:cancel>
	</p>

</fr:form>