<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-report.tld" prefix="report"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen, print" type="text/css" />
<link href="<%= request.getContextPath() + "/CSS/report.css" %>" rel="stylesheet" type="text/css" />
</head>
<body style="background: #fff;">
<table id="bodycontent" width="100%">
	<tr>
		<td><logic:present name="infoReport">
			<logic:notEmpty name="infoReport" property="infoProject">
				<table>
					<tr>
						<td rowspan="7">
						    <img height="110" alt="Logo <bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/>" src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
						</td>
					</tr>
					<tr>
						<td colspan="3"><bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />
						<h2><bean:message key="<%="title."+reportType%>" /></h2>
						</td>
					</tr>
					<bean:define id="infoProject" name="infoReport" property="infoProject" />

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
				<%double parentTotal = 0;
        double totalJustified = 0;%>
				<logic:notEmpty name="infoReport" property="lines">
					<bean:define id="parentLines" name="infoReport" property="lines" />
					<logic:iterate id="parentLine" name="parentLines">
						<bean:define id="parentValue" name="parentLine" property="parentValue" />
						<%parentTotal = parentTotal + ((Double) pageContext.findAttribute("parentValue")).doubleValue();
        double thisTotal = 0;%>
						<strong><bean:message key="<%="label."+reportType%>" /></strong>
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
							<strong><bean:message key="<%="label.executionsOf."+reportType%>" /></strong>
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
								<bean:define id="lines" name="parentLine" property="movements" />
								<logic:iterate id="line" name="lines" indexId="lineIndex">
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
								<tr>
									<td class="report-line-total-first" colspan="5"><bean:message key="label.total" /></td>
									<td class="report-line-total"><report:sumColumn id="lines" column="5" /></td>
									<td class="report-line-total"><report:sumColumn id="lines" column="6" /></td>
									<td class="report-line-total-last"><report:sumColumn id="lines" column="7" /></td>
								</tr>
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
				</logic:notEmpty>
			</logic:notEmpty>
			<br />
			<br />
			<br />
		</logic:present></td>
	</tr>
</table>
</body>
</html>
