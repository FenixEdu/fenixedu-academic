<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.mainTitle"/></h2>

<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.types"/>:
<table class="tstyle4 thlight mtop05">
	<tr>
		<th class="width12em"><bean:message bundle="RESEARCHER_RESOURCES" key="label.activities" /></th>
		<th><bean:message bundle="RESEARCHER_RESOURCES" key="label.types" /></th>
		<th><bean:message bundle="RESEARCHER_RESOURCES" key="label.roles" /></th>
		<th><bean:message bundle="RESEARCHER_RESOURCES" key="label.roleMessage" /></th>
	</tr>
	<tr>
		<td class="aright bold"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions.types" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.event.additionalInformation" /></td>
	</tr>
	<tr>
		<td class="aright bold"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals" /></td>
		<td style="text-align: center;">-</td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.magazine.additionalInformation" /></td>
	</tr>
	<tr>
		<td class="aright bold"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues" /></td>
		<td style="text-align: center;">-</td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.journalIssues.additionalInformation" /></td>
	</tr>
	<tr>
		<td class="aright bold" rowspan="3"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.scientificOrganizationsAndNetworksRoles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.scientificOrganizationsAndNetworksRoles.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.cooperations.networks.additionalInformation" /></td>
	</tr>
	<tr>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.bilateralCooperationRoles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.bilateralCooperationRoles.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.cooperations.bilateral.additionalInformation" /></td>
	</tr>
	<tr>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.comission" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.comission.roles" /></td>
		<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.cooperations.comission.additionalInformation" /></td>
	</tr>
</table>


<ul class="mtop15 mbottom1 list5">
	<li>
		<bean:message key="researcher.activity.activitiesManagement.createParticipation" bundle="RESEARCHER_RESOURCES"/>: 
		<%-- <html:link page="/activities/createEvent.do?method=prepareEventSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.events" /></html:link>,  --%>
		<html:link page="/activities/createEventEdition.do?method=prepareEventEditionSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions" /></html:link>, 
		<html:link page="/activities/createScientificJournal.do?method=prepareJournalSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals" /></html:link>,
		<html:link page="/activities/createJournalIssue.do?method=prepareJournalIssueSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues" /></html:link>,
		<html:link page="/activities/createCooperation.do?method=prepareCreateCooperationParticipation"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations" /></html:link>
	</li>
</ul>


<logic:notEmpty name="international-events">
	<p id='events' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.international" /></span></strong>
	</p>
	<bean:define id="participations" name="international-events" toScope="request"/>
	<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
	<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="national-events">
	<p id='events' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.national" /></span></strong>
	</p>
	<bean:define id="participations" name="national-events" toScope="request"/>
	<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
	<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
	
</logic:notEmpty>

<logic:notEmpty name="international-eventEditions">
	<p id='events' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.international" /></span></strong>
	</p>
	<bean:define id="participations" name="international-eventEditions" toScope="request"/>
	<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
	<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>
	
<logic:notEmpty name="national-eventEditions">
	<p id='events' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.national" /></span></strong>
	</p>
	<bean:define id="participations" name="national-eventEditions" toScope="request"/>
	<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
	<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
	
</logic:notEmpty>

<logic:notEmpty name="international-journals">
	<p id='scientificJournals' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.international" /></span></strong>
	</p>
	<bean:define id="participations" name="international-journals" toScope="request"/>
	<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
	<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="national-journals">
	<p id='scientificJournals' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.national" /></span></strong>
	</p>
	<bean:define id="participations" name="national-journals" toScope="request"/>
	<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
	<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="international-issues">
	<p id='issues' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.international" /></span></strong>
	</p>
	<bean:define id="participations" name="international-issues" toScope="request"/>
	<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
	<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>
	
<logic:notEmpty name="national-issues">
	<p id='issues' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.national" /></span></strong>
	</p>
	<bean:define id="participations" name="national-issues" toScope="request"/>
	<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
	<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="cooperations">
	<p id='cooperations' class="mtop3 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperationsTitle" /></span></strong>
	</p>
	<bean:define id="participations" name="cooperations" toScope="request"/>
	<bean:define id="forwardTo" value="EditCooperation" toScope="request"/>
	<bean:define id="schema" value="researchCooperationParticipation.summary" toScope="request"/>
	<jsp:include page="researchActivitiesList.jsp"/>
</logic:notEmpty>
