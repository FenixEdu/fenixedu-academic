<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><strong><bean:message key="link.student.manageIngressionAndAgreement" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<bean:define id="registration" name="ingressionBean" property="registration" toScope="request" />
<bean:define id="registrationId" name="registration" property="externalId" />



<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="ingressionBean" paramProperty="registration.externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.username"/>
	<html:img align="middle" src="<%= request.getContextPath() + "/user/photo/" + personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<jsp:include page="student/registration/registrationDetailsTable.jsp"/>

<bean:define id="registrationID" name="ingressionBean" property="registration.externalId" />

<fr:form action='<%= "/manageIngression.do?registrationId=" + registrationID.toString() %>'>
	<html:hidden property="method" value="editIngression" />

	<logic:equal name="ingressionBean" property="requestAgreementInformation" value="false">
		<fr:edit name="ingressionBean" schema="ingression.information" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="agreementPostBack" path="/manageIngression.do?method=postBack" />
			<fr:destination name="ingressionPostBack" path="/manageIngression.do?method=postBack" />
			<fr:destination name="entryPhasePostBack" path="/manageIngression.do?method=postBack" />
		</fr:edit>
	</logic:equal>
	
	<logic:equal name="ingressionBean" property="requestAgreementInformation" value="true">
		<fr:edit name="ingressionBean" schema="ingression.information.requestAgreementInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="agreementPostBack" path="/manageIngression.do?method=postBack" />
			<fr:destination name="ingressionPostBack" path="/manageIngression.do?method=postBack" />
			<fr:destination name="entryPhasePostBack" path="/manageIngression.do?method=postBack" />
		</fr:edit>
	</logic:equal>

	<html:submit><bean:message key="label.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='prepare'; return true;"><bean:message key="label.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>

</fr:form>

<h3 class="mtop3"><bean:message key="title.registrationIngression.reingressions" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<logic:empty name="registration" property="reingressions">
	<em><bean:message key="message.registrationIngression.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="registration" property="reingressions">
	<fr:view name="registration" property="reingressions" schema="RegistrationReingression.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 mtop15" />
			
			<fr:link name="delete" label="label.delete,APPLICATION_RESOURCES" confirmation="message.registrationIngression.delete.registration.confirmation,ACADEMIC_OFFICE_RESOURCES" 
				link="<%= String.format("/manageIngression.do?method=deleteReingression&registrationId=%s&executionYearId=${executionYear.externalId}", registrationId) %>" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p class="mtop15">
	<html:link action="/manageIngression.do?method=prepareCreateReingression" paramId="registrationId" paramName="registration" paramProperty="externalId">
		<bean:message key="link.registrationIngression.mark.reingression" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>
