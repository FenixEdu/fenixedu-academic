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
<%@page import="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJobType"%>

<html:xhtml />
<logic:present role="role(MANAGER)">

	<h2><bean:message bundle="MANAGER_RESOURCES" key="title.gratuity.reports" /></h2>

	<fr:form action="/gratuityReports.do?method=generateReport">
		<fr:edit id="gratuity.report.bean" name="gratuityReportBean" visible="false" />
		
		<fr:edit id="gratuity.report.bean.select.type" name="gratuityReportBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportBean">
				<fr:slot name="type" layout="menu-postback">
					<fr:property name="destination" value="postback" />
				</fr:slot>		
			</fr:schema>
			
			<fr:destination name="postback" path="/gratuityReports.do?method=generateReportPostback" />
		</fr:edit>
		
		<h3><bean:message bundle="MANAGER_RESOURCES" key="title.gratuity.reports.by.execution.year" /></h3>
		<fr:edit id="gratuity.report.bean.execution.year" name="gratuityReportBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportBean">
				<fr:slot name="executionYear" layout="menu-select" >
					<fr:property name="providerClass"
						value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
				<fr:property name="columnClasses" value=",acenter,aright,aright" />			
			</fr:layout>
		</fr:edit>
		
		<logic:equal name="gratuityReportBean" property="type" value="<%= GratuityReportQueueJobType.DATE_INTERVAL.name() %>">
		
			<h3><bean:message bundle="MANAGER_RESOURCES" key="title.gratuity.reports.by.date.interval" /></h3>
			<fr:edit id="gratuity.report.date.intervals" name="gratuityReportBean">
				<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportBean">
					<fr:slot name="beginDate" />
					<fr:slot name="endDate" />
				</fr:schema>
			</fr:edit>
		</logic:equal>

		<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
	</fr:form>
	
</logic:present>