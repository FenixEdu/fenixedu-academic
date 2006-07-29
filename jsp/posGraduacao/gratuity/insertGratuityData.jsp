<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.util.Data"%>
<h2><bean:message key="link.masterDegree.gratuity.insert"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<p />
<html:form action="/insertGratuityDataLA.do"> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeName" property="degreeName"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.specializationArea" property="specializationArea"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuityId" property="gratuityId"/>
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
	<table>
		<%-- Gratuity Values --%>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="message.masterDegree.gratuity.values.help"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="label.masterDegree.gratuity.values"/></b>
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<br />
				<bean:message key="label.masterDegree.gratuity.annual.value"/>:
				<br />
				&nbsp;
				<br />
			</td>
			<td>
				<br />
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.annualValue" property="annualValue" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
				<br />
				&nbsp;<i><bean:message key="label.masterDegree.gratuity.or"/></i>&nbsp;
				<br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.scholarPart"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.scholarPart" property="scholarPart" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="label.masterDegree.gratuity.thesisPart"/>:
				<br /><br />
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.thesisPart" property="thesisPart" />&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>&nbsp;
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.paymentWhen" property="paymentWhen" />
				<bean:message key="label.masterDegree.gratuity.paymentSituation"/>
				<br /><br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<br />
				<bean:message key="label.masterDegree.gratuity.unitaryValue"/>:
			</td>
			<td>
				<br />
				<bean:message key="label.masterDegree.gratuity.valueByCourse"/>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.unitaryValueCourse" property="unitaryValueCourse" />&nbsp;
				<bean:message key="label.masterDegree.gratuity.euro"/>
				<br />
				&nbsp;<i><bean:message key="label.masterDegree.gratuity.or"/></i>&nbsp;
				<br />
				<bean:message key="label.masterDegree.gratuity.valueByCredit"/>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.unitaryValueCredit" property="unitaryValueCredit" />&nbsp;
				<bean:message key="label.masterDegree.gratuity.euro"/>
				<br /><br />
			</td>
		</tr>
		<%-- Payment Conditions --%>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="message.masterDegree.gratuity.paymentConditions.help"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="label.masterDegree.gratuity.paymentConditions"/></b>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.totalPayment" property="totalPayment" />
				<bean:message key="label.masterDegree.gratuity.totalPayment"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.initialDateTotalPayment" property="initialDateTotalPayment" maxlength="10" size="10"/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.finalDateTotalPayment" property="finalDateTotalPayment" maxlength="10" size="10"/>
				<bean:message key="message.dateFormat"/>
				<br /><br />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.partialPayment" property="partialPayment" />
				<bean:message key="label.masterDegree.gratuity.partialPayment"/>
				<br />
			</td>
		</tr>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="message.masterDegree.gratuity.registrationPayment.help"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.registrationPayment" property="registrationPayment"/>
				<bean:message key="label.masterDegree.gratuity.registrationPayment"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.initialDateRegistrationPayment" property="initialDateRegistrationPayment" maxlength="10" size="10"/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.finalDateRegistrationPayment" property="finalDateRegistrationPayment" maxlength="10" size="10"/>
				<bean:message key="message.dateFormat"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.value"/>:<html:text bundle="HTMLALT_RESOURCES" altKey="text.registrationValue" property="registrationValue" />
				&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
				<br /><br /><br />
			</td>
		</tr>
		<%-- Show defined Payment Phases--%>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="message.masterDegree.gratuity.paymentPhases.help"/>
			</td>
		</tr>
		<logic:present name="infoPaymentPhases">
			<bean:size id="numberPayments" name="infoPaymentPhases"/>
			<logic:greaterThan name="numberPayments" value="0">
				<tr>
					<td colspan="2">
						<br />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<i><bean:message key="label.masterDegree.gratuity.definedPhases"/></i>
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
									<bean:message key="button.masterDegree.gratuity.remove"/></th>
							</tr>
							<logic:iterate id="phase" name="infoPaymentPhases" type="net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase" indexId="index">
								<tr>
									<logic:notEmpty name="phase" property="startDate">
										<td class="listClasses">
											<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.paymentPhases" property="paymentPhases" value="<%=Data.format2DayMonthYear(phase.getStartDate(), "/")%>"/>
											<%=Data.format2DayMonthYear(phase.getStartDate(), "/")%>
										</td>
									</logic:notEmpty>
									<logic:empty name="phase" property="startDate">
										<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.paymentPhases" property="paymentPhases" value=""/>
										<td class="listClasses">-</td>
									</logic:empty>
									<td class="listClasses">
										<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.paymentPhases" property="paymentPhases" value="<%=Data.format2DayMonthYear(phase.getEndDate(), "/")%>"/>
										<%=Data.format2DayMonthYear(phase.getEndDate(), "/")%>
									</td>
									<td class="listClasses">
										<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.paymentPhases" property="paymentPhases" value="<%=phase.getValue().toString()%>"/>
										<bean:write name="phase" property="value"/>
									</td>
									<td class="listClasses">
										<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedPhases" property="removedPhases">
											<bean:write name="index"/>
										</html:multibox>	
									</td>
								</tr>
							</logic:iterate>
							<tr>
								<td colspan="4" style="text-align:right">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
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
				<br />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<i><bean:message key="label.masterDegree.gratuity.phaseDefinition"/></i>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.paymentPeriod"/>
				<bean:message key="label.masterDegree.gratuity.from"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.initialDatePartialPayment" property="initialDatePartialPayment" maxlength="10" size="10" value=""/>
				<bean:message key="label.masterDegree.gratuity.until"/>&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.finalDatePartialPayment" property="finalDatePartialPayment" maxlength="10" size="10" value=""/>
				<bean:message key="message.dateFormat"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="label.masterDegree.gratuity.phaseValue"/>:
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.phaseValue" property="phaseValue" value=""/>
				&nbsp;<bean:message key="label.masterDegree.gratuity.euro"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				&nbsp;&nbsp;&nbsp;&nbsp;			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
					<bean:message key="button.masterDegree.gratuity.addPhase"/>
				</html:submit>
			</td>
		</tr>
	</table>
	<br /><br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
		<bean:message key="button.masterDegree.gratuity.submit"/>
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
		<bean:message key="button.cancel"/>
	</html:submit>
</html:form>