<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<h2><bean:message key="create.invited.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	
	<script language="JavaScript">
		function check(e,v)
		{	
			var contextPath = '<%= request.getContextPath() %>';	
			if (e.style.display == "none")
			  {
			  e.style.display = "";
			  v.src = contextPath + '/images/toggle_minus10.gif';
			  }
			else
			  {
			  e.style.display = "none";
			  v.src = contextPath + '/images/toggle_plus10.gif';
			  }
		}
	</script>
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>	
	
	
	<logic:notEmpty name="invitation">		
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="invitation" property="invitedPerson.name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="invitation" property="invitedPerson.username"/>
		</p>
		
		<ul class="mvert15 list5">
			<li>	
				<html:link page="/invitationsManagement.do?method=prepareEditPersonInvitation" paramId="invitationID" paramName="invitation" paramProperty="idInternal">
					<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
				</html:link>
			</li>
		</ul>		
		<logic:notEmpty name="infoToEdit">	
			
			<bean:define id="goToPrepareEditPage" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitation&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define>
			
			<%-- Invitation Unit --%>	
			<logic:equal name="infoToEdit" value="hostUnit">
				<p><b><bean:message key="label.choose.new.invitation.host.unit" bundle="MANAGER_RESOURCES"/></b></p>
				<bean:define id="editHostUnitURL" type="java.lang.String">/manager/invitationsManagement.do?method=editPersonInvitationHostUnit&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define>
				<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= editHostUnitURL %>" state="true"/>															
			</logic:equal>
	
			<%-- Responsible Entity --%>	
			<logic:equal name="infoToEdit" value="responsibleParty">
				<p><b><bean:message key="label.choose.new.invitation.responsible.entity" bundle="MANAGER_RESOURCES"/></b></p>
								
				<p><em><bean:message key="label.choose.responsibility.unit" bundle="MANAGER_RESOURCES"/></em></p>										
				<bean:define id="editResponsibleURL" type="java.lang.String">/manager/invitationsManagement.do?method=editPersonInvitationResponsible&invitationID=<bean:write name="invitation" property="idInternal"/></bean:define>
				<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= editResponsibleURL %>" state="true"/>								
				
				<p><em><bean:message key="label.choose.responsibility.person" bundle="MANAGER_RESOURCES"/></em></p>		
				<fr:form action="<%= goToPrepareEditPage %>">
					<fr:edit id="EditInvitationResponsibleID" name="invitation" schema="EditInvitationResponsible">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1"/>
					        <fr:property name="columnClasses" value=",,noborder"/>
						</fr:layout>
					</fr:edit>
					<html:submit><bean:message key="label.refresh" bundle="MANAGER_RESOURCES" /></html:submit>	
				</fr:form>		
			</logic:equal>
			
			<%-- Invitation Interval --%>	
			<logic:equal name="infoToEdit" value="timeInterval">
				<p><b><bean:message key="label.choose.new.invitation.time.interval" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:form action="<%= goToPrepareEditPage %>">
					<fr:edit id="EditInviteTimeIntervalID" name="invitation" schema="EditInviteTimeInterval">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1"/>
					        <fr:property name="columnClasses" value=",,noborder"/>
						</fr:layout>	
					</fr:edit>
					<html:submit><bean:message key="label.refresh" bundle="MANAGER_RESOURCES" /></html:submit>	
				</fr:form>			
			</logic:equal>
				
		</logic:notEmpty>	
	</logic:notEmpty>
</logic:present>	