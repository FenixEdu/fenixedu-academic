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
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeEditCandidacyCurricularCoursesInformation" />

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
	<bean:define id="precedentDegreeTypeName" name="individualCandidacyProcessBean" property="precedentDegreeType.name" />
	<bean:define id="schema"><bean:write name="processName"/>.precedentDegreeInformation.<bean:write name="precedentDegreeTypeName"/></bean:define>

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.precedentCurricularCoursesInformation" bundle="APPLICATION_RESOURCES"/>:</h3>
	<fr:edit id="individualCandidacyProcessBean.precedentCurricularCoursesInformation"
			name="individualCandidacyProcessBean" schema="<%= schema.toString() + ".curricularCourses" %>" 
			nested="true">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
   	    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandling" + processName + ".do?method=executeEditCandidacyCurricularCoursesInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>

	<br/>
	<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
