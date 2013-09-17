<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="activityId" name="researchActivity" property="externalId" toScope="request"/>
<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
<bean:define id="parameter" value="<%= "activityId=" +  activityId + "&amp;forwardTo=" + "prepareEdit" + activityType + "Participants" %>" toScope="request"/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityParticipants"/></h2>

<ul class="list5 mtop2 mbottom1">
	<li>
		<html:link page="<%="/activities/editResearchActivity.do?method=prepare" + activityType + "&activityId="+ activityId%>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.goBack" />
		</html:link>
	</li>
</ul>

<fr:view name="loggedPerson" schema="researchActivityParticipant.participantName">
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle2 thlight"/>
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
		<div class="infoop2">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityParticipants.editRoles"/>
		</div>

		<logic:notEqual name="activityType" value="Cooperation">
			<logic:notPresent name="participationRoleBean">
				<ul class="mvert15">
					<li>
						<html:link page="<%="/activities/editResearchActivity.do?method=prepareCreateNew" + activityType +"ParticipationRole&amp;" + parameter %>">
							<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityParticipants.createNewParticipationRole" />
						</html:link>
					</li>
				</ul>
			</logic:notPresent>
		</logic:notEqual>
	
		<logic:notEmpty name="unableToEdit">
			<div class="error2">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityParticipants.unableToEdit"/>
				<fr:view name="unableToEdit">
					<fr:layout>
						<fr:property name="eachLayout" value="values"/>
						<fr:property name="eachSchema" value="currentParticipant"/>
						<fr:property name="classes" value="mbottom05"/>
					</fr:layout>
				</fr:view>
			</div>
		</logic:notEmpty>

		<jsp:include page="createNewParticipationRole.jsp"/>

		<fr:hasMessages for="participantsTable" type="validation">
			<p><span class="error0"><fr:message for="participantsTable" show="message"/></span></p>
		</fr:hasMessages>

		<fr:form action="<%="/activities/editResearchActivity.do?method=editParticipants&" + parameter %>">
			<fr:edit id="participantsTable" name="participantBeans" layout="tabular-editable" schema="activityParticipants.edit-role">
				<fr:layout>
					<logic:notEqual name="lastRole" value="yes">
						<fr:property name="link(remove)" value="<%= "/activities/editResearchActivity.do?method=removeParticipation&amp;" + parameter %>"/>
						<fr:property name="param(remove)" value="participation.externalId/participationId"/>
						<fr:property name="key(remove)" value="researcher.activity.remove"/>
						<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
					</logic:notEqual>
		    	    <fr:property name="classes" value="tstyle5 tdtop mtop05"/>
				</fr:layout>
				<fr:destination name="invalid" path="<%="/activities/editResearchActivity.do?method=prepareEditParticipants&amp;" + parameter %>"/>
			</fr:edit>
			<html:submit><bean:message key="button.change" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</fr:form>
	</logic:notEmpty>

</logic:notEqual>
