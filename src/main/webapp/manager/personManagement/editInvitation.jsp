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

<h2><bean:message key="invitation.edit.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="(role(MANAGER) | role(OPERATOR))">

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
		
		<bean:define id="invitedPersonID" name="invitation" property="invitedPerson.externalId" />
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
		<bean:define id="goToChangeInvitationHostUnitDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationHostUnit&invitationID=<bean:write name="invitation" property="externalId"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationHostUnitDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
							
		<%-- Responsible Entity --%>					
		<p><b><bean:message key="label.responsible.party" bundle="MANAGER_RESOURCES"/></b></p>
		<logic:equal name="invitation" property="responsible.class.simpleName" value="Unit">
			<bean:write name="invitation" property="responsible.presentationNameWithParents"/>
		</logic:equal>	
		<logic:equal name="invitation" property="responsible.class.simpleName" value="Person">
			<bean:write name="invitation" property="responsible.name"/>	(<bean:write name="invitation" property="responsible.username"/>)
		</logic:equal>			
		<bean:define id="goToChangeInvitationResponsibleDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationResponsible&invitationID=<bean:write name="invitation" property="externalId"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationResponsibleDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
								
		<%-- Invitation Interval --%>			
		<p><b><bean:message key="label.invitation.time.interval" bundle="MANAGER_RESOURCES"/></b></p>
		<fr:view name="invitation" schema="EditInviteTimeInterval">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>	
		</fr:view>				
		<bean:define id="goToChangeInvitationTimeIntervalDetailsURL" type="java.lang.String">/invitationsManagement.do?method=prepareEditPersonInvitationTimeInterval&invitationID=<bean:write name="invitation" property="externalId"/></bean:define> 		
		<p><html:link page="<%= goToChangeInvitationTimeIntervalDetailsURL %>"><bean:message key="label.change" bundle="MANAGER_RESOURCES"/></html:link></p>		
		
	</logic:notEmpty>		
</logic:present>	