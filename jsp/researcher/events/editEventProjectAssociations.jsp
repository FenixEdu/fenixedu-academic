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
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.projects.useCaseTitle"/> 
	  	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
			<%= eventName %>
		</html:link>
	</h2>  	
	
	<%-- LIST OF EXISTING ASSOCIATIONS --%>	
	<logic:notEmpty name="projectAssociations">
		<fr:edit id="associationsTable" name="projectAssociations" layout="tabular-editable" schema="eventProjectAssociations.edit-role"
			action="<%= "/events/editEvent.do?method=prepareEditProjectAssociations&eventId=" + eventId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/events/editEvent.do?method=removeProjectAssociation&eventId=" + eventId %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="researcher.event.editEvent.projects.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>	
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>				
		</fr:edit>
	</logic:notEmpty>
	<br/>

	<%-- CREATION OF A NEW ASSOCIATION --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.projects.addNewProjectAssociation"/> </h3>
	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="<%="/events/editEvent.do?method=createProjectAssociationSimple&eventId="+eventId%>" schema="eventProjectAssociation.simpleCreation">
			<fr:destination name="invalid" path="<%="/events/editEvent.do?method=prepareEditProjectAssociations&eventId="+eventId%>"/>			
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>				
		</fr:edit>
	</logic:present>
	<logic:present name="fullBean">
		<fr:edit id="fullBean" name="fullBean" action="<%="/events/editEvent.do?method=createProjectAssociationFull&eventId="+eventId%>" schema="eventProjectAssociation.fullCreation">
			<fr:destination name="invalid" path="<%="/events/editEvent.do?method=createProjectAssociationSimple&eventId="+eventId%>"/>		
			<fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>	
		</fr:edit>
	</logic:present>
  	<br/>
  	<html:link page="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.goBackToView" />
	</html:link>
</logic:present>
<br/>