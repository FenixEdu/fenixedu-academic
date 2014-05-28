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

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports.request.create" /></h2>

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>


<p><strong><bean:message key="message.event.reports.request.parameters" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>


<fr:form action="/eventReports.do?method=createReportRequest">
	
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
	
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
			<fr:slot name="beginDate" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
			</fr:slot>
			<fr:slot name="endDate" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
			</fr:slot>
			<fr:slot name="administrativeOffice" layout="menu-select-postback" key="label.academicAdminOffice" required="true">
				<fr:property name="from" value="availableOffices" />
				<fr:property name="format" value="${unit.name}" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	
		<fr:destination name="postback" path="/eventReports.do?method=createReportRequestPostback" />
		<fr:destination name="invalid" path="/eventReports.do?method=createReportRequestInvalid" />
		<fr:destination name="cancel" path="/eventReports.do?method=listReports" />

	</fr:edit>

	<logic:present name="bean" property="administrativeOffice">

	<fr:edit id="bean-edit-options" name="bean">
	
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
			
				<fr:slot name="executionYear" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			
				<fr:slot name="exportGratuityEvents" required="true" />
				<fr:slot name="exportAcademicServiceRequestEvents" required="true" />
			<logic:equal name="bean" property="administrativeOffice.hasAnyPhdProgram" value="false">
				<fr:slot name="exportIndividualCandidacyEvents" required="true" />
			</logic:equal>

			<logic:equal name="bean" property="administrativeOffice.hasAnyPhdProgram" value="true">
				<fr:slot name="exportPhdEvents" required="true" />
			</logic:equal>

			<fr:slot name="exportOthers" required="true" />
			
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	
	</fr:edit>

	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	
	</logic:present>
	
	<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
