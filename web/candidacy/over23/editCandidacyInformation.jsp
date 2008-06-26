<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingOver23IndividualCandidacyProcess.do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeEditCandidacyInformation" />

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="process">
	
		<fr:edit id="individualCandidacyProcessBean.candidacyDate" 
			 name="individualCandidacyProcessBean"
			 schema="Over23IndividualCandidacyProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
	
		<fr:edit id="individualCandidacyProcessBean.degrees"
			name="individualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.degrees">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		<html:submit onclick="this.form.method.value='addDegreeToCandidacyWhenEditing';return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<logic:empty name="individualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<em><bean:message key="label.candidacy.over23.no.associated.degrees" bundle="APPLICATION_RESOURCES"/>.<span class="highlight1"><bean:message key="label.candidacy.over23.must.select.at.least.one.degree" bundle="APPLICATION_RESOURCES"/></span>.</em>
			<br/>
		</logic:empty>
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<bean:message key="label.candidacy.choosen.degrees" bundle="APPLICATION_RESOURCES"/>:
			<table class="tstyle1 mtop025">
			<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees">
				<tr>
					<td><bean:write name="degree" property="name" /> - <bean:write name="degree" property="sigla" /></td>
					<td>
						<bean:define id="degreeId" name="degree" property="idInternal" />
						<html:hidden property="degreeToDelete" value="<%= degreeId.toString() %>" />
						<html:submit onclick="this.form.method.value='removeDegreeFromCandidacyWhenEditing';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
		</logic:notEmpty>
		
		<fr:edit id="individualCandidacyProcessBean.optionalInformation"
			name="individualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.optionalInformation">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		<br/>
		
	</logic:notEmpty>
	
	<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
