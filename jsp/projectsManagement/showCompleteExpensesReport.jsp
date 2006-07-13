<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/projectReports.tld" prefix="report"%>
<logic:present name="infoExpensesReport">
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
	<logic:notEmpty name="infoExpensesReport" property="infoProject">
		<bean:define id="infoProject" name="infoExpensesReport" property="infoProject" />
		<table class="viewHeader" width="100%" cellspacing="0">
			<tr>
				<td class="infoop">
				<h2><bean:message key="title.completeExpensesReport" /></h2>
				</td>
				<logic:notEmpty name="infoExpensesReport" property="lines">
					<bean:define id="projectCode" name="infoProject" property="projectCode" />
					<bean:define id="rubric2url" value="" />
					<logic:present name="rubric" scope="request">
						<bean:define id="rubric" value="<%=request.getAttribute("rubric").toString()%>" />
						<bean:define id="rubric2url" value="<%="&amp;rubric="+rubric%>" />
					</logic:present>
					<td class="infoop" width="20"><html:link
						page="<%="/projectReport.do?method=exportToExcel&amp;reportType=completeExpensesReport&amp;projectCode="+projectCode+rubric2url+code%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="excel" bundle="IMAGE_RESOURCES" align="right" />
					</html:link></td>
				</logic:notEmpty>
			</tr>
			<tr>
				<td><span class="error"><bean:message key="message.printLayoutOrientation" /></span></td>
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
				<h2><bean:message key="title.completeExpensesReport" /></h2>
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
		<logic:notEmpty name="infoExpensesReport" property="rubricList">
			<br />
			<br />
			<html:form action="/projectReport">
				<html:hidden property="method" value="getReport" />
				<html:hidden property="reportType" value="completeExpensesReport" />
				<html:hidden property="projectCode" value="<%=(pageContext.findAttribute("projectCode")).toString()%>" />
				<logic:present name="infoCostCenter" scope="request">
					<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
					<html:hidden property="costCenter" value="<%=cc.toString()%>" />
				</logic:present>
				<table>
					<tr>
						<td><html:select property="rubric">
							<html:option value="">
								<bean:message key="label.allRubrics" />
							</html:option>
							<logic:iterate id="rubric" name="infoExpensesReport" property="rubricList">
								<bean:define id="r" name="rubric" property="label" />
								<html:option value="<%=r.toString()%>">
									<bean:write name="rubric" property="label" />
									&nbsp;-&nbsp;
									<bean:write name="rubric" property="value" />
								</html:option>
							</logic:iterate>
						</html:select></td>
						<td><html:submit styleClass="inputbutton">
							<bean:message key="label.select" />
						</html:submit></td>
					</tr>
				</table>
			</html:form>
		</logic:notEmpty>
		<br />
		<br />
		<logic:notEmpty name="infoExpensesReport" property="lines">
			<bean:define id="expensesLines" name="infoExpensesReport" property="lines" />
			<bean:define id="startSpan" value="0" />
			<bean:define id="length" name="infoExpensesReport" property="linesSize" />

			<logic:present name="numberOfSpanElements">
				<bean:define id="span" value="<%=request.getAttribute("span").toString()%>" />
				<bean:define id="startSpan" value="<%=request.getAttribute("startSpan").toString()%>" />
				<bean:define id="length" value="<%=request.getAttribute("length").toString()%>" />
				<bean:define id="numberOfSpanElements" value="<%=request.getAttribute("numberOfSpanElements").toString()%>" />
				<bean:define id="spanNumber" value="<%=request.getAttribute("spanNumber").toString()%>" />
				<table class="viewHeader">
					<tr>
						<td><report:navigation-bar linesId="infoExpensesReport" spanId="span" numberOfSpanElements="numberOfSpanElements" /></td>
					</tr>
				</table>
				<br />
			</logic:present>

			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.idMov" /></td>
					<td class="report-hdr"><bean:message key="label.member" /></td>
					<td class="report-hdr"><bean:message key="label.supplier" /></td>
					<td class="report-hdr"><bean:message key="label.docType" /></td>
					<td class="report-hdr"><bean:message key="label.docNumber" /></td>
					<td class="report-hdr"><bean:message key="label.financingSource" /></td>
					<td class="report-hdr"><bean:message key="label.rubric" /></td>
					<td class="report-hdr"><bean:message key="label.type" /></td>
					<td class="report-hdr"><bean:message key="label.date" /></td>
					<td class="report-hdr"><bean:message key="label.description" /></td>
					<td class="report-hdr"><bean:message key="label.ivaPercentage" /></td>
					<td class="report-hdr"><bean:message key="label.value" /></td>
					<td class="report-hdr"><bean:message key="label.tax" /></td>
					<td class="report-hdr"><bean:message key="label.total" /></td>
					<td class="report-hdr"><bean:message key="label.imputedPercentage" /></td>
				</tr>
				<logic:iterate id="line" name="expensesLines" indexId="lineIndex">
					<tr class="printHeader">
						<logic:greaterEqual name="lineIndex" value="<%=startSpan%>">
							<logic:lessThan name="lineIndex"
								value="<%=new Integer(new Integer(startSpan.toString()).intValue() + new Integer(length.toString()).intValue()).toString()%>">
					</tr>
					<tr>
						</logic:lessThan>
						</logic:greaterEqual>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="movementId" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="member" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="supplierDescription" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="documentType" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="documentNumber" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="financingSource" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="rubric" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="movementType" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="date" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="description" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><bean:write name="line" property="ivaPercentage" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="value" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="tax" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="total" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><bean:write name="line" property="imputedPercentage" /></td>
					</tr>
				</logic:iterate>
				<tr class="printHeader">
					<logic:present name="lastSpan">
						<bean:define id="lastSpan" value="<%=request.getAttribute("lastSpan").toString()%>" />
						<logic:equal name="lastSpan" value="true">
				</tr>
				<tr>
					</logic:equal>
</logic:present>
<td class="report-line-total-first" colspan="11"><bean:message key="label.total" /></td>
<td class="report-line-total"><report:sumColumn id="expensesLines" column="11" /></td>
<td class="report-line-total"><report:sumColumn id="expensesLines" column="12" /></td>
<td class="report-line-total"><report:sumColumn id="expensesLines" column="13" /></td>
<td class="report-line-total-last"></td>
</tr>
</table>
<br />
<bean:message key="message.listReport" />
<br />
</logic:notEmpty>
<logic:empty name="infoExpensesReport" property="lines">
	<span class="error"><bean:message key="message.noMovements" /></span>
</logic:empty>
<logic:present name="infoExpensesReport" property="summaryPTEReport">
	<table class="printHeader">

		<logic:present name="lastSpan">
			<bean:define id="lastSpan" value="<%=request.getAttribute("lastSpan").toString()%>" />
			<logic:equal name="lastSpan" value="true">
	</table>
	<table>
		</logic:equal>
</logic:present>
<tr>
	<td><jsp:include page="showExtraReportLines.jsp" /></td>
</tr>
</table>
</logic:present>
<br />
<br />
<br />
</logic:notEmpty>
</logic:present>
