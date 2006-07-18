<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />
	<bean:define id="projectName" name="selectedProject" property="title.content" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<%-- TITLE --%>		
	<h2/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participantsUnits.useCaseTitle"/> 
	  	<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
			<%= projectName %>
		</html:link>
	</h2>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="unitParticipations">
		<fr:edit id="unitParticipantsTable" name="unitParticipations" layout="tabular-editable" schema="projectParticipantUnits.edit-role">
		
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/projects/editProject.do?method=removeParticipantUnit&projectId=" + projectId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantUnitId"/>
				<fr:property name="key(remove)" value="researcher.project.projectsManagement.role.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:notEmpty>
	<br/>
	
	<%-- CREATION OF A NEW PARTICIPATION --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.addNewParticipant"/> </h3>
	<logic:present name="bean">
		<fr:edit id="bean" name="bean" action="<%="/projects/editProject.do?method=createParticipantUnit&projectId="+projectId%>" schema="projectUnitParticipation.creation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantUnits&projectId="+projectId%>"/>	
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:present>
  	<br/>
	<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>


<br/>