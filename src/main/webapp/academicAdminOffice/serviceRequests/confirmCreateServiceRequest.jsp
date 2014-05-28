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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="label.serviceRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="academicServiceRequestCreateBean" property="registration.student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="academicServiceRequestCreateBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<logic:present name="academicServiceRequestCreateBean" property="registration">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="academicServiceRequestCreateBean" property="registration" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<bean:define id="registrationID" name="academicServiceRequestCreateBean" property="registration.externalId" />

<fr:form action="<%= "/academicServiceRequestsManagement.do?registrationID=" + registrationID.toString() %>">
	<html:hidden property="method" value="createServiceRequest" />
	
	<fr:edit id="academicServiceRequestCreateBean" name="academicServiceRequestCreateBean" visible="false" />
	
	<bean:define id="schema">RegistrationAcademicServiceRequestCreateBean.<bean:write name="academicServiceRequestCreateBean" property="academicServiceRequestType.name"/></bean:define>	

	<h3 class="mbottom05"><strong><bean:message key="message.confirm.create.service" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></h3>
	<fr:view name="academicServiceRequestCreateBean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop15">
		<html:submit><bean:message key="button.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:submit onclick="this.form.method.value='chooseServiceRequestTypePostBack'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='backToViewRegistration'" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</p>
</fr:form>
