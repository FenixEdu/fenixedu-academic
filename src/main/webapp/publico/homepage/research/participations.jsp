<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

  	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="link.activitiesManagement"/></h2>
		
	<logic:notEmpty name="international-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
		
	</logic:notEmpty>
	
	<logic:notEmpty name="international-eventEditions">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-eventEditions">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
		
	</logic:notEmpty>

	<logic:notEmpty name="international-journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="international-issues">
		<p id='issues' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-issues">
		<p id='issues' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="cooperations">
		<p id='cooperations' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperationsTitle" /></span></strong>
		</p>
		<bean:define id="participations" name="cooperations" toScope="request"/>
		<bean:define id="forwardTo" value="EditCooperation" toScope="request"/>
		<bean:define id="schema" value="researchCooperationParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>