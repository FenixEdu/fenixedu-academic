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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(DIRECTIVE_COUNCIL)">


	<h2><bean:message bundle="APPLICATION_RESOURCES" key="label.directiveCouncil.gratuityReports" /></h2>
	
	<fr:hasMessages for="reportParameters" type="conversion">
		<p><span class="error0">			
			<fr:message for="reportParameters" show="message"/>
		</span></p>
	</fr:hasMessages>	
	
	<fr:form action="/gratuityReports.do?method=showReport">
		<fr:edit	id="reportParameters"
				 	name="reportParameters" 
					schema="GratuityReportParametersBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:destination name="invalid" path="/gratuityReports.do?method=showReportInvalid"/>
			</fr:layout>
		</fr:edit>
	
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>

	<br/>

	<fr:view name="report" schema="GratuityReport.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>


	<br/>

	<logic:equal name="reportParameters" property="detailed" value="true">	
		<fr:view name="report" property="entries" schema="GratuityReportEntry.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="sortBy" value="date=DESC" />
			</fr:layout>
		</fr:view>
	</logic:equal>


</logic:present>
