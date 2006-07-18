<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />
	<bean:define id="eventName" name="selectedEvent" property="name.content" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>

	<%-- TITLE --%>		
	<h2/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.useCaseTitle"/> 
	  	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
			<%= eventName %>
		</html:link>
	</h2>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="participations">
		<fr:edit id="participantsTable" name="participations" layout="tabular-editable" schema="eventParticipants.edit-role"
			action="<%= "/events/editEvent.do?method=prepareEditParticipants&eventId=" + eventId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/events/editEvent.do?method=removeParticipant&eventId=" + eventId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantId"/>
				<fr:property name="key(remove)" value="researcher.event.editEvent.participations.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>	
		</fr:edit>
	</logic:notEmpty>
	<br/>
	
	<%-- CREATION OF A NEW PARTICIPATION --%>
	
	

		<logic:notPresent name="external">
			<h3>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewParticipant"/>
				<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=false&eventId="+eventId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.internal" />
				</html:link>				
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.or" />
				<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=true&eventId="+eventId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.external" />
				</html:link>				
			</h3>
		</logic:notPresent>
		<logic:present name="external">
			<logic:equal name="external" value="false">
				<h3>
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewInternalParticipant"/>
				</h3>
				<fr:edit id="simpleBean" name="simpleBean" action="<%="/events/editEvent.do?method=createParticipantInternalPerson&eventId="+eventId%>" schema="eventParticipation.internalPerson.creation">
					<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=false&eventId="+eventId%>"/>	
					<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
				</fr:edit>
			</logic:equal>
			<logic:equal name="external" value="true">
				<h3>
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewExternalParticipant"/>			
				</h3>
				<logic:present name="simpleBean">
					<fr:edit id="simpleBean" name="simpleBean" action="<%="/events/editEvent.do?method=createParticipantExternalPerson&external=true&eventId="+eventId%>" schema="eventParticipation.externalPerson.simpleCreation">
						<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=true&eventId="+eventId%>"/>	
						<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
					</fr:edit>						
				</logic:present>
				<logic:present name="fullBean">
					<fr:edit id="fullBean" name="fullBean" action="<%="/events/editEvent.do?method=createParticipantExternalPerson&eventId="+eventId%>" schema="projectParticipation.externalPerson.fullCreation">
						<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithFullBean&external=true&eventId="+eventId%>"/>
						<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
					</fr:edit>
				</logic:present>
			</logic:equal>
		</logic:present>


	
  	<br/>
	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.goBackToView" />
	</html:link>
</logic:present>
<br/>