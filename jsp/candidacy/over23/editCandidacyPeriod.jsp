<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="processId" name="process" property="idInternal" />

<fr:edit id="over23CandidacyProcessBean"
	name="over23CandidacyProcessBean"
	schema="Over23CandidacyProcessBean.manage"
	action="<%= "/caseHandlingOver23CandidacyProcess.do?method=executeEditCandidacyPeriod&amp;processId=" + processId %>">
	<fr:layout name="tabular-editable">
	</fr:layout>

	<fr:destination name="cancel"
		path="<%= "/caseHandlingOver23CandidacyProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId %>" />
</fr:edit>
