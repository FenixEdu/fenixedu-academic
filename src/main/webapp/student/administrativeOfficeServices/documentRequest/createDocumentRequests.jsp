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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" >
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="documentRequestCreateBean" property="registration.student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert15">
	<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="documentRequestCreateBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<logic:present name="documentRequestCreateBean" property="registration.ingression">
<p class="mbottom05"><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="documentRequestCreateBean" property="registration.ingression">
<p class="mbottom05"><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<logic:equal name="documentRequestCreateBean" property="registration.hasGratuityDebtsCurrently" value="true">
	<p>
		<span class="warning0"><bean:message key="DocumentRequest.registration.has.not.payed.gratuities" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
	</p>
</logic:equal>
<logic:equal name="documentRequestCreateBean" property="registration.hasGratuityDebtsCurrently" value="false">
	<fr:form action="/documentRequest.do?method=viewDocumentRequestToCreate" >
		
		<p class="mbottom025"><strong><bean:message key="message.document.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>
		
		<p class="mbottom025 warning0"><bean:message key="message.warning.urgent.request" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
		
		<fr:edit id="documentRequestTypeEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.chooseDocumentRequestType-for-given-registration" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
			<fr:destination name="documentRequestTypeChosenPostBack" path="/documentRequest.do?method=documentRequestTypeChosenPostBack"/>
			<fr:destination name="invalid" path="/documentRequest.do?method=documentRequestTypeInvalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop025 mbottom0 thmiddle"/>
				<fr:property name="columnClasses" value="width14em,width35em,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>
		
		<logic:present name="additionalInformationSchemaName">
			<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
			<fr:edit id="additionalInformationEdit" name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
					<fr:property name="columnClasses" value="width14em,width35em,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/documentRequest.do?method=documentRequestTypeInvalid"/>
			</fr:edit>			
		</logic:present>
		
		<logic:notEmpty name="documentRequestCreateBean" property="chosenDocumentRequestType">
		
			<fr:edit id="purposesEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
					<fr:property name="columnClasses" value="width14em,width35em,tdclear tderror1"/>
				</fr:layout>	
			</fr:edit>
		
		</logic:notEmpty>
		
		<p class="mtop15">
			<html:submit><bean:message key="button.continue"/></html:submit>
		</p>
		
	</fr:form>
</logic:equal>
