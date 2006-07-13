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

	<table class="viewHeader" width="100%" cellspacing="0">
		<tr>
			<td class="infoop">
			<h2><bean:message key="title.generatedOverheadsReport" /></h2>
			</td>
			<logic:notEmpty name="infoReport" property="lines">
				<td class="infoop" width="20"><html:link page="<%="/overheadReport.do?method=exportToExcel&amp;reportType=generatedOverheadsReport"+code%>">
					<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.exportToExcel" align="right" />
				</html:link></td>
			</logic:notEmpty>
		</tr>
	</table>
	<br />
	<table>
		<tr class="printHeader">
			<td rowspan="5"><img height="110" alt="<bean:message key="dotist-id" bundle="IMAGE_RESOURCES" />"
				src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
		</tr>
		<tr class="printHeader">
			<td colspan="2">
			<h2><bean:message key="title.generatedOverheadsReport" /></h2>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.unit" />:</strong></td>
			<td><bean:write name="infoCostCenter" property="description" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.costCenter" />:</strong></td>
			<td><bean:write name="infoCostCenter" property="code" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.date" />:</strong></td>
			<td><report:computeDate /></td>
		</tr>
	</table>
	<br />
	<br />
	<logic:notEmpty name="infoReport" property="lines">
		<bean:define id="startSpan" value="0" />
		<bean:define id="length" name="infoReport" property="linesSize" />
		<bean:define id="lines" name="infoReport" property="lines" />
		<logic:present name="numberOfSpanElements">
			<bean:define id="span" value="<%=request.getAttribute("span").toString()%>" />
			<bean:define id="startSpan" value="<%=request.getAttribute("startSpan").toString()%>" />
			<bean:define id="length" value="<%=request.getAttribute("length").toString()%>" />
			<bean:define id="numberOfSpanElements" value="<%=request.getAttribute("numberOfSpanElements").toString()%>" />
			<bean:define id="spanNumber" value="<%=request.getAttribute("spanNumber").toString()%>" />
			<table class="viewHeader">
				<tr>
					<td><report:navigation-bar linesId="infoReport" spanId="span" numberOfSpanElements="numberOfSpanElements" /></td>
				</tr>
			</table>
			<br />
		</logic:present>

		<table class="report-table">
			<tr>
				<td class="report-hdr"><bean:message key="label.explorUnit" /></td>
				<td class="report-hdr"><bean:message key="label.projNum" /></td>
				<td class="report-hdr"><bean:message key="label.acronym" /></td>
				<td class="report-hdr"><bean:message key="label.coordinator" /></td>
				<td class="report-hdr"><bean:message key="label.name" /></td>
				<td class="report-hdr"><bean:message key="label.type" /></td>
				<td class="report-hdr"><bean:message key="label.date" /></td>
				<td class="report-hdr"><bean:message key="label.description" /></td>
				<td class="report-hdr"><bean:message key="link.revenue" /></td>
				<td class="report-hdr"><bean:message key="label.percentageSymbol" />&nbsp;<bean:message key="label.ovh" /></td>
				<td class="report-hdr"><bean:message key="label.value" />&nbsp;<bean:message key="label.ovh" /></td>
			</tr>
			<logic:iterate id="line" name="infoReport" property="lines" indexId="lineIndex">
				<tr class="printHeader">
					<logic:greaterEqual name="lineIndex" value="<%=startSpan%>">
						<logic:lessThan name="lineIndex"
							value="<%=new Integer(new Integer(startSpan.toString()).intValue() + new Integer(length.toString()).intValue()).toString()%>">
				</tr>
				<tr>
					</logic:lessThan>
					</logic:greaterEqual>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="explorationUnit" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="projectNumber" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="acronim" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="coordinatorNumber" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="coordinatorName" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="type" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="date" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="description" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="revenue" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="overheadPerscentage" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="overheadValue" /></td>
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
<td class="report-line-total-first" colspan="8"><bean:message key="label.total" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="8" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="9" /></td>
<td class="report-line-total-last"><report:sumColumn id="lines" column="10" /></td>
</tr>
</table>
<br />
<br />
</logic:notEmpty>
<logic:empty name="infoReport" property="lines">
	<span class="error"><bean:message key="message.noMovements" /></span>
</logic:empty>
<br />
<br />
<br />
</logic:present>
