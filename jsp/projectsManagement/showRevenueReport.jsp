<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-report.tld" prefix="report"%>
<logic:present name="infoRevenueReport">
	<logic:notEmpty name="infoRevenueReport" property="infoProject">
		<bean:define id="infoProject" name="infoRevenueReport" property="infoProject" />
		<table width="100%" cellspacing="0">
			<tr>
				<td class="infoop">
				<h2><bean:message key="title.revenueReport" /></h2>
				</td>
				<logic:notEmpty name="infoRevenueReport" property="lines">
					<bean:define id="projectCode" name="infoProject" property="projectCode" />
					<td class="infoop" width="20"><html:link
						page="<%="/projectReport.do?method=exportToExcel&amp;reportType=revenueReport&amp;projectCode="+projectCode%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.exportToExcel" align="right" />
					</html:link></td>
					<td class="infoop" width="20"><html:link target="_blank"
						page="<%="/projectReport.do?method=getReport&amp;reportType=revenueReport&amp;projectCode="+projectCode+"&amp;print=true"%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/printer.gif"%>" altKey="label.print" align="right" />
					</html:link></td>
				</logic:notEmpty>
			</tr>
		</table>
		<br />
		<table>
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
		<logic:notEmpty name="infoRevenueReport" property="lines">
			<bean:define id="revenueLines" name="infoRevenueReport" property="lines" />
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.idMov" /></td>
					<td class="report-hdr"><bean:message key="label.financialEntity" /></td>
					<td class="report-hdr"><bean:message key="label.rubric" /></td>
					<td class="report-hdr"><bean:message key="label.date" /></td>
					<td class="report-hdr"><bean:message key="label.description" /></td>
					<td class="report-hdr"><bean:message key="label.value" /></td>
				</tr>
				<logic:iterate id="line" name="revenueLines" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="movementId" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="financialEntity" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="rubric" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="date" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="description" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="value" /></td>
					</tr>
				</logic:iterate>
				<tr>
					<td class="report-line-total-first" colspan="5"><bean:message key="label.total" /></td>
					<td class="report-line-total"><report:sumColumn id="revenueLines" column="5" /></td>
				</tr>
			</table>
		</logic:notEmpty>
		<br />
		<br />
		<br />
	</logic:notEmpty>
</logic:present>
