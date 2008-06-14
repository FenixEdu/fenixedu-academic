<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message name="processName" bundle="CASE_HANDLING_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
	<br />
</html:messages>

<bean:define id="processName" name="processName" />

<fr:edit id="candidacyProcessBean" name="candidacyProcessBean" schema='<%= processName.toString() + "Bean.manage" %>'
		 action='<%= "/caseHandling" + processName.toString() +".do?method=createNewProcess" %>'>
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
        <fr:property name="requiredMarkShown" value="true" />
	</fr:layout>
	
	<fr:destination name="invalid" path='<%= "/caseHandling" + processName.toString() + ".do?method=createNewProcessInvalid" %>' />
	<fr:destination name="cancel" path='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses" %>' />
	
</fr:edit>
<br/>
<em><bean:message key="renderers.validator.required.mark.explanation" bundle="RENDERER_RESOURCES" /></em>

