<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-report.tld" prefix="report"%>
<logic:present name="infoReport">
	<logic:notEmpty name="infoReport" property="infoProject">
		<bean:define id="infoProject" name="infoReport" property="infoProject" />
		<table width="100%" cellspacing="0">
			<tr>
				<td class="infoop"><bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />
				<h2><bean:message key="<%="title."+reportType%>" /></h2>
				</td>
				<logic:notEmpty name="infoReport" property="lines">
					<bean:define id="projectCode" name="infoProject" property="projectCode" />
					<td class="infoop" width="20"><html:link
						page="<%="/projectReport.do?method=exportToExcel&amp;reportType="+reportType+"&amp;projectCode="+projectCode%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.exportToExcel" align="right" />
					</html:link></td>
					<td class="infoop" width="20"><html:link target="_blank"
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;projectCode="+projectCode+"&amp;print=true"%>">
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
		<br />
		<%double parentTotal = 0;
        double totalJustified = 0;%>
		<logic:notEmpty name="infoReport" property="lines">
			<bean:define id="parentLines" name="infoReport" property="lines" />
			<logic:iterate id="parentLine" name="parentLines">
				<bean:define id="parentValue" name="parentLine" property="parentValue" />
				<%parentTotal = parentTotal + ((Double) pageContext.findAttribute("parentValue")).doubleValue();
        double thisTotal = 0;%>
				<table width="100%" cellspacing="0">
					<tr>
						<td class="listClasses-header"><strong><bean:message key="label.idMov" /></strong></td>
						<td class="listClasses-header"><strong><bean:message key="label.rubric" /></strong></td>
						<td class="listClasses-header"><strong><bean:message key="label.type" /></strong></td>
						<td class="listClasses-header"><strong><bean:message key="label.date" /></strong></td>
						<td class="listClasses-header" colspan="3"><strong><bean:message key="label.description" /></strong></td>
						<td class="listClasses-header"><strong><bean:message key="label.value" /></strong></td>
					</tr>
					<tr>
						<td class="listClasses" align="center"><bean:write name="parentLine" property="parentMovementId" /></td>
						<td class="listClasses" align="center"><bean:write name="parentLine" property="parentRubricId" /></td>
						<td class="listClasses" align="center"><bean:write name="parentLine" property="parentType" /></td>
						<td class="listClasses" align="center"><bean:write name="parentLine" property="parentDate" /></td>
						<td class="listClasses" colspan="3" align="left"><bean:write name="parentLine" property="parentDescription" /></td>
						<td class="listClasses" align="right"><report:formatDoubleValue name="parentLine" property="parentValue" /></td>
					</tr>
				</table>
				<logic:notEmpty name="parentLine" property="movements">
					<br />
					<table class="report-table">
						<tr>
							<td class="report-hdr"><bean:message key="label.idMov" /></td>
							<td class="report-hdr"><bean:message key="label.rubric" /></td>
							<td class="report-hdr"><bean:message key="label.type" /></td>
							<td class="report-hdr"><bean:message key="label.date" /></td>
							<td class="report-hdr"><bean:message key="label.description" /></td>
							<td class="report-hdr"><bean:message key="label.value" /></td>
							<td class="report-hdr"><bean:message key="label.tax" /></td>
							<td class="report-hdr"><bean:message key="label.total" /></td>
						</tr>
						<logic:iterate id="line" name="parentLine" property="movements" indexId="lineIndex">
							<tr>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="movementId" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="rubricId" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="type" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="date" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="description" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="value" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="tax" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="total" /></td>
								<bean:define id="total" name="line" property="total" />
							</tr>
							<%
        thisTotal = Util.projectsManagement.FormatDouble.round(thisTotal + ((Double) pageContext.findAttribute("total")).doubleValue());
        totalJustified = Util.projectsManagement.FormatDouble.round(totalJustified + ((Double) pageContext.findAttribute("total")).doubleValue());%>
						</logic:iterate>
					</table>
					<table>
						<tr>
							<td><strong><bean:message key="<%="label."+reportType%>" />:</strong></td>
							<td><report:formatDoubleValue name="parentValue" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="<%="label.totalExecuted."+reportType%>" />:</strong></td>
							<td><%= Util.projectsManagement.FormatDouble.convertDoubleToString(thisTotal)%></td>
						</tr>
						<tr>
							<td><strong><bean:message key="<%="label.forExecute."+reportType%>" />:</strong></td>
							<td><%= Util.projectsManagement.FormatDouble.convertDoubleToString(((Double) pageContext.findAttribute("parentValue")).doubleValue()
                        - thisTotal)%></td>
						</tr>
					</table>
					<br />
					<br />
					<br />
				</logic:notEmpty>
			</logic:iterate>
			<table align="center">
				<tr>
					<td colspan="2" align="center">
					<h2><bean:message key="<%="label."+reportType+"Resume"%>" /></h2>
					</td>
				</tr>
				<tr>
					<td><strong><bean:message key="<%="label.total."+reportType%>" />:</strong></td>
					<td align="right"><%= Util.projectsManagement.FormatDouble.convertDoubleToString(parentTotal)%></td>
				</tr>
				<tr>
					<td><strong><bean:message key="<%="label.returnsExecuted."+reportType%>" />:</strong></td>
					<td align="right"><%= Util.projectsManagement.FormatDouble.convertDoubleToString(totalJustified)%></td>
				</tr>
				<tr>
					<td><strong><bean:message key="<%="label.toExecute."+reportType%>" />:</strong></td>
					<td align="right"><%= Util.projectsManagement.FormatDouble.convertDoubleToString(parentTotal - totalJustified)%></td>
				</tr>
			</table>
			<br />
			<br />
			<bean:message key="message.listReport" />
			<br />
		</logic:notEmpty>
		<logic:empty name="infoReport" property="lines">
			<span class="error"><bean:message key="message.noMovements" /></span>
		</logic:empty>
		<br />
		<br />
		<br />
	</logic:notEmpty>
</logic:present>
