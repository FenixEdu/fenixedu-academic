<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/projectReports.tld" prefix="report"%>
<logic:present name="infoOpeningProjectFileReport">
	<br />
	<table>
		<tr>
			<td rowspan="3"><img height="110" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
				src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
		</tr>
		<tr>
			<td colspan="3">
			<h2><bean:message key="title.openingProjectFileReport" /></h2>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.date" />:</strong></td>
			<td><report:computeDate /></td>
		</tr>
	</table>
	<br />
	<br />
	<br />
	<logic:empty name="infoOpeningProjectFileReport" property="projectCode">
		<span class="error"><bean:message key="message.notAvailableOpeningProjectFile" /></span>
	</logic:empty>
	<logic:notEmpty name="infoOpeningProjectFileReport" property="projectCode">
		<table class="box-table">
			<tr>
				<td><strong><bean:message key="label.projectNumber" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="projectCode" /></td>
				<td><strong><bean:message key="label.acronym" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="acronym" /></td>
				<td><strong><bean:message key="label.explorUnit" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="explorationUnit" /></td>
			</tr>
		</table>
		<br />
		<br />
		<table class="box-table">
			<tr>
				<td><strong><bean:message key="label.mecanographicNumber" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="coordinatorCode" /></td>
				<td><strong><bean:message key="label.name" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="coordinatorName" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.ext" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="coordinatorContact" /></td>
				<td><strong><bean:message key="label.academicUnit" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="academicUnit" /> - <bean:write
					name="infoOpeningProjectFileReport" property="academicUnitDescription" /></td>
			</tr>
		</table>
		<br />
		<br />
		<table class="box-table">
			<tr>
				<td><strong><bean:message key="label.origin" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="projectOrigin" /></td>
				<td><strong><bean:message key="label.type" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="projectType" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.cost" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="cost" /></td>
				<td><strong><bean:message key="label.coordination" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="coordination" /></td>
			</tr>
			<tr>
				<td colspan="2"><strong><bean:message key="label.operationalUnit" />:</strong> <bean:write name="infoOpeningProjectFileReport"
					property="operationalUnit" /> - <bean:write name="infoOpeningProjectFileReport" property="operationalUnitDescription" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.currency" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="currency" /></td>
				<td><strong><bean:message key="label.bid" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="bid" /></td>
			</tr>
			<tr>
				<td colspan="2"><strong><bean:message key="label.contractNumber" />:</strong> <bean:write name="infoOpeningProjectFileReport"
					property="contractNumber" /></td>
			</tr>
			<tr>
				<td colspan="2"><strong><bean:message key="label.parentProjectNumber" />:</strong> <bean:write name="infoOpeningProjectFileReport"
					property="parentProjectNumber" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.gd" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="generalDirection" /></td>
				<td><strong><bean:message key="label.program" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="program" />-<bean:write
					name="infoOpeningProjectFileReport" property="programDescription" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.initialDate" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="initialDate" /></td>
				<td><strong><bean:message key="label.duration" />:</strong> <bean:write name="infoOpeningProjectFileReport" property="duration" />&nbsp;<bean:message
					key="label.months" /></td>
			</tr>
			<tr>
				<td colspan="2"><strong><bean:message key="label.title" />:</strong></td>
			</tr>
			<tr>
				<td colspan="2"><bean:write name="infoOpeningProjectFileReport" property="title" /></td>
			</tr>
			<tr>
				<td colspan="2"><strong><bean:message key="link.summary" />:</strong></td>
			</tr>
			<tr>
				<td colspan="2"><bean:write name="infoOpeningProjectFileReport" property="summary" /></td>
			</tr>
		</table>
		<br />
		<br class="break" />
		<strong><bean:message key="label.financing" /></strong>
		<br />
		<br />
		<table class="box-table">
			<tr>
				<td><bean:message key="label.budgetaryMemberControl" />: <bean:write name="infoOpeningProjectFileReport" property="budgetaryMemberControl" /></td>
				<td><bean:message key="label.taxControl" />: <bean:write name="infoOpeningProjectFileReport" property="taxControl" /></td>
				<td><bean:message key="label.ilegivelTaxControl" />: <bean:write name="infoOpeningProjectFileReport" property="ilegivelTaxControl" /></td>
			</tr>
		</table>
		<br />
		<br />
		<strong><bean:message key="label.overheadsDistribution" /></strong>
		<br />
		<br />
		<table class="report-table">
			<tr>
				<td class="report-hdr"><bean:message key="label.beginDate" /></td>
				<td class="report-hdr"><bean:message key="label.managementUnitOverhead" /></td>
				<td class="report-hdr"><bean:message key="label.explorationUnit" /></td>
				<td class="report-hdr"><bean:message key="label.academicUnit" /></td>
				<td class="report-hdr"><bean:message key="label.operationalUnit" /></td>
				<td class="report-hdr"><bean:message key="label.coordinator" /></td>
			</tr>
			<tr>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="overheadsDate" /></td>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="managementUnitOverhead" /><bean:message
					key="label.percentageSymbol" /></td>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="explorationUnitOverhead" /><bean:message
					key="label.percentageSymbol" /></td>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="academicUnitOverhead" /><bean:message
					key="label.percentageSymbol" /></td>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="operationalUnitOverhead" /><bean:message
					key="label.percentageSymbol" /></td>
				<td class="report-td-0" align="center"><bean:write name="infoOpeningProjectFileReport" property="coordinatorOverhead" /><bean:message
					key="label.percentageSymbol" /></td>
			</tr>
		</table>
		<br />
		<br />
		<%double total = 0;

        %>
		<strong><bean:message key="label.projectFinancialEntities" /></strong>
		<br />
		<br />
		<logic:notEmpty name="infoOpeningProjectFileReport" property="projectFinancialEntities">
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.entity" /></td>
					<td class="report-hdr"><bean:message key="label.name" /></td>
					<td class="report-hdr"><bean:message key="label.value" /></td>
				</tr>

				<logic:iterate id="financialEntity" name="infoOpeningProjectFileReport" property="projectFinancialEntities" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="financialEntity" property="code" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="financialEntity" property="description" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="financialEntity" property="value" /></td>
						<bean:define id="thisValue" name="financialEntity" property="value" />
						<%total = total + ((Double) pageContext.findAttribute("thisValue")).doubleValue();

        %>
					</tr>
				</logic:iterate>
				<tr>
					<td class="report-line-total-first" colspan="2"><bean:message key="label.total" /></td>
					<td class="report-line-total-last"><strong><%=net.sourceforge.fenixedu.util.projectsManagement.FormatDouble.convertDoubleToString(total)%></strong></td>
				</tr>
			</table>
			<br />
			<br />
		</logic:notEmpty>
		<logic:notEmpty name="infoOpeningProjectFileReport" property="projectRubricBudget">
			<strong><bean:message key="label.projectRubricBudget" /></strong>
			<br />
			<br />
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.entity" /></td>
					<td class="report-hdr"><bean:message key="label.name" /></td>
					<td class="report-hdr"><bean:message key="label.value" /></td>
				</tr>
				<%total = 0;

        %>
				<logic:iterate id="rubricBudget" name="infoOpeningProjectFileReport" property="projectRubricBudget" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="rubricBudget" property="code" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="rubricBudget" property="description" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="rubricBudget" property="value" /></td>
						<bean:define id="thisValue" name="rubricBudget" property="value" />
						<%total = total + ((Double) pageContext.findAttribute("thisValue")).doubleValue();

        %>
					</tr>
				</logic:iterate>
				<tr>
					<td class="report-line-total-first" colspan="2"><bean:message key="label.total" /></td>
					<td class="report-line-total-last"><strong><%=net.sourceforge.fenixedu.util.projectsManagement.FormatDouble.convertDoubleToString(total)%></strong></td>
				</tr>
			</table>
			<bean:message key="message.rubricBudgetReport" />
		</logic:notEmpty>
		<br />
		<br class="break" />
		<logic:notEmpty name="infoOpeningProjectFileReport" property="projectMembersBudget">
			<strong><bean:message key="label.projectMembersBudget" /></strong>
			<br />
			<br />
			<logic:iterate id="memberBudget" name="infoOpeningProjectFileReport" property="projectMembersBudget" indexId="lineIndex">
				<p class="noBreak">
				<table class="box-table">
					<tr>
						<td colspan="4"><strong><bean:message key="label.entity" />:</strong> <bean:write name="memberBudget" property="institutionCode" /> - <bean:write
							name="memberBudget" property="institutionName" /></td>
					</tr>
					<tr>
						<td><strong><bean:message key="label.type" />:</strong> <bean:write name="memberBudget" property="type" /></td>
						<td><strong><bean:message key="label.ovh" />:</strong> <bean:write name="memberBudget" property="overheads" /></td>
						<td><strong><bean:message key="label.trf" />:</strong> <bean:write name="memberBudget" property="transferences" /></td>
						<td><strong><bean:message key="label.financingPercentage" />:</strong> <bean:write name="memberBudget" property="financingPercentage" /><bean:message
							key="label.percentageSymbol" /></td>
					</tr>
				</table>
				<logic:notEmpty name="memberBudget" property="rubricBudget">
					<br />
					<table class="report-table">
						<tr>
							<td class="report-hdr"><bean:message key="label.code" /></td>
							<td class="report-hdr"><bean:message key="label.name" /></td>
							<td class="report-hdr"><bean:message key="label.value" /></td>
						</tr>
						<%total = 0;

        %>
						<logic:iterate id="rubricBudget" name="memberBudget" property="rubricBudget" indexId="lineIndex">
							<tr>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="rubricBudget" property="code" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="rubricBudget" property="description" /></td>
								<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="rubricBudget" property="value" /></td>
								<bean:define id="thisValue" name="rubricBudget" property="value" />
								<%total = total + ((Double) pageContext.findAttribute("thisValue")).doubleValue();

        %>
							</tr>
						</logic:iterate>
						<tr>
							<td class="report-line-total-first" colspan="2"><bean:message key="label.total" /></td>
							<td class="report-line-total-last"><strong><%=net.sourceforge.fenixedu.util.projectsManagement.FormatDouble.convertDoubleToString(total)%></strong></td>
						</tr>
					</table>
				</logic:notEmpty></p>
				<br />
				<br />
			</logic:iterate>
		</logic:notEmpty>
		<br class="break" />
		<logic:notEmpty name="infoOpeningProjectFileReport" property="projectInvestigationTeam">
			<strong><bean:message key="label.projectInvestigationTeam" /></strong>
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.code" /></td>
					<td class="report-hdr"><bean:message key="label.name" /></td>
				</tr>
				<logic:iterate id="teamPerson" name="infoOpeningProjectFileReport" property="projectInvestigationTeam" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="teamPerson" property="code" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="teamPerson" property="description" /></td>
					</tr>
				</logic:iterate>
			</table>
			<br />
			<br />
		</logic:notEmpty>
	</logic:notEmpty>
</logic:present>
