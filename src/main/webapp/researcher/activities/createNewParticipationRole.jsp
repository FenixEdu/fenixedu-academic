<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="activityId" name="researchActivity" property="externalId" scope="request"/>
<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
<bean:define id="parameter" value="<%= "activityId=" +  activityId + "&amp;forwardTo=" + "prepareEdit" + activityType + "Participants" %>" toScope="request"/>

<bean:define id="schema" value="researchctivityParticipation.newRoleCreation" type="java.lang.String"/>
<logic:equal name="activityType" value="ScientificJournal">
	<bean:define id="schema" value="researchctivityParticipation.newRoleCreation.scientificJournal" type="java.lang.String"/>
</logic:equal>

<%-- CREATION OF A NEW PARTICIPATION ROLE --%>

	<logic:present name="participationRoleBean">
		<div class="mvert15">
			<p class="mbottom025"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityAssociations.addNewParticipationRole"/></strong></p>
			<fr:form action="<%="/activities/editResearchActivity.do?method=createNewParticipationRole&" + parameter%>">
				<fr:edit id="roleBean" action="<%="/activities/editResearchActivity.do?method=createNewParticipationRole&" + parameter%>" name="participationRoleBean" schema="<%= schema %>" >
					<fr:destination name="invalid" path="<%="/activities/editResearchActivity.do?method=prepareCreateNew" + activityType + "ParticipationRole&" + parameter%>"/>	
					<fr:destination name="cancel" path="<%="/activities/editResearchActivity.do?method=prepareEditEventEditionParticipants&" + parameter%>"/>
			 	    <fr:layout name="tabular">
			    	    <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025 mbottom0"/>
			        	<fr:property name="columnClasses" value="width8em,width50em,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
				<table class="tstyle5 gluetop mtop0">
					<tr>
						<th class="width8em"></th>
						<td class="width50em"><html:submit><bean:message key="button.add" bundle="RESEARCHER_RESOURCES"/></html:submit> <html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel></td>
					</tr>
				</table>
			</fr:form>
		</div>
	</logic:present>
