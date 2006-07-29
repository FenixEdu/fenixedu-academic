<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="java.lang.Boolean"%>
<h2><bean:message key="link.masterDegree.gratuity.insert"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<p />
<logic:present name="infoGratuityValues">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>
				<bean:define id="degreeLabel"><%=pageContext.findAttribute("degreeName")%></bean:define>
				<b><bean:message key="label.masterDegree.gratuity.executionYear" />:</b>&nbsp;<bean:write name="executionYearLabel" /><br />
				<b><bean:message key="label.qualification.degree"/>:</b>&nbsp;<bean:write name="degreeLabel"/>
			</td>
		</tr>
	</table>
	<br />
	<center><b><bean:message key="message.masterDegree.gratuity.definition.success"/></b></center>
	<table>
		<%-- Gratuity Values --%>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="label.masterDegree.gratuity.values"/></b>
			</td>
		</tr>
		<logic:greaterThan name="infoGratuityValues" property="anualValue" value="0">
			<tr>
				<td colspan="2">
					<br />
					<bean:message key="label.masterDegree.gratuity.annual.value"/>:&nbsp;
					<bean:write name="infoGratuityValues" property="anualValue" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
				</td>
			</tr>
		</logic:greaterThan>
		<logic:greaterThan name="infoGratuityValues" property="scholarShipValue" value="0">		
			<tr>
				<td colspan="2">
					<br />
					<bean:message key="label.masterDegree.gratuity.scholarPart"/>:&nbsp;
					<bean:write name="infoGratuityValues" property="scholarShipValue" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<bean:message key="label.masterDegree.gratuity.thesisPart"/>:&nbsp;
					<bean:write name="infoGratuityValues" property="finalProofValue" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>&nbsp;
					<logic:equal name="infoGratuityValues" property="proofRequestPayment" value="<%=Boolean.TRUE.toString()%>">
						<bean:message key="label.masterDegree.gratuity.paymentSituation"/>
					</logic:equal>
					<br /><br />
				</td>
			</tr>
		</logic:greaterThan>
		<tr>
			<td colspan="2">
				<bean:message key="label.masterDegree.gratuity.unitaryValue"/>:&nbsp;
				<logic:greaterThan name="infoGratuityValues" property="courseValue" value="0">		
					<bean:message key="label.masterDegree.gratuity.valueByCourse"/>
					<bean:write name="infoGratuityValues" property="courseValue" />&nbsp;
					<bean:message key="label.masterDegree.gratuity.euro"/>
				</logic:greaterThan>
				<logic:greaterThan name="infoGratuityValues" property="creditValue" value="0">		
					<bean:message key="label.masterDegree.gratuity.valueByCredit"/>
					<bean:write name="infoGratuityValues" property="creditValue" />&nbsp;
					<bean:message key="label.masterDegree.gratuity.euro"/>
				</logic:greaterThan>
				<br /><br />
			</td>
		</tr>
		<%-- Payment Conditions --%>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="label.masterDegree.gratuity.paymentConditions"/></b>
			</td>
		</tr>
		<logic:present name="infoGratuityValues" property="endPayment">
			<tr>
				<td colspan="2">
					<bean:message key="label.masterDegree.gratuity.totalPayment"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&nbsp;&nbsp;&nbsp;&nbsp;
					<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
					<logic:present name="infoGratuityValues" property="startPayment">
						<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
						<dt:format pattern="dd-MM-yyyy">
							<bean:write name="infoGratuityValues" property="startPayment.time"/>
						</dt:format>
					</logic:present>
					<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
					<dt:format pattern="dd-MM-yyyy">
						<bean:write name="infoGratuityValues" property="endPayment.time"/>
					</dt:format>
					<br /><br />
				</td>
			</tr>
		</logic:present>		
		<bean:size id="phasesSize" name="infoGratuityValues" property="infoPaymentPhases"/>
		<logic:greaterThan name="phasesSize" value="0">
			<tr>
				<td colspan="2">
					<bean:message key="label.masterDegree.gratuity.partialPayment"/>
					<br />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<table>
						<tr>
							<th class="listClasses-header">
								<bean:message key="label.masterDegree.gratuity.initialDate"/></th>
							<th class="listClasses-header">
								<bean:message key="label.masterDegree.gratuity.finalDate"/></th>
							<th class="listClasses-header">
								<bean:message key="label.masterDegree.gratuity.value"/>&nbsp;
								<bean:message key="label.masterDegree.gratuity.euro"/></th>
							<th class="listClasses-header">
								<bean:message key="label.masterDegree.gratuity.description"/></th>
						</tr>
						<logic:iterate id="phase" name="infoGratuityValues" property="infoPaymentPhases" type="net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase" indexId="index">
							<tr>
								<logic:notEmpty name="phase" property="startDate">
									<td class="listClasses">
										<dt:format pattern="dd-MM-yyyy">
											<bean:write name="phase" property="startDate.time"/>
										</dt:format>		
									</td>
								</logic:notEmpty>
								<logic:empty name="phase" property="startDate">
									<td class="listClasses">-</td>
								</logic:empty>
								<td class="listClasses">
									<dt:format pattern="dd-MM-yyyy">
										<bean:write name="phase" property="endDate.time"/>
									</dt:format>		
								</td>
								<td class="listClasses">
									<bean:write name="phase" property="value"/>
								</td>
								<td class="listClasses">
									<logic:present name="phase" property="description">
										<bean:message key="label.masterDegree.gratuity.phase" arg0="<%=phase.getDescription()%>"/>
									</logic:present>
									<logic:notPresent name="phase" property="description">
										&nbsp;
									</logic:notPresent>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</logic:greaterThan>
	</table>
</logic:present>