<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
	<br />
</html:messages>

<fr:edit id="over23CandidacyProcessBean" name="over23CandidacyProcessBean"
		 schema="Over23CandidacyProcessBean.manage"
		 action="/caseHandlingOver23CandidacyProcess.do?method=createNewProcess">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="invalid" path="/caseHandlingOver23CandidacyProcess.do?method=createNewProcessInvalid" />
	<fr:destination name="cancel" path="/caseHandlingOver23CandidacyProcess.do?method=listProcesses" />
</fr:edit>
