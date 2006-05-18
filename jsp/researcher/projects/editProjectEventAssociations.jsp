<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.events.useCaseTitle"/> </h2>
  	
	<br/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editEventsAssociationsExplanation"/>	
	<br/>
	
	<logic:notEmpty name="eventAssociations">
	<fr:edit id="associationsTable" name="eventAssociations" layout="tabular-editable" schema="projectEventAssociations.edit-role">
		<fr:layout>
			<fr:property name="link(remove)" value="<%= "/projects/editProject.do?method=removeEventAssociation&projectId=" + projectId %>"/>
			<fr:property name="param(remove)" value="idInternal/oid"/>
			<fr:property name="key(remove)" value="researcher.project.editProject.events.remove"/>
			<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
		</fr:layout>	
	</fr:edit>
	</logic:notEmpty>


	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="<%="/projects/editProject.do?method=createEventAssociationSimple&projectId="+projectId%>" schema="projectEventAssociation.simpleCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditEventAssociations&projectId="+projectId%>"/>			
		</fr:edit>
	</logic:present>
	
	<logic:present name="fullBean">
		<fr:edit id="fullBean" name="fullBean" action="<%="/projects/editProject.do?method=createEventAssociationFull&projectId="+projectId%>" schema="projectEventAssociation.fullCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=createEventAssociationSimple&projectId="+projectId%>"/>		
		</fr:edit>
	</logic:present>
	
  	
  	<br/>

	
</logic:present>
		
<br/>