<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="Util.Data"%>
<h2><bean:message key="link.masterDegree.gratuity.insert"/></h2>
<span class="error"><html:errors/></span>
<p />
<html:form action="/insertGratuityDataLA.do"> 
	<html:hidden property="page" value="2"/>
	<html:hidden property="executionYear"/>
	<html:hidden property="degree"/>
	<html:hidden property="degreeName"/>
	<html:hidden property="specializationArea"/>	
	<html:hidden property="gratuityId"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>
				<bean:define id="degreeLabel"><%=pageContext.findAttribute("degreeName")%></bean:define>
				<b><bean:message key="label.masterDegree.gratuity.executionYear" />:</b>&nbsp;<bean:write name="executionYearLabel" /><br />
				<b><bean:message key="label.qualification.degree"/>:</b>&nbsp;<bean:write name="degreeLabel"/>
<%--				<bean:define id="labelSpecializationArea"><%=pageContext.findAttribute("specializationArea")%></bean:define>
				<b><bean:message key="label.masterDegree.gratuity.specializationArea"/></b>&nbsp;<bean:write name="labelSpecializationArea"/>--%>
			</td>
		</tr>
	</table>
	<br />
	<table>
		<%-- Gratuity Values --%>
		<tr>
			<td colspan="2">
				<b><bean:message key="label.masterDegree.gratuity.values"/></b>
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.annual.value"/>:
				<br /><br />
			</td>
			<td>
				<html:text property="annualValue" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
				<br /><br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.scholarPart"/>:
			</td>
			<td>
				<html:text property="scholarPart" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.thesisPart"/>:
				<br /><br />
			</td>
			<td>
				<html:text property="thesisPart" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>&nbsp;
				<html:checkbox property="paymentWhen" />
				<bean:message key="label.masterDegree.gratuity.paymentSituation"/>
				<br /><br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.unitaryValue"/>:
			</td>
			<td>
				<bean:message key="label.masterDegree.gratuity.valueByCourse"/>
				<html:text property="unitaryValueCourse" />&nbsp;
				<bean:message key="label.masterDegree.gratuity.euro"/>
				<br />
				&nbsp;<i><bean:message key="label.masterDegree.gratuity.or"/></i>&nbsp;
				<br />
				<bean:message key="label.masterDegree.gratuity.valueByCredit"/>
				<html:text property="unitaryValueCredit" />&nbsp;
				<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<%-- Payment Conditions --%>
		<tr>
			<td colspan="2">
				<br /><br /><br />
				<b><bean:message key="label.masterDegree.gratuity.paymentConditions"/></b>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:checkbox property="totalPayment" />
				<bean:message key="label.masterDegree.gratuity.totalPayment"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text property="initialDateTotalPayment" maxlength="10" size="10"/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text property="finalDateTotalPayment" maxlength="10" size="10"/>
				<bean:message key="message.dateFormat"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br /><br />
				<html:checkbox property="partialPayment" />
				<bean:message key="label.masterDegree.gratuity.partialPayment"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<html:checkbox property="registrationPayment"/>
				<bean:message key="label.masterDegree.gratuity.registrationPayment"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text property="initialDateRegistrationPayment" maxlength="10" size="10"/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text property="finalDateRegistrationPayment" maxlength="10" size="10"/>
				<bean:message key="message.dateFormat"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.value"/>:<html:text property="registrationValue" />
				&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<%-- Show defined Payment Phases--%>
		<logic:present name="infoPaymentPhases">
			<bean:size id="numberPayments" name="infoPaymentPhases"/>
			<logic:greaterThan name="numberPayments" value="0">
				<tr>
					<td colspan="2">
						<br /><br />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<i><bean:message key="label.masterDegree.gratuity.definedPhases"/></i>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table>
							<tr>
								<td class="listClasses-header">
									<bean:message key="label.masterDegree.gratuity.initialDate"/></td>
								<td class="listClasses-header">
									<bean:message key="label.masterDegree.gratuity.finalDate"/></td>
								<td class="listClasses-header">
									<bean:message key="label.masterDegree.gratuity.value"/>&nbsp;
									<bean:message key="label.masterDegree.gratuity.euro"/></td>
								<td class="listClasses-header">
									<bean:message key="button.masterDegree.gratuity.remove"/></td>
							</tr>
							<logic:iterate id="phase" name="infoPaymentPhases" type="DataBeans.InfoPaymentPhase" indexId="index">
								<tr>
									<logic:notEmpty name="phase" property="startDate">
										<td class="listClasses">
											<html:hidden property="paymentPhases" value="<%=Data.format2DayMonthYear(phase.getStartDate(), "/")%>"/>
											<%=Data.format2DayMonthYear(phase.getStartDate(), "/")%>
										</td>
									</logic:notEmpty>
									<logic:empty name="phase" property="startDate">
										<html:hidden property="paymentPhases" value=""/>
										<td class="listClasses">-</td>
									</logic:empty>
									<td class="listClasses">
										<html:hidden property="paymentPhases" value="<%=Data.format2DayMonthYear(phase.getEndDate(), "/")%>"/>
										<%=Data.format2DayMonthYear(phase.getEndDate(), "/")%>
									</td>
									<td class="listClasses">
										<html:hidden property="paymentPhases" value="<%=phase.getValue().toString()%>"/>
										<bean:write name="phase" property="value"/>
									</td>
									<td class="listClasses">
										<html:multibox property="removedPhases">
											<bean:write name="index"/>
										</html:multibox>	
									</td>
								</tr>
							</logic:iterate>
							<tr>
								<td colspan="4" style="text-align:right">
									<html:submit property="method" styleClass="inputbutton">
										<bean:message key="button.masterDegree.gratuity.remove"/>
									</html:submit>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</logic:greaterThan>
		</logic:present>
		<%-- Fields for new Payment Phase --%>
		<tr>
			<td colspan="2">
				<br /><br />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<i><bean:message key="label.masterDegree.gratuity.phaseDefinition"/></i>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text property="initialDatePartialPayment" maxlength="10" size="10" value=""/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text property="finalDatePartialPayment" maxlength="10" size="10" value=""/>
				<bean:message key="message.dateFormat"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.phaseValue"/>:
				<html:text property="phaseValue" value=""/>
				&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;			
				<html:submit property="method" styleClass="inputbutton">
					<bean:message key="button.masterDegree.gratuity.addPhase"/>
				</html:submit>
			</td>
		</tr>
	</table>
	<br /><br />
	<html:submit property="method" styleClass="inputbutton">
		<bean:message key="button.masterDegree.gratuity.submit"/>
	</html:submit>
	<html:submit property="method" styleClass="inputbutton">
		<bean:message key="button.cancel"/>
	</html:submit>
</html:form>