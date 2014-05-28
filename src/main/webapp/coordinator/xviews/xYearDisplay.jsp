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


<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.YearViewBean"%>
<%@page
	import="net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup"%>
<%@page
	import="net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule"%>
<%@page import="java.util.Set"%>
<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>

<html:xhtml />

<jsp:include page="/coordinator/context.jsp" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/CSS/gwt/xviews/executionYear.css">

<script language="javascript"
	src="<%=request.getContextPath() + "/gwt/XviewsYear/raphael-r1.5.2.js"%>">
</script>
<script language="javascript"
	src="<%=request.getContextPath() + "/gwt/XviewsYear/XviewsYear.nocache.js"%>">
</script>
<style type="text/css">
#gwt_content {
	width: 1000px;
	height: 1200px;
}
</style>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />

<bean:define id="dcpEId" name="dcpEId" />
<bean:define id="eyEId" name="eyEId" />
<input type='hidden' id='dcpId' value='<%=dcpEId.toString()%>' />
<input type='hidden' id='eyId' value='<%=eyEId.toString()%>' />

<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears" /></h2>

<fr:form id="searchForm"
	action="<%= "/xYear.do?method=showYearInformation&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() + "&locale=" + I18N.getLocale().getLanguage() %>">
	<fr:edit id="searchFormBean" name="searchFormBean">
		<fr:schema
			type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.YearViewBean"
			bundle="COORDINATOR_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select"
				key="label.executionYear" required="true">
				<fr:property name="format" value="${qualifiedName}" />
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsForDegreeCurricularPlanProvider" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="COORDINATOR_RESOURCES" key="button.show" />
	</html:submit>
</fr:form>

<div id="gwt_overlay" class="ExecutionYear-Overlay"></div>
<div id="gwt_shader" class="ExecutionYear-Shader"></div>
<div id="gwt_content"></div>

<%-- INAR -- %>
<logic:present name="totalInar">
	<fr:view name="totalInar">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thleft" />
			<fr:property name="headerClasses" value="," />
			<fr:property name="columnClasses" value="," />
		</fr:layout>
		<fr:schema
			type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar"
			bundle="COORDINATOR_RESOURCES">
			<fr:slot name="enrolled" key="label.enrolled" />
			<fr:slot name="frequenting" key="label.frequenting" />
			<fr:slot name="approved" key="label.approved" />
			<fr:slot name="notEvaluated" key="label.notEvaluated" />
			<fr:slot name="flunked" key="label.flunked" />
			<fr:slot name="checksum" key="label.checksum" />
		</fr:schema>
	</fr:view>
</logic:present>

<%-- INARs by CurricularYear -- %>
<logic:present name="#years">
	<bean:define id="years" name="#years" />
	<table>
		<tr>
			<%
			    for (int i = 1; i <= (Integer) years; i++) {
			%>
			<th><bean:message key="<%= "label.InarFor"+i+"Year" %>"
				bundle="COORDINATOR_RESOURCES" /></th>
			<%
			    }
			%>
		</tr>
		<tr>
			<%
			    for (int i = 1; i <= (Integer) years; i++) {
			%>
			<td><fr:view name="<%= "InarFor"+i+"Year" %>">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thleft" />
					<fr:property name="headerClasses" value="," />
					<fr:property name="columnClasses" value="," />
				</fr:layout>
				<fr:schema
					type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar"
					bundle="COORDINATOR_RESOURCES">
					<fr:slot name="enrolled" key="label.enrolled" />
					<fr:slot name="frequenting" key="label.frequenting" />
					<fr:slot name="approved" key="label.approved" />
					<fr:slot name="notEvaluated" key="label.notEvaluated" />
					<fr:slot name="flunked" key="label.flunked" />
					<fr:slot name="checksum" key="label.checksum" />
				</fr:schema>
			</fr:view></td>
			<%
			    }
			%>
		</tr>
	</table>
</logic:present>




<logic:present name="averageByYear">
	<fr:view name="averageByYear">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thleft" />
			<fr:property name="headerClasses" value="," />
			<fr:property name="columnClasses" value="," />
		</fr:layout>
		<fr:schema type="java.lang.Object" bundle="COORDINATOR_RESOURCES">
			<fr:slot name="key.year" key="label.curricularYear"
				layout="integer-prefixsuffix">
				<fr:property name="suffix" value="label.ordinalYears" />
				<fr:property name="bundle" value="COORDINATOR_RESOURCES" />
			</fr:slot>
			<fr:slot name="value" key="label.average" />
		</fr:schema>
	</fr:view>
</logic:present>


<%-- Segmented by branches -- %>
<logic:present name="yearViewBean">
	<%
	    YearViewBean beanie = (YearViewBean) request.getAttribute("yearViewBean");
	%>
	<bean:define id="hasMajors" name="yearViewBean"
		property="hasMajorBranches" />
	<logic:equal name="hasMajors" value="true">
		<bean:define id="majorBranches" name="yearViewBean"
			property="majorBranches" />
		<bean:message key="label.majorBranches" bundle="COORDINATOR_RESOURCES" />
		<table>
			<tr>
				<logic:iterate id="branch" name="majorBranches">
					<th><bean:write name="branch" property="name" /></th>
				</logic:iterate>
			</tr>
			<tr>
				<%
				    for (BranchCourseGroup branch : (Set<BranchCourseGroup>) majorBranches) {
				%>
				<%
				    Inar inar = beanie.getInarByMajorBranches().get(branch);
				%>
				<%
				    request.setAttribute("inar", inar);
				%>
				<td><fr:view name="inar">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thleft" />
						<fr:property name="headerClasses" value="," />
						<fr:property name="columnClasses" value="," />
					</fr:layout>
					<fr:schema
						type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar"
						bundle="COORDINATOR_RESOURCES">
						<fr:slot name="enrolled" key="label.enrolled" />
						<fr:slot name="frequenting" key="label.frequenting" />
						<fr:slot name="approved" key="label.approved" />
						<fr:slot name="notEvaluated" key="label.notEvaluated" />
						<fr:slot name="flunked" key="label.flunked" />
						<fr:slot name="checksum" key="label.checksum" />
					</fr:schema>
				</fr:view></td>
				<%
				    }
				%>
			</tr>
			<tr>
				<%
				    for (BranchCourseGroup branch : (Set<BranchCourseGroup>) majorBranches) {
				%>
				<%
				    String average = beanie.getAverageByMajorBranches().get(branch);
				%>
				<%
				    request.setAttribute("average", average);
				%>
				<td><bean:message key="label.averagePrefix"
					bundle="COORDINATOR_RESOURCES" /><bean:write name="average" /></td>
				<%
				    }
				%>
			</tr>
		</table>
	</logic:equal>

	<bean:define id="hasMinors" name="yearViewBean"
		property="hasMinorBranches" />
	<logic:equal name="hasMinors" value="true">
		<bean:define id="minorBranches" name="yearViewBean"
			property="minorBranches" />
		<bean:message key="label.minorBranches" bundle="COORDINATOR_RESOURCES" />
		<table>
			<tr>
				<logic:iterate id="branch" name="minorBranches">
					<th><bean:write name="branch" property="name" /></th>
				</logic:iterate>
			</tr>
			<tr>
				<%
				    for (BranchCourseGroup branch : (Set<BranchCourseGroup>) minorBranches) {
				%>
				<%
				    Inar inar = beanie.getInarByMinorBranches().get(branch);
				%>
				<%
				    request.setAttribute("inar", inar);
				%>
				<td><fr:view name="inar">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thleft" />
						<fr:property name="headerClasses" value="," />
						<fr:property name="columnClasses" value="," />
					</fr:layout>
					<fr:schema
						type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar"
						bundle="COORDINATOR_RESOURCES">
						<fr:slot name="enrolled" key="label.enrolled" />
						<fr:slot name="frequenting" key="label.frequenting" />
						<fr:slot name="approved" key="label.approved" />
						<fr:slot name="notEvaluated" key="label.notEvaluated" />
						<fr:slot name="flunked" key="label.flunked" />
						<fr:slot name="checksum" key="label.checksum" />
					</fr:schema>
				</fr:view></td>
				<%
				    }
				%>
			</tr>
			<tr>
				<%
				    for (BranchCourseGroup branch : (Set<BranchCourseGroup>) minorBranches) {
				%>
				<%
				    String average = beanie.getAverageByMinorBranches().get(branch);
				%>
				<%
				    request.setAttribute("average", average);
				%>
				<td><bean:message key="label.averagePrefix"
					bundle="COORDINATOR_RESOURCES" /><bean:write name="average" /></td>
				<%
				    }
				%>
			</tr>
		</table>
	</logic:equal>
</logic:present>


<%-- Resumed QUC -- %>
<logic:present name="yearViewBean">
	<bean:message key="label.resumedQUC" bundle="COORDINATOR_RESOURCES" />
	<fr:view name="yearViewBean" property="resumedQUC" />
</logic:present>
--%>
