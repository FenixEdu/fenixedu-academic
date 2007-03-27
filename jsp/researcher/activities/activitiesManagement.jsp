<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
  	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.mainTitle"/></h2>
		
		
	<ul class="mtop15 mbottom1 list5">
		<li> <bean:message key="researcher.activity.activitiesManagement.createParticipation" bundle="RESEARCHER_RESOURCES"/>: 
			<html:link page="/activities/createEvent.do?method=prepareEventSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.events" /></html:link>, 
			<html:link page="/activities/createScientificJournal.do?method=prepareJournalSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals" /></html:link>,	
			<html:link page="/activities/createCooperation.do?method=prepareCreateCooperationParticipation"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations" /></html:link>
		</li>
	</ul>

	<logic:notEmpty name="national-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="international-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
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
	
	<logic:notEmpty name="international-journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
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
	
</logic:present>
		
