<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />
	<bean:define id="eventName" name="selectedEvent" property="name.content" />

	<em>Eventos</em> <!-- tobundle -->

	<%-- TITLE --%>		
	<h2>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.useCaseTitle"/>: <%= eventName %>
		<%--
	  	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"></html:link>
	  	--%>
	</h2>
	
	<ul class="list5 mtop2 mbottom1">
		<li>
			<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.goBackToView" />
			</html:link>
		</li>
	</ul>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="participations">
		<fr:edit id="participantsTable" name="participations" layout="tabular-editable" schema="eventParticipants.edit-role"
			action="<%= "/events/editEvent.do?method=prepareEditParticipants&eventId=" + eventId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/events/editEvent.do?method=removeParticipant&eventId=" + eventId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantId"/>
				<fr:property name="key(remove)" value="researcher.event.editEvent.participations.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>	
		</fr:edit>
	</logic:notEmpty>
	
	<%-- CREATION OF A NEW PARTICIPATION --%>
	
		<logic:notPresent name="external">
			<p class="mtop2">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewParticipant"/>: 
				<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=false&eventId="+eventId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.internal" />
				</html:link>
				, 
				<%--
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.or" />
				--%>
				<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=true&eventId="+eventId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.external" />
				</html:link>				
			</p>
		</logic:notPresent>
		
	
		<logic:present name="external">
			<logic:equal name="external" value="false">
				<p class="mtop2 mbottom05">
					<strong>
						<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewInternalParticipant"/>
					</strong>
				</p>
				<fr:edit id="simpleBean" name="simpleBean" action="<%="/events/editEvent.do?method=createParticipantInternalPerson&eventId="+eventId%>" schema="eventParticipation.internalPerson.creation">
					<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=false&eventId="+eventId%>"/>	
					<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
			 	    <fr:layout name="tabular">
			    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</logic:equal>
			<logic:equal name="external" value="true">
				<p class="mtop2 mbottom05">
					<strong>
						<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants.addNewExternalParticipant"/>			
					</strong>
				</p>
				<logic:present name="simpleBean">
					<fr:edit id="simpleBean" name="simpleBean" action="<%="/events/editEvent.do?method=createParticipantExternalPerson&external=true&eventId="+eventId%>" schema="eventParticipation.externalPerson.simpleCreation">
						<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithSimpleBean&external=true&eventId="+eventId%>"/>	
						<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
				 	    <fr:layout name="tabular">
				    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					    </fr:layout>
					</fr:edit>						
				</logic:present>
				<logic:present name="fullBean">
					<fr:edit id="fullBean" name="fullBean" action="<%="/events/editEvent.do?method=createParticipantExternalPerson&eventId="+eventId%>" schema="projectParticipation.externalPerson.fullCreation">
						<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditParticipantsWithFullBean&external=true&eventId="+eventId%>"/>
						<fr:destination name="cancel" path="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>"/>	
				 	    <fr:layout name="tabular">
				    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					    </fr:layout>
					</fr:edit>
				</logic:present>
			</logic:equal>
		</logic:present>

</logic:present>