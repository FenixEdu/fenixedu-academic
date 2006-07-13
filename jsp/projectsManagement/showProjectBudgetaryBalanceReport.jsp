<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/projectReports.tld" prefix="report"%>
<logic:present name="infoReport">
	<bean:define id="code" value="" />
	<logic:present name="infoCostCenter" scope="request">
		<table class="viewHeader">
			<tr>
				<td>
				<h3><bean:write name="infoCostCenter" property="description" /></h3>
				</td>
			</tr>
		</table>
		<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
		<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
	</logic:present>
	<logic:notEmpty name="infoReport" property="infoProject">
		<bean:define id="infoProject" name="infoReport" property="infoProject" />
		<table class="viewHeader" width="100%" cellspacing="0">
			<tr>
				<td class="infoop">
				<h2><bean:message key="title.projectBudgetaryBalanceReport" /></h2>
				</td>
				<logic:notEmpty name="infoReport" property="lines">
					<bean:define id="projectCode" name="infoProject" property="projectCode" />
					<td class="infoop" width="20"><html:link
						page="<%="/projectReport.do?method=exportToExcel&amp;reportType=projectBudgetaryBalanceReport&amp;projectCode="+projectCode+code%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.exportToExcel" align="right" />
					</html:link></td>
				</logic:notEmpty>
			</tr>
		</table>
		<br />
		<table>
			<tr class="printHeader">
				<td rowspan="7"><img height="110" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
					src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
			</tr>
			<tr class="printHeader">
				<td colspan="2">
				<h2><bean:message key="title.projectBudgetaryBalanceReport" /></h2>
				</td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.acronym" />:</strong></td>
				<td><bean:write name="infoProject" property="title" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.projectNumber" />:</strong></td>
				<td><bean:write name="infoProject" property="projectIdentification" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.type" />:</strong></td>
				<bean:define id="type" name="infoProject" property="type" />
				<td><bean:write name="type" property="label" />&nbsp;-&nbsp;<bean:write name="type" property="value" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.coordinator" />:</strong></td>
				<td><bean:write name="infoProject" property="coordinatorName" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.date" />:</strong></td>
				<td><report:computeDate /></td>
			</tr>
		</table>
		<br />
		<br />
		<logic:notEmpty name="infoReport" property="lines">
			<bean:define id="budgetaryBalanceLines" name="infoReport" property="lines" />
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.rubric" /></td>
					<td class="report-hdr"><bean:message key="label.name" /></td>
					<td class="report-hdr"><bean:message key="label.budgeted" /></td>
					<td class="report-hdr"><bean:message key="label.executed" /></td>
					<td class="report-hdr"><bean:message key="label.balance" /></td>
				</tr>
				<logic:iterate id="line" name="budgetaryBalanceLines" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="rubric" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="rubricDescription" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="budget" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="executed" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="balance" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<br />
		<br />
		<br />
	</logic:notEmpty>
</logic:present>
