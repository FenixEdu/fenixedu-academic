<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="invitation.edit.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
					
	<logic:notEmpty name="invitation">
		
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="invitation" property="invitedPerson.name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="invitation" property="invitedPerson.username"/>
		</p>
		
		<bean:define id="invitedPersonID" name="invitation" property="invitedPerson.idInternal" />
		<ul class="mvert15 list5">
			<li>	
				<html:link page="/invitationsManagement.do?method=managePersonInvitations" paramId="personID" paramName="invitedPersonID">
					<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
				</html:link>
			</li>
		</ul>			
		
		<%-- Invitation Unit --%>			
		<p><b><bean:message key="label.host.unit" bundle="MANAGER_RESOURCES"/></b></p>		
		<bean:write name="invitation" property="hostUnit.presentationNameWithParents"/>		
		<bean:define id="goToChangeInvitationHostUnitDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationHostUnit&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationHostUnitDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
							
		<%-- Responsible Entity --%>					
		<p><b><bean:message key="label.responsible.party" bundle="MANAGER_RESOURCES"/></b></p>
		<logic:equal name="invitation" property="responsible.class.simpleName" value="Unit">
			<bean:write name="invitation" property="responsible.presentationNameWithParents"/>
		</logic:equal>	
		<logic:equal name="invitation" property="responsible.class.simpleName" value="Person">
			<bean:write name="invitation" property="responsible.name"/>	(<bean:write name="invitation" property="responsible.username"/>)
		</logic:equal>			
		<bean:define id="goToChangeInvitationResponsibleDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationResponsible&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationResponsibleDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
								
		<%-- Invitation Interval --%>			
		<p><b><bean:message key="label.invitation.time.interval" bundle="MANAGER_RESOURCES"/></b></p>
		<fr:view name="invitation" schema="EditInviteTimeInterval">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>	
		</fr:view>				
		<bean:define id="goToChangeInvitationTimeIntervalDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationTimeInterval&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationTimeIntervalDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
		
	</logic:notEmpty>		
</logic:present>	