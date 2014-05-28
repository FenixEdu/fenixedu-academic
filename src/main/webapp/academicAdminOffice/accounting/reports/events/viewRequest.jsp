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

<html:xhtml />


<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports.request.view" /></h2>

<bean:define id="queueJob" name="queueJob" type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" />

<fr:view name="queueJob">
	<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" bundle="ACADEMIC_OFFICE_RESOURCES">		
		<fr:slot name="beginDate" />
		<fr:slot name="endDate" />

	<logic:notEmpty name="queueJob" property="forExecutionYear" >
		<fr:slot name="forExecutionYear.name" />
	</logic:notEmpty>
		<fr:slot name="exportGratuityEvents" />
		<fr:slot name="exportAcademicServiceRequestEvents" />

	<logic:equal name="queueJob" property="forAdministrativeOffice.hasAnyPhdProgram" value="false">
		<fr:slot name="exportIndividualCandidacyEvents" />
	</logic:equal>
	<logic:equal name="queueJob" property="forAdministrativeOffice.hasAnyPhdProgram" value="true">
		<fr:slot name="exportPhdEvents" />
	</logic:equal>

		<fr:slot name="exportResidenceEvents" />
		<fr:slot name="exportOthers" />

	<logic:notEmpty name="queueJob" property="forAdministrativeOffice" >
		<fr:slot name="forAdministrativeOffice.unit.name" />
	</logic:notEmpty>
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>

</fr:view>

<p>
	<html:link action="/eventReports.do?method=listReports">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:link>
</p>
