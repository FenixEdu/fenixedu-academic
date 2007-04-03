<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="RESEARCHER">

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createIssues.useCasetitle"/></h2>


<bean:define id="schema" value="journalIssueCreation" type="java.lang.String"/>

<logic:present name="bean" property="scientificJournal">
	<bean:define id="schema" value="journalIssueCreation.journalSelected" type="java.lang.String"/>
</logic:present>

<bean:define id="createJournal" value="false"/>

<logic:notPresent name="newJournal">
<logic:notPresent name="bean" property="scientificJournal">
	<logic:present name="bean" property="scientificJournalName">
		<bean:define id="createJournal" value="true"/>
	</logic:present>
</logic:notPresent>
</logic:notPresent>

<logic:equal name="createJournal" value="true">
		<div class="warning0">
			<strong><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</strong><br/>
			<bean:message key="label.informationForCreateMagazine" bundle="RESEARCHER_RESOURCES"/>
		</div>
</logic:equal>

<div class="dinline forminline">
<fr:form action="/activities/createJournalIssue.do?method=prepareJournalIssueParticipation">
	<logic:notPresent name="issueBean">
	<fr:edit id="createParticipation" name="bean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 dinline"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit><br/>
	<html:submit><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:notPresent>
	<logic:present name="issueBean">
	<fr:edit id="name" name="bean" visible="false"/>
	<fr:edit id="issueBean" name="issueBean" visible="false"/>
	<fr:edit id="issueJournalData" name="issueBean" schema="result.publication.create.Article.createMagazine">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 dinline"/>
		</fr:layout>
	</fr:edit>
	<br/>

	<bean:define id="issueSchema" value="result.publication.create.Article.createIssue"/>
	<logic:equal name="issueBean" property="specialIssue" value="true">
		<bean:define id="issueSchema" value="result.publication.create.Article.createSpecialIssue"/>
	</logic:equal>

	<fr:edit id="issueIssueData" name="issueBean" schema="<%= issueSchema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 dinline"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="postBack" path="/activities/createJournalIssue.do?method=createPostback"/>
	</fr:edit>
	<br/>
	<html:submit property="createNewJournal"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
	<logic:equal name="createJournal" value="true">
		<html:submit property="prepareCreateNewJournal"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:equal>
</fr:form>
<fr:form action="/activities/createJournalIssue.do?method=prepareJournalIssueParticipation">
	<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
</fr:form>
</div>
</logic:present>