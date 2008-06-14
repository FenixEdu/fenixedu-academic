<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/projectReports.tld" prefix="report"%>

<logic:present name="infoExpensesReport">
	<br />
	<br />
	<br />
	<table width="70%" align="center">
		<tr>
			<td class="infoop"><strong><bean:message key="label.treasury" /></strong></td>
		</tr>
		<tr>
			<td>
			<table>
				<tr>
					<td width="50%">
					<table valign="top" align="left" width="90%" cellspacing="0" cellpadding="1">
						<bean:define id="lineSummaryPTE" name="infoExpensesReport" property="summaryPTEReport" />
						<tr>
							<td><strong><bean:message key="link.revenue" />&nbsp;<bean:message key="label.pte" />:</strong></td>
							<td align="right"><report:formatDoubleValue name="lineSummaryPTE" property="revenue" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="link.expenses" />&nbsp;<bean:message key="label.pte" />:</strong></td>
							<td align="right"><report:formatDoubleValue name="lineSummaryPTE" property="expense" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="label.tax" />&nbsp;<bean:message key="label.pte" />:</strong></td>
							<td class="bottomborder" align="right"><report:formatDoubleValue name="lineSummaryPTE" property="tax" /></td>
						</tr>
						<tr>
							<td colspan="2" align="right"><report:formatDoubleValue name="lineSummaryPTE" property="total" /></td>
						</tr>
						<bean:define id="totalPTE" name="lineSummaryPTE" property="total" />
					</table>

					</td>


					<td width="50%">
					<table valign="top" align="right" width="100%" cellspacing="0" cellpadding="1">
						<bean:define id="lineSummaryEUR" name="infoExpensesReport" property="summaryEURReport" />
						<tr>
							<td><strong><bean:message key="link.revenue" />&nbsp;<bean:message key="label.eur" />:</strong></td>
							<td align="right"><report:formatDoubleValue name="lineSummaryEUR" property="revenue" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="link.expenses" />&nbsp;<bean:message key="label.eur" />:</strong></td>
							<td align="right"><report:formatDoubleValue name="lineSummaryEUR" property="expense" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="label.tax" />&nbsp;<bean:message key="label.eur" />:</strong></td>
							<td align="right"><report:formatDoubleValue name="lineSummaryEUR" property="tax" /></td>
						</tr>
						<tr>
							<td><strong><bean:message key="label.adiantPorJust" />:</strong></td>
							<td class="bottomborder" align="right"><report:formatDoubleValue name="lineSummaryEUR" property="adiantamentosPorJustificar" /></td>
						</tr>
						<tr>
							<td colspan="2" align="right"><report:formatDoubleValue name="lineSummaryEUR" property="total" /></td>
						</tr>
						<bean:define id="totalEuro" name="lineSummaryEUR" property="total" />
					</table>
					</td>
				</tr>
			</table>

			<br />
			<%
        double total = ((Double) pageContext.findAttribute("totalPTE")).doubleValue() / 200.482
                + ((Double) pageContext.findAttribute("totalEuro")).doubleValue();

        %> <strong><bean:message key="label.treasuryBalance" /></strong> (a)(b) <strong>:</strong> &nbsp; &nbsp; <%if (total < 0) {

        %> <font
				color="red"> <%}

        %> <%=net.sourceforge.fenixedu.util.projectsManagement.FormatDouble.convertDoubleToString(total)%> <%if (total < 0) {

        %> </font> <%}

    %></td>
		</tr>
	</table>
	<br />
	<table width="70%" align="center">
		<tr>
			<td class="infoop"><strong><bean:message key="label.adiantamentos" /></strong></td>
		</tr>
		<tr>
			<td><br />
			<table width="50%" cellspacing="0">
				<bean:define id="lineAdvancingReport" name="infoExpensesReport" property="adiantamentosReport" />
				<tr>
					<td><strong><bean:message key="label.total.adiantamentosReport" />:</strong></td>
					<td align="right"><report:formatDoubleValue name="lineAdvancingReport" property="adiantamentos" /></td>
				</tr>
				<tr>
					<td class="bottomborder"><strong><bean:message key="label.returnsExecuted.adiantamentosReport" /></strong>:</td>
					<td class="bottomborder" align="right"><report:formatDoubleValue name="lineAdvancingReport" property="justifications" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.total" />:</strong></td>
					<td align="right"><report:formatDoubleValue name="lineAdvancingReport" property="total" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.extraInformation.a" /></td>
		</tr>
	</table>

	<br />
	<table width="70%" align="center">
		<tr>
			<td class="infoop"><strong><bean:message key="label.cabimentos" /></strong></td>
		</tr>
		<tr>
			<td><br />
			<table width="50%" cellspacing="0">
				<bean:define id="cabimentosReport" name="infoExpensesReport" property="cabimentosReport" />
				<tr>
					<td><strong><bean:message key="label.total.cabimentosReport" />:</strong></td>
					<td align="right"><report:formatDoubleValue name="cabimentosReport" property="cabimentos" /></td>
				</tr>
				<tr>
					<td class="bottomborder"><b><bean:message key="label.returnsExecuted.cabimentosReport" />:</b></td>
					<td class="bottomborder" align="right"><report:formatDoubleValue name="cabimentosReport" property="justifications" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.toExecute.cabimentosReport" />:</strong></td>
					<td align="right"><report:formatDoubleValue name="cabimentosReport" property="total" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.extraInformation.b" /></td>
		</tr>
	</table>
</logic:present>
