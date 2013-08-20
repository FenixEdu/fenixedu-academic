<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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