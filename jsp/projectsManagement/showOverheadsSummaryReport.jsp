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
			<h2><bean:message key="title.overheadsSummaryReport" /></h2>
			</td>
			<logic:notEmpty name="infoReport" property="lines">
				<td class="infoop" width="20"><html:link page="<%="/overheadReport.do?method=exportToExcel&amp;reportType=overheadsSummaryReport"+code%>">
					<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.exportToExcel" align="right" />
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
			<td rowspan="5"><img height="110" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
				src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
		</tr>
		<tr class="printHeader">
			<td colspan="2">
			<h2><bean:message key="title.overheadsSummaryReport" /></h2>
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
				<td class="report-hdr"><bean:message key="label.year" /></td>
				<td class="report-hdr"><bean:message key="label.explorUnit" /></td>
				<td class="report-hdr"><bean:message key="label.costCenter" /></td>
				<td class="report-hdr"><bean:message key="label.OGRevenue" /></td>
				<td class="report-hdr"><bean:message key="label.OGOverhead" /></td>
				<td class="report-hdr"><bean:message key="label.OARevenue" /></td>
				<td class="report-hdr"><bean:message key="label.OAOverhead" /></td>
				<td class="report-hdr"><bean:message key="label.OORevenue" /></td>
				<td class="report-hdr"><bean:message key="label.OOOverhead" /></td>
				<td class="report-hdr"><bean:message key="label.OERevenue" /></td>
				<td class="report-hdr"><bean:message key="label.OEOverhead" /></td>
				<td class="report-hdr"><bean:message key="label.totalOverheads" /></td>
				<td class="report-hdr"><bean:message key="label.transferedOverheads" /></td>
				<td class="report-hdr"><bean:message key="label.balance" /></td>
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
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="year" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="explorationUnit" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="costCenter" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OGRevenue" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OGOverhead" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OARevenue" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OAOverhead" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OORevenue" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OOOverhead" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OERevenue" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="OEOverhead" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="totalOverheads" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="transferedOverheads" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="balance" /></td>
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
<td class="report-line-total-first" colspan="3"><bean:message key="label.total" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="3" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="4" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="5" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="6" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="7" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="8" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="9" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="10" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="11" /></td>
<td class="report-line-total"><report:sumColumn id="lines" column="12" /></td>
<td class="report-line-total-last"><report:sumColumn id="lines" column="13" /></td>
</tr>
</table>
<br />
<bean:message key="message.overheadsSummaryReport" />
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
