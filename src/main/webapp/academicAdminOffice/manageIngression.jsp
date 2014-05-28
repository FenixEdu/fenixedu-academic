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

<bean:define id="registration" name="ingressionBean" property="registration" />


<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="ingressionBean" paramProperty="registration.externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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

<logic:present name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<logic:notPresent name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

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