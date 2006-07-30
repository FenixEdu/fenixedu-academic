<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/projectReports.tld" prefix="report"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<logic:present name="infoSummaryReport">
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
	<bean:define id="infoCoordinator" name="infoSummaryReport" property="infoCoordinator" />
	<bean:define id="coodinatorCode" name="infoCoordinator" property="code" />
	<table class="viewHeader" width="100%" cellspacing="0">
		<tr>
			<td class="infoop">
			<h2><bean:message key="title.summaryReport" /></h2>
			</td>
			<logic:notEmpty name="infoSummaryReport" property="lines">
				<td class="infoop" width="20"><html:link
					page="<%="/projectReport.do?method=exportToExcel&amp;reportType=summaryReport&amp;coordinatorCode="+coodinatorCode+code%>">
					<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="excel" bundle="IMAGE_RESOURCES" align="right" />
				</html:link></td>
			</logic:notEmpty>
		</tr>
		<tr>
			<td><span class="error"><!-- Error messages go here --><bean:message key="message.printLayoutOrientation" /></span></td>
		</tr>
	</table>
	<br />
	<table>
		<tr class="printHeader">
			<td rowspan="4"><img height="110" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
				src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
		</tr>
		<tr class="printHeader">
			<td colspan="2">
			<h2><bean:message key="title.summaryReport" /></h2>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.coordinator" />:</strong></td>
			<td><bean:write name="infoCoordinator" property="description" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.date" />:</strong></td>
			<td><report:computeDate /></td>
		</tr>
	</table>
	<br />
	<bean:message key="message.summaryReport" />
	<br />
	<br />
	<bean:define id="summaryLines" name="infoSummaryReport" property="lines" />
	<table class="report-table">
		<tr>
			<td class="report-hdr"><bean:message key="label.projNum" /></td>
			<td class="report-hdr"><bean:message key="label.acronym" /></td>
			<td class="report-hdr"><bean:message key="label.explorUnit" /></td>
			<td class="report-hdr"><bean:message key="label.type" /></td>
			<td class="report-hdr"><bean:message key="label.budget" /></td>
			<td class="report-hdr"><bean:message key="label.maxFinance" /></td>
			<td class="report-hdr"><bean:message key="link.revenue" /></td>
			<td class="report-hdr"><bean:message key="link.expenses" /></td>
			<td class="report-hdr"><bean:message key="label.adiantPorJust" /></td>
			<td class="report-hdr"><bean:message key="label.treasuryBalance" /></td>
			<td class="report-hdr"><bean:message key="label.toExecute.cabimentosReport" /></td>
			<td class="report-hdr"><bean:message key="label.budgetBalance" /></td>
		</tr>
		<logic:iterate id="line" name="summaryLines" indexId="lineIndex">
			<tr>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="projectCode" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="acronym" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="explorationUnit" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="type" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="budget" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="maxFinance" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="revenue" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="expense" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line"
					property="adiantamentosPorJustificar" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="treasuryBalance" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="cabimentoPorExecutar" /></td>
				<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="budgetBalance" /></td>
			</tr>
		</logic:iterate>
		<tr>
			<td class="report-line-total-first" colspan="4"><bean:message key="label.total" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="4" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="5" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="6" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="7" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="8" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="9" /></td>
			<td class="report-line-total"><report:sumColumn id="summaryLines" column="10" /></td>
			<td class="report-line-total-last"><report:sumColumn id="summaryLines" column="11" /></td>
		</tr>
	</table>
	<logic:empty name="infoSummaryReport" property="lines">
		<span class="error"><!-- Error messages go here --><bean:message key="message.noUserProjects" /></span>
	</logic:empty>
	<br />
	<br />
	<br />
	<br />
	<br />
</logic:present>
<logic:notPresent name="infoSummaryReport">
	<span class="error"><!-- Error messages go here --><bean:message key="message.noUserProjects" /></span>
</logic:notPresent>
