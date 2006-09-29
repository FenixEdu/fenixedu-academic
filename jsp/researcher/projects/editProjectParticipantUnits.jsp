<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />
	<bean:define id="projectName" name="selectedProject" property="title.content" />

	<em>Projectos</em> <!-- tobundle -->
		
	<%-- TITLE --%>		
	<h2>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participantsUnits.useCaseTitle"/> <%= projectName %>
	</h2>
	

	<ul class="list5 mvert1">
		<li>
			<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
			</html:link>
		</li>
	</ul>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="unitParticipations">
		<fr:edit id="unitParticipantsTable" name="unitParticipations" layout="tabular-editable" schema="projectParticipantUnits.edit-role">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/projects/editProject.do?method=removeParticipantUnit&projectId=" + projectId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantUnitId"/>
				<fr:property name="key(remove)" value="researcher.project.projectsManagement.role.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
	    	    <fr:property name="classes" value="tstyle1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:notEmpty>
	
	<%-- CREATION OF A NEW PARTICIPATION --%>
	<p class="mtop2 mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.addNewParticipant"/></strong></p>
	<logic:present name="bean">
		<fr:edit id="bean" name="bean" action="<%="/projects/editProject.do?method=createParticipantUnit&projectId="+projectId%>" schema="projectUnitParticipation.creation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantUnits&projectId="+projectId%>"/>	
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
	        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		    </fr:layout>
		</fr:edit>
	</logic:present>
	
</logic:present>
