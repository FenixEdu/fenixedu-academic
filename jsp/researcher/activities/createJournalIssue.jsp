<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="RESEARCHER">

<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createIssues.mainTitle"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createIssues.useCasetitle"/></h2>


<bean:define id="schema" value="journalIssueCreation" type="java.lang.String"/>

<logic:present name="bean" property="scientificJournal">
	<bean:define id="schema" value="journalIssueCreation.journalSelected" type="java.lang.String"/>
</logic:present>

<fr:edit id="createParticipation "name="bean" schema="<%= schema %>" action="/activities/createJournalIssue.do?method=prepareJournalIssueParticipation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
</fr:edit>

</logic:present>