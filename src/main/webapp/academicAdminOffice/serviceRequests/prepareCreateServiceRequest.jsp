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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="label.serviceRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="academicServiceRequestCreateBean" property="registration.student.person.username"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/user/photo/"+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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
	<html:hidden property="method" value="confirmCreateServiceRequest" />
	
	<fr:edit id="academicServiceRequestCreateBean" name="academicServiceRequestCreateBean" visible="false" />

	<bean:define id="schema">RegistrationAcademicServiceRequestCreateBean.choose.type</bean:define>
	<logic:notEmpty name="academicServiceRequestCreateBean" property="serviceRequestSchema">
		<bean:define id="schema"><bean:write name="academicServiceRequestCreateBean" property="serviceRequestSchema"/></bean:define>	
	</logic:notEmpty>

	<p class="mbottom025"><strong><bean:message key="message.service.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>
	<fr:edit id="academicServiceRequestCreateBean.type" name="academicServiceRequestCreateBean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop025 mbottom0 thmiddle"/>
			<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/academicServiceRequestsManagement.do?method=chooseServiceRequestTypeInvalid" />
		<fr:destination name="academicServiceRequestTypeChoosedPostBack" path="/academicServiceRequestsManagement.do?method=chooseServiceRequestTypePostBack" />
	</fr:edit>

	<!-- Requested Cycle -->
	<logic:equal name="academicServiceRequestCreateBean" property="hasCycleTypeDependency" value="true">
		<fr:edit id="requestedCycleEdit" name="academicServiceRequestCreateBean">
			<fr:schema  type="org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestCreateBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="requestedCycle" key="label.cycleType" layout="menu-select"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass"
						value="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean$CycleTypeProvider" />
					<fr:property name="eachLayout" value="" />
				</fr:slot>
			</fr:schema>
			

			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
				<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>
	</logic:equal>
	
	<bean:define id="academicServiceRequestCreateBean" name="academicServiceRequestCreateBean" type="org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestCreateBean" />
		
	<p class="mtop15">
		<html:submit><bean:message key="button.confirm"/></html:submit>
		<html:cancel onclick="this.form.method.value='backToViewRegistration'" ><bean:message key="button.cancel"/></html:cancel>
	</p>
</fr:form>