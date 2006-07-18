<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />
	<bean:define id="eventName" name="selectedEvent" property="name.content" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>
		
	<%-- TITLE --%>		
	<h2/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participantsUnits.useCaseTitle"/> 
	  	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
			<%= eventName %>
		</html:link>
	</h2>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="unitParticipations">
		<fr:edit id="unitParticipantsTable" name="unitParticipations" layout="tabular-editable" schema="eventParticipantUnits.edit-role">
		
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/events/editEvent.do?method=removeParticipantUnit&eventId=" + eventId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantUnitId"/>
				<fr:property name="key(remove)" value="researcher.event.eventsManagement.role.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>	
		</fr:edit>
	</logic:notEmpty>
	<br/>
	
	<%-- CREATION OF A NEW PARTICIPATION --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewParticipant"/> </h3>
	<logic:present name="bean">
		<fr:edit id="bean" name="bean" action="<%="/events/editEvent.do?method=createParticipantUnit&eventId="+eventId%>" schema="eventUnitParticipation.creation">
			<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantUnits&eventId="+eventId%>"/>	
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>	
		</fr:edit>
	</logic:present>
  	<br/>
	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.goBackToView" />
	</html:link>
</logic:present>


<br/>