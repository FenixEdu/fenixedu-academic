<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-report.tld" prefix="report"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen, print" type="text/css" />
<link href="<%= request.getContextPath() + "/CSS/report.css" %>" rel="stylesheet" type="text/css" />
</head>
<body style="background: #fff;">
<table id="bodycontent" width="100%">
	<tr>
		<td><logic:present name="infoSummaryReport">
			<table>
				<tr>
					<td rowspan="4"><html:img height="110" src="<%= request.getContextPath() + "/images/LogoIST.gif"%>" /></td>
				</tr>
				<tr>
					<td colspan="2">
					<h2><bean:message key="title.summaryReport" /></h2>
					</td>
				</tr>
				<logic:present name="userView" name="<%= SessionConstants.U_VIEW %>" scope="session">
					<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session" />
					<tr>
						<td><strong><bean:message key="label.coordinator" />:</strong></td>
						<td><bean:write name="userView" property="fullName" /></td>
					</tr>
					<tr>
						<td><strong><bean:message key="label.date" />:</strong></td>
						<td><report:computeDate /></td>
					</tr>
				</logic:present>
			</table>
			<br />
			<bean:message key="message.summaryReport" />
			<br />
			<br />
			<bean:define id="summaryLines" name="infoSummaryReport" property="lines" />
			<logic:notEmpty name="summaryLines">
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
							<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line"
								property="cabimentoPorExecutar" /></td>
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
			</logic:notEmpty>
		</logic:present></td>
	</tr>
</table>
</body>
</html:html>