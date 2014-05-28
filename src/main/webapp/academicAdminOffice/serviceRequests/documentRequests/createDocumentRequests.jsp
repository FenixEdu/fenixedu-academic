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
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %> 
<%@ page import="org.apache.struts.action.ActionMessages"%>

<html:xhtml/>

	<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
		</p>
	</html:messages>
	<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="warning">
		<p>
			<span class="warning0"><!-- Warning messages go here --><bean:write name="message" /></span>
		</p>
	</html:messages>
	
	<p class="mtop2">
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="documentRequestCreateBean" paramProperty="registration.externalId">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
		
	<div style="float: right;">
		<bean:define id="personID" name="documentRequestCreateBean" property="registration.student.person.externalId"/>
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
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
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationDetail.short" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	</logic:present>
	<logic:notPresent name="documentRequestCreateBean" property="registration.ingression">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	</logic:notPresent>
	
	
	<fr:form action="/documentRequestsManagement.do?method=viewDocumentRequestToCreate">
	
	<p class="mbottom025"><strong><bean:message key="message.document.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>
	
		<!-- Choose Document Request Type -->
		<bean:define id="schema" name="documentRequestCreateBean" property="schema" type="java.lang.String"/>
		<fr:edit id="documentRequestTypeEdit" name="documentRequestCreateBean" schema="<%=schema%>" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop025 mbottom0 thmiddle"/>
				<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="documentRequestTypeChosenPostBack" path="/documentRequestsManagement.do?method=documentRequestTypeChosenPostBack"/>
			<fr:destination name="invalid" path="/documentRequestsManagement.do?method=documentRequestTypeInvalid"/>
		</fr:edit>
		
		<!-- Insert additional Information (if any) -->
		<logic:present name="additionalInformationSchemaName">
			<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
			<fr:edit id="additionalInformationEdit" name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
					<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
				</fr:layout>	
				<fr:destination name="executionYearChangedPostBack" path="/documentRequestsManagement.do?method=executionYearToCreateDocumentChangedPostBack"/>
				<fr:destination name="executionPeriodChangedPostBack" path="/documentRequestsManagement.do?method=executionPeriodToCreateDocumentChangedPostBack"/>
				<fr:destination name="invalid" path="/documentRequestsManagement.do?method=documentRequestTypeInvalid"/>
				<fr:destination name="useAllPostBack" path="/documentRequestsManagement.do?method=useAllPostBack"/>
			</fr:edit>
		</logic:present>
		
		<logic:notEmpty name="documentRequestCreateBean" property="chosenDocumentRequestType">
			
			<!-- Requested Cycle -->
			<logic:equal name="documentRequestCreateBean" property="hasCycleTypeDependency" value="true">
				<fr:edit id="requestedCycleEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.requestedCycle" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
			
			<!-- Internationalization -->
			<fr:edit id="languageEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.language" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
					<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
				</fr:layout>	
			</fr:edit>
	
			<!-- Mobility Program -->
			<logic:equal name="documentRequestCreateBean" property="hasMobilityProgramDependency" value="true">
				<fr:edit id="mobilityProgramEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.mobilityProgram" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
	
			<!-- Purposes -->
			<logic:equal name="documentRequestCreateBean" property="hasPurposeNeed" value="true">
				<fr:edit id="purposesEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
			</logic:equal>
	
			<!-- Can be free processed? -->
			<logic:equal name="documentRequestCreateBean" property="chosenDocumentRequestType.canBeFreeProcessed" value="true">
				<fr:edit id="freeProcessedEdit" name="documentRequestCreateBean" schema="DocumentRequestCreateBean.freeProcessed" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
	
		</logic:notEmpty>
		
		<p class="mtop15">
			<html:submit><bean:message key="button.continue"/></html:submit>
		</p>
		
</fr:form>