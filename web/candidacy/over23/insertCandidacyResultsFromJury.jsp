<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingOver23CandidacyProcess.do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeInsertResultsFromJury" />

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h3>
	<fr:edit id="over23IndividualCandidacyResultBeans"
		name="over23IndividualCandidacyResultBeans"
		schema="Over23IndividualCandidacyResultBean.manage.all.students">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 mtop025"/>
		</fr:layout>
		<fr:destination name="invalid" path='<%= "/caseHandlingOver23CandidacyProcess.do?method=executeInsertResultsFromJuryInvalid&amp;processId=" + processId.toString() %>' />
	</fr:edit>
		
	<html:submit><bean:message key="label.insert" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
