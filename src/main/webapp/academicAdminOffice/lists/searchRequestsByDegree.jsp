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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType"%>
<html:xhtml />

<h2>
	<bean:message key="label.requestListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES" />
</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"> <!-- Error messages go here --> <bean:write name="message" />
		</span>
	</p>
</html:messages>

<fr:form action="/requestListByDegree.do" id="searchForm">
	<html:hidden property="method" value="runSearchAndShowResults" />
	<fr:edit name="degreeByExecutionYearBean" id="degreeByExecutionYearBean">
		<fr:schema
			type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DegreeByExecutionYearBean"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="executionYear" key="label.executionYear.notCapitalized" layout="menu-select-postback" required="true">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="destination" value="postBack" />
			</fr:slot>
			<fr:slot name="degreeType" key="label.degreeType" layout="menu-select-postback" bundle="APPLICATION_RESOURCES">
				<fr:property name="from" value="administratedDegreeTypes" />
				<fr:property name="destination" value="postBack" />
				<fr:property name="eachLayout" value="this-does-not-exist" />
			</fr:slot>
			<fr:slot name="degree" key="label.degree" layout="menu-select-postback">
				<fr:property name="from" value="administratedDegrees" />
				<fr:property name="format" value="${presentationName}" />
				<fr:property name="destination" value="postBack" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="postBack" path="/requestListByDegree.do?method=postBack" />
		<fr:destination name="invalid" path="/requestListByDegree.do?method=prepareSearch" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025 mbottom0" />
			<fr:property name="columnClasses" value="width110px,width600px,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
			<fr:property name="requiredMessageShown" value="false" />
		</fr:layout>
	</fr:edit>

	<bean:define id="documentRequestSearchBean" name="documentRequestSearchBean"
		type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DocumentRequestSearchBean" />
	<fr:edit id="documentRequestSearchBean" name="documentRequestSearchBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DocumentRequestSearchBean"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="academicServiceRequestType" key="label.requestType" layout="menu-select-postback">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests.AcademicServiceRequestTypeProvider" />
				<fr:property name="eachLayout" value="" />
			</fr:slot>
			<fr:slot name="academicServiceRequestSituationType"
				key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.academicServiceRequestSituationType" />
			<fr:slot name="urgentRequest" key="label.only.urgent" />
			<%
			    if (documentRequestSearchBean.getAcademicServiceRequestType() == AcademicServiceRequestType.DOCUMENT) {
			%>
			<fr:slot name="chosenDocumentRequestType"
				key="label.documentRequestsManagement.searchDocumentRequests.documentRequestType" layout="menu-select">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DocumentRequestTypeProvider$QuickDeliveryTypes" />
				<fr:property name="eachLayout" value="" />
			</fr:slot>
			<%
			    }
			%>
		</fr:schema>
		<fr:destination name="postBack" path="/requestListByDegree.do?method=postBack" />
		<fr:destination name="invalid" path="/requestListByDegree.do?method=prepareSearch" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 tgluetop" />
			<fr:property name="columnClasses" value="width110px,width600px,tdclear" />
		</fr:layout>
	</fr:edit>

	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:submit>
	</p>

	<logic:present name="registrationAcademicServiceRequestList">
		<bean:size id="requestListSize" name="registrationAcademicServiceRequestList" />
		<p class="mtop2 mbottom05">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.requestList.total" arg0="<%=requestListSize.toString()%>" />
		</p>

		<logic:greaterThan name="requestListSize" value="0">
			<p class="mvert05">
				<a
					href="javascript:var form = document.getElementById('searchForm');form.method.value='exportInfoToExcel';form.submit();form.method.value='runSearchAndShowResults'">
					<img src="<%=request.getContextPath() + "/images/excel.gif"%>" /> <bean:message key="link.lists.xlsFileToDownload"
						bundle="ACADEMIC_OFFICE_RESOURCES" />
				</a>
			</p>
		</logic:greaterThan>

		<fr:view name="registrationAcademicServiceRequestList">
			<fr:schema type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"
				bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="serviceRequestNumber" key="label.serviceRequestNumber" layout="link">
					<fr:property name="linkFormat"
						value="/academicServiceRequestsManagement.do?backMethod=visualizeRegistration&amp;backAction=student&amp;academicServiceRequestId=${externalId}&amp;method=viewAcademicServiceRequest" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
					<fr:property name="useParent" value="true" />
				</fr:slot>
				<fr:slot name="requestDate" key="label.requestDate" />
				<fr:slot name="description"
					key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.description" />
				<fr:slot name="student.number"
					key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.studentCurricularPlan.student.studentNumber"
					layout="link">
					<fr:property name="linkFormat"
						value="/student.do?method=visualizeRegistration&amp;registrationID=${registration.externalId}" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
					<fr:property name="useParent" value="true" />
				</fr:slot>
				<fr:slot name="student.name"
					key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.studentCurricularPlan.student.person.name" />
				<fr:slot name="registration.degree.presentationName" key="label.degree"/>	
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter tdcenter mtop05" />
			</fr:layout>
		</fr:view>
	</logic:present>

</fr:form>
