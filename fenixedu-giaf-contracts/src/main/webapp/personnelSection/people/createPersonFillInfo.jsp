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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="create.person.title" bundle="MANAGER_RESOURCES"/></h2>

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

			<br/>			
			<fr:form action="/personnelManagePeople.do?method=createNewPerson">		
							
				<p><b>a) <bean:message key="label.person.identification.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithIdentificationInfo" schema="NewInvitedPersonIdentificationInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>
				</fr:edit>			
							
				<p><b>b) <bean:message key="label.invitedPerson.personal.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithPersonalInfoInfo" schema="NewInvitedPersonPersonalInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>	
				</fr:edit>
								
				<p><b>c) <bean:message key="label.invitedPerson.filiation.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithFiliationInfoFiliation" schema="NewInvitedPersonFiliationInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>
				</fr:edit>		
				
				<p><b>d) <bean:message key="label.invitedPerson.residence.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithResidenceInfoResidence" schema="NewInvitedPersonResidenceInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>
				</fr:edit>		
				
				<p><b>e) <bean:message key="label.invitedPerson.contacts.info" bundle="MANAGER_RESOURCES"/></b></p>			
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithContactsInfoContact" schema="NewInvitedPersonContactsInfo">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>
				</fr:edit>

				<p><b>f) <bean:message key="label.person.relation.with.institution" bundle="MANAGER_RESOURCES"/></b></p>
				<fr:edit nested="true" name="personBean" id="invitedPersonBeanWithContactsInfoInstitution" schema="InternalPersonBeanRelationTypes">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="invalid" path="/personnelManagePeople.do?method=invalid"/>
				</fr:edit>

				<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
			</fr:form>	
