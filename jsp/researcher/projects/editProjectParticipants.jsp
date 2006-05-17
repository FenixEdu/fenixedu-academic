<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.useCaseTitle"/> </h2>
  	
	<br/>
	<br/>
	
	<fr:edit id="participantsTable" name="participations" layout="tabular-editable" schema="projectParticipants.edit-role">
	</fr:edit>


	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="<%="/projects/editProject.do?method=createParticipantSimple&projectId="+projectId%>" schema="projectParticipation.simpleCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:present>
	
	<logic:present name="fullBean">
		<fr:edit id="fullBean" name="fullBean" action="<%="/projects/editProject.do?method=createParticipantFull&projectId="+projectId%>" schema="projectParticipation.fullCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantsSimple&projectId="+projectId%>"/>
		</fr:edit>
	</logic:present>
	
  	
  	<br/>

	
</logic:present>
		
<br/>