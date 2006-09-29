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
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.useCaseTitle"/> <%= projectName %>
	</h2>
	

	<ul class="list5 mtop2 mbottom1">
		<li>
			<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
			</html:link>
		</li>
	</ul>
	
	<%-- LIST OF EXISTING PARTICIPATIONS --%>	
	<logic:notEmpty name="participations">
		<fr:edit id="participantsTable" name="participations" layout="tabular-editable" schema="projectParticipants.edit-role"
			action="<%= "/projects/editProject.do?method=prepareEditParticipants&projectId=" + projectId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/projects/editProject.do?method=removeParticipant&projectId=" + projectId %>"/>
				<fr:property name="param(remove)" value="idInternal/participantId"/>
				<fr:property name="key(remove)" value="researcher.project.editProject.participations.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:notEmpty>

	
	<%-- CREATION OF A NEW PARTICIPATION --%>
		<logic:notPresent name="external">
			<p class="mtop2">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.addNewParticipant"/>:
				<html:link page="<%="/projects/editProject.do?method=prepareEditParticipantsWithSimpleBean&external=false&projectId="+projectId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.internal" />
				</html:link>		
				, 						
				<html:link page="<%="/projects/editProject.do?method=prepareEditParticipantsWithSimpleBean&external=true&projectId="+projectId%>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.external" />
				</html:link>				
			</p>
		</logic:notPresent>
		<logic:present name="external">
			<logic:equal name="external" value="false">
				<p class="mtop2 mbottom05">
					<strong>
						<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.addNewInternalParticipant"/>
					</strong>
				</p>
				<fr:edit id="simpleBean" name="simpleBean" action="<%="/projects/editProject.do?method=createParticipantInternalPerson&projectId="+projectId%>" schema="projectParticipation.internalPerson.creation">
					<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantsWithSimpleBean&external=false&projectId="+projectId%>"/>	
					<fr:destination name="cancel" path="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>"/>	
			 	    <fr:layout name="tabular">
			    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</logic:equal>
			<logic:equal name="external" value="true">
				<p class="mtop2 mbottom05">
					<strong>
						<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.addNewExternalParticipant"/>			
					</strong>
				</p>
				<logic:present name="simpleBean">
					<fr:edit id="simpleBean" name="simpleBean" action="<%="/projects/editProject.do?method=createParticipantExternalPerson&external=true&projectId="+projectId%>" schema="projectParticipation.externalPerson.simpleCreation">
						<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantsWithSimpleBean&external=true&projectId="+projectId%>"/>	
						<fr:destination name="cancel" path="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>"/>	
				 	    <fr:layout name="tabular">
				    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					    </fr:layout>
					</fr:edit>						
				</logic:present>
				<logic:present name="fullBean">
					<fr:edit id="fullBean" name="fullBean" action="<%="/projects/editProject.do?method=createParticipantExternalPerson&projectId="+projectId%>" schema="projectParticipation.externalPerson.fullCreation">
						<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditParticipantsWithFullBean&external=true&projectId="+projectId%>"/>
						<fr:destination name="cancel" path="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>"/>	
				 	    <fr:layout name="tabular">
				    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					    </fr:layout>
					</fr:edit>
				</logic:present>
			</logic:equal>
		</logic:present>

</logic:present>