<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>

<h2><bean:message key="create.invited.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="(role(MANAGER) | role(OPERATOR))">
	
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
				<html:link page="/invitationsManagement.do?method=prepareEditPersonInvitation" paramId="invitationID" paramName="invitation" paramProperty="externalId">
					<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
				</html:link>
			</li>
		</ul>		
		<logic:notEmpty name="infoToEdit">	
			
			<bean:define id="goToPrepareEditPage" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitation&invitationID=<bean:write name="invitation" property="externalId"/></bean:define>
			
			<%-- Invitation Unit --%>	
			<logic:equal name="infoToEdit" value="hostUnit">
				<p><b><bean:message key="label.choose.new.invitation.host.unit" bundle="MANAGER_RESOURCES"/></b></p>
				<bean:define id="editHostUnitURL" type="java.lang.String">/manager/invitationsManagement.do?method=editPersonInvitationHostUnit&invitationID=<bean:write name="invitation" property="externalId"/></bean:define>
				<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= editHostUnitURL %>" state="true"/>															
			</logic:equal>
	
			<%-- Responsible Entity --%>	
			<logic:equal name="infoToEdit" value="responsibleParty">
				<p><b><bean:message key="label.choose.new.invitation.responsible.entity" bundle="MANAGER_RESOURCES"/></b></p>
								
				<p><em><bean:message key="label.choose.responsibility.unit" bundle="MANAGER_RESOURCES"/></em></p>										
				<bean:define id="editResponsibleURL" type="java.lang.String">/manager/invitationsManagement.do?method=editPersonInvitationResponsible&invitationID=<bean:write name="invitation" property="externalId"/></bean:define>
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