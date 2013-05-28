<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>

<%-- <h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2> --%>

<h2>Edit Personal Information</h2>


<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>

 	<html:hidden property="method" value="executeEditCandidacyPersonalInformation" />
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="personBean">
		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean" property="personBean"
			schema="ErasmusIndividualCandidacyProcess.personalDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=executeEditCandidacyPersonalInformationInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		
		<p class="mtop05">
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
			<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		</p>
		
	</logic:notEmpty>

</fr:form>
