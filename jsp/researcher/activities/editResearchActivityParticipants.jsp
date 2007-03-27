<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<bean:define id="participationId" name="participation" property="idInternal" toScope="request"/>
	<bean:define id="researchActivityName" name="researchActivity" property="name" toScope="request"/>
	<bean:define id="parameter" value="<%= "participationId=" +  participationId %>" toScope="request"/>
	<bean:define id="activityType" name="researchActivity" property="class.simpleName" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.superTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="<%= "researcher.activity.editResearchActivityParticipants." + activityType %>"/>:&nbsp;
	<%= researchActivityName %></h2>
	
	<ul class="list5 mtop2 mbottom1">
		<li>
			<html:link page="<%="/activities/editResearchActivity.do?method=prepare&participationId="+ participationId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.goBack" />
			</html:link>
		</li>
	</ul>
	
	<fr:view name="loggedPerson" schema="researchActivityParticipant.participantName">
		<fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:view>
	
	<logic:equal name="confirm" value="yes">
		<p class="mbottom025 mtop1"><span class="warning0"><bean:message key="researcher.activity.activitiesManagement.delete.editParticipantsUseCase"/></span></p>
		<p class="mbottom1 mtop025"><span class="warning0"><bean:message key="researcher.activity.activitiesManagement.delete.useCase"/></span></p>
		<fr:form action="<%="/activities/activitiesManagement.do?method=delete&forwardTo=EditParticipants&" + parameter%>">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:submit>
		</fr:form>
	</logic:equal>
	
	<logic:notEqual name="confirm" value="yes">
		<logic:messagesPresent message="true">
			<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
				<p>
					<span class="error0"><bean:write name="messages" /></span>
				</p>
			</html:messages>
		</logic:messagesPresent>
		
		<%-- LIST OF EXISTING PARTICIPATIONS --%>	
		<logic:notEmpty name="participantBeans">
			<p class="mtop1 mbottom05">
				<strong><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.activity.editResearchActivityParticipants.editRoles." + activityType%>"/></strong>
			</p>
			<logic:notEmpty name="unableToEdit">
				<div class="error2">
					<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.activity.editResearchActivityParticipants.unableToEdit." + activityType%>"/>
					<fr:view name="unableToEdit">
						<fr:layout>
							<fr:property name="eachLayout" value="values"/>
							<fr:property name="eachSchema" value="currentParticipant"/>
							<fr:property name="classes" value="mbottom05"/>
						</fr:layout>
					</fr:view>
				</div>
			</logic:notEmpty>
			<fr:hasMessages for="participantsTable" type="validation">
				<ul>
					<li><span class="error0"><fr:message for="participantsTable" show="message"/></span></li>
				</ul>
			</fr:hasMessages>
			<fr:form action="<%="/activities/editResearchActivity.do?method=editParticipants&" + parameter %>">
				<fr:edit id="participantsTable" name="participantBeans" layout="tabular-editable" schema="activityParticipants.edit-role">
					<fr:layout>
						<logic:notEqual name="lastRole" value="yes">
							<fr:property name="link(remove)" value="<%= "/activities/editResearchActivity.do?method=removeParticipation&" + parameter %>"/>
							<fr:property name="param(remove)" value="participation.idInternal/participationId"/>
							<fr:property name="key(remove)" value="researcher.activity.remove"/>
							<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
						</logic:notEqual>
			    	    <fr:property name="classes" value="tstyle1"/>
					</fr:layout>
					<fr:destination name="invalid" path="<%="/activities/editResearchActivity.do?method=prepareEditParticipants&" + parameter %>"/>
				</fr:edit>
				<html:submit><bean:message key="button.change" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</fr:form>
		</logic:notEmpty>
		
		<logic:notEqual name="activityType" value="Cooperation">
			<logic:notPresent name="participationRoleBean">
				<ul class="list5 mtop2 mbottom1">
					<li>
						<html:link page="<%="/activities/editResearchActivity.do?method=prepareCreateNewParticipationRole&" + parameter %>">
							<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityParticipants.createNewParticipationRole" />
						</html:link>
					</li>
				</ul>
			</logic:notPresent>
		</logic:notEqual>
		
		<jsp:include page="createNewParticipationRole.jsp"/>
	</logic:notEqual>
</logic:present>