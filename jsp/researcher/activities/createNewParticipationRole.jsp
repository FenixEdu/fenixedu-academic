<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="activityId" name="researchActivity" property="idInternal" scope="request"/>
<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
<bean:define id="parameter" value="<%= "activityId=" +  activityId + "&amp;forwardTo=" + "prepareEdit" + activityType + "Participants" %>" toScope="request"/>

<%-- CREATION OF A NEW PARTICIPATION ROLE --%>
	<logic:present name="participationRoleBean">
		<p class="mtop2 mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityAssociations.addNewParticipationRole"/></strong></p>
		<fr:form action="<%="/activities/editResearchActivity.do?method=createNewParticipationRole&" + parameter%>">
			<fr:edit id="roleBean" action="<%="/activities/editResearchActivity.do?method=createNewParticipationRole&" + parameter%>" name="participationRoleBean" schema="researchctivityParticipation.newRoleCreation" >
				<fr:destination name="invalid" path="<%="/activities/editResearchActivity.do?method=prepareCreateNewParticipationRole&" + parameter%>"/>	
				<fr:destination name="cancel" path="<%="/activities/editResearchActivity.do?method=prepareEditParticipants&" + parameter%>"/>
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			    </fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.add" bundle="RESEARCHER_RESOURCES"/></html:submit>
			<html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel>
		</fr:form>
	</logic:present>
