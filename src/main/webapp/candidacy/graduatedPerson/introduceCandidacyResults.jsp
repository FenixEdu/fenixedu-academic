<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.graduatedPerson" bundle="APPLICATION_RESOURCES"/></h2>

<strong><bean:write name="degree" property="presentationName" /></strong>
<br/>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyResultBeans" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />
<bean:define id="degreeId" name="degree" property="externalId" />

<fr:form action='<%= "/caseHandling" + processName + ".do?processId=" + processId.toString() + "&amp;degreeId=" + degreeId.toString() %>'>
 	<html:hidden property="method" value="executeIntroduceCandidacyResults" />

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h3>
	<fr:edit id="individualCandidacyResultBeans"
		name="individualCandidacyResultBeans"
		schema="DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean.introduce.result">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 mtop025"/>
		</fr:layout>
		<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=executeIntroduceCandidacyResultsInvalid&amp;processId=" + processId.toString() + "&amp;degreeId=" + degreeId.toString() %>' />
	</fr:edit>
		
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='prepareExecuteIntroduceCandidacyResults';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
