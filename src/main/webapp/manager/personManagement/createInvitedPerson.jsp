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
			<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>	
		
	<logic:empty name="invitedPersonBean" property="unit">				
		<p class="breadcumbs"><span class="actual"><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.third.step" bundle="MANAGER_RESOURCES"/></span></p>
		<bean:define id="goToPrepareCreateInvitedPersonURL1" type="java.lang.String">/manager/invitationsManagement.do?method=prepareCreateInvitedPerson&name=<bean:write name="invitedPersonBean" property="name"/>&idDocumentType=<bean:write name="invitedPersonBean" property="idDocumentType"/>&documentIdNumber=<bean:write name="invitedPersonBean" property="documentIdNumber"/></bean:define>
		<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= goToPrepareCreateInvitedPersonURL1 %>" state="true"/>										
	</logic:empty>

	<logic:notEmpty name="invitedPersonBean" property="unit">
		<logic:empty name="invitedPersonBean" property="responsible">

			<p class="breadcumbs"><span><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.third.step" bundle="MANAGER_RESOURCES"/></span></p>			
			
			<p class="infoop2">
				<b><bean:message key="label.host.unit" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="invitedPersonBean" property="unit.presentationNameWithParents"/>
			</p>
			
			<br/>				
			&nbsp;&nbsp;<em><bean:message key="label.choose.responsibility.unit" bundle="MANAGER_RESOURCES"/></em>
			<bean:define id="goToPrepareCreateInvitedPersonURL2" type="java.lang.String">/manager/invitationsManagement.do?method=prepareCreateInvitedPerson&name=<bean:write name="invitedPersonBean" property="name"/>&idDocumentType=<bean:write name="invitedPersonBean" property="idDocumentType"/>&documentIdNumber=<bean:write name="invitedPersonBean" property="documentIdNumber"/>&unitID=<bean:write name="invitedPersonBean" property="unit.externalId"/></bean:define>
			<un:tree initialUnit="initialUnit" unitParamName="responsibilityUnitID" path="<%= goToPrepareCreateInvitedPersonURL2 %>" state="true"/>								
						
			<br/>			
			&nbsp;&nbsp;<em><bean:message key="label.choose.responsibility.person" bundle="MANAGER_RESOURCES"/></em>
			<fr:form action="/invitationsManagement.do?">		
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="createInvitedPerson" value="associateResponsibilityParty"/>					
				
				<fr:edit name="invitedPersonBean" id="invitedPersonBeanWithResponsibilityParty" schema="ChooseInvitedPersonResponsibilityPerson" />						
				<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
			</fr:form>	
										
		</logic:empty>
	</logic:notEmpty>	
	
	<logic:notEmpty name="invitedPersonBean" property="unit">
		<logic:notEmpty name="invitedPersonBean" property="responsible">
			
			<p class="breadcumbs"><span><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.create.new.invited.person.third.step" bundle="MANAGER_RESOURCES"/></span></p>
			
			<p class="infoop2">
				<b><bean:message key="label.host.unit" bundle="MANAGER_RESOURCES"/>:</b> 		
				<bean:write name="invitedPersonBean" property="unit.presentationNameWithParents"/>		
				<br/>						
				<logic:notEmpty name="invitedPersonBean" property="responsiblePerson">
					<b><bean:message key="label.responsibility.person" bundle="MANAGER_RESOURCES"/>:</b>
					<bean:write name="invitedPersonBean" property="responsible.name"/> (<bean:write name="invitedPersonBean" property="responsible.username"/>)
				</logic:notEmpty>
				<logic:empty name="invitedPersonBean" property="responsiblePerson">
					<b><bean:message key="label.responsibility.unit" bundle="MANAGER_RESOURCES"/>:</b> 		
					<bean:write name="invitedPersonBean" property="responsible.presentationNameWithParents"/>
				</logic:empty>
			</p>
			
			<br/>			
			<fr:form action="/invitationsManagement.do?method=createNewInvitedPerson">		
							
				<p><b>a) <bean:message key="label.person.identification.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithIdentificationInfo" schema="NewInvitedPersonIdentificationInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>
				</fr:edit>			
							
				<p><b>b) <bean:message key="label.invitedPerson.personal.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithPersonalInfo" schema="NewInvitedPersonPersonalInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>	
				</fr:edit>
								
				<p><b>c) <bean:message key="label.invitedPerson.filiation.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithFiliationInfo" schema="NewInvitedPersonFiliationInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>
				</fr:edit>		
				
				<p><b>d) <bean:message key="label.invitedPerson.residence.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithResidenceInfo" schema="NewInvitedPersonResidenceInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>
				</fr:edit>		
				
				<p><b>e) <bean:message key="label.invitedPerson.contacts.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithContactsInfo" schema="NewInvitedPersonContactsInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>
				</fr:edit>
				
				<p><b>f) <bean:message key="label.invitedPerson.login.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="invitedPersonBean" id="invitedPersonBeanWithLoginInfo" schema="NewInvitedPersonLoginInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/invitationsManagement.do?method=invalid"/>
				</fr:edit>						
							
				<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
			</fr:form>	
			
		</logic:notEmpty>	
	</logic:notEmpty>

</logic:present>