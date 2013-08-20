<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<p>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<%-- student information --%>
<logic:notEmpty name="process" property="personalDetails.student">
	<p class="mbottom05"><strong><bean:message key="label.studentDetails" bundle="APPLICATION_RESOURCES"/></strong></p>
	<fr:view name="process" schema="MobilityIndividualApplicationProcess.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<p>Do want to create and import a user for this candidate?</p>

<fr:form action='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=executeCreateStudentData&processId=" + processId.toString() %>' id="erasmusCandidacyForm">	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>
