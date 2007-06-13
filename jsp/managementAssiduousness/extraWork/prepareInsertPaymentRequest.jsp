<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<em><bean:message key="title.extraWork" /></em>
<h2><bean:message key="link.paymentRequests" /></h2>

<p class="mtop15"><span class="error0"><html:errors /></span></p>


<logic:present name="extraWorkRequestFactory">
	<fr:form action="/extraWorkPaymentRequest.do?method=chooseUnitYearMonth">
		<fr:edit id="1" name="extraWorkRequestFactory" schema="choose.unitYearMonth">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.submit" />
			</html:submit>
		</p>
	</fr:form>
	
	<logic:notEmpty name="extraWorkRequestFactory" property="unitCode">
	<logic:notEmpty name="extraWorkRequestFactory" property="unit">
		<logic:empty name="extraWorkRequestFactory" property="employeesExtraWorkRequests">
			<p class="mtop15">
				<em><bean:message key="message.noPaymentRequests"/></em>
			</p>
		</logic:empty>
		<logic:notEmpty name="extraWorkRequestFactory" property="employeesExtraWorkRequests">
			<bean:define id="year" name="extraWorkRequestFactory" property="year"/>
			<bean:define id="month" name="extraWorkRequestFactory" property="month"/>
			<bean:define id="unitCode" name="extraWorkRequestFactory" property="unitCode"/>
			
			<strong><bean:message key="label.extraWorkDoneIn" bundle="ASSIDUOUSNESS_RESOURCES"/>:</strong>
			<fr:edit id="yearMonthHoursDone" name="extraWorkRequestFactory" schema="choose.hoursDoneIn" 
				action="/extraWorkPaymentRequest.do?method=chooseUnitYearMonth">
				<fr:destination name="invalid" path="/extraWorkPaymentRequest.do?method=chooseUnitYearMonth"/>
			</fr:edit>
			
			<bean:define id="doneInYear" name="extraWorkRequestFactory" property="yearMonthHoursDone.year"/>
			<bean:define id="doneInMonth" name="extraWorkRequestFactory" property="yearMonthHoursDone.month"/>
			
			<fr:form action="/extraWorkPaymentRequest.do?method=processPayments">
				<fr:edit id="extraWorkRequestFactory" name="extraWorkRequestFactory" visible="false"/>				
				<p>
					<bean:message key="label.extraWork.unitInitialbalance" bundle="ASSIDUOUSNESS_RESOURCES"/>:
					<fmt:formatNumber maxFractionDigits="3" minFractionDigits="3" pattern="#.###">
						<bean:write name="extraWorkRequestFactory" property="initialUnitBalance"/>
					</fmt:formatNumber>
				<p/>
				<logic:messagesPresent message="true">
					<html:messages id="message" message="true">
						<p><span class="error0"><bean:write name="message" /></span></p>
					</html:messages>
				</logic:messagesPresent>
				<fr:view name="extraWorkRequestFactory" property="employeesExtraWorkRequests" schema="view.extraWorkRequest">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 printborder" />
						<fr:property name="columnClasses" value="acenter,," />
						<logic:equal name="extraWorkRequestFactory" property="isMonthClosed" value="true">
							<fr:property name="link(view)" value="<%="/extraWorkPaymentRequest.do?method=chooseEmployee&year="+year.toString()+"&month="+month.toString()+"&unitCode="+unitCode.toString() + "&doneInYear="+doneInYear.toString()+"&doneInMonth="+doneInMonth.toString()%>" />
							<fr:property name="key(view)" value="link.paymentRequest" />
							<fr:property name="param(view)" value="employee.employeeNumber/employeeNumber,idInternal" />
							<fr:property name="bundle(view)" value="ASSIDUOUSNESS_RESOURCES" />
							<fr:property name="visibleIf(view)" value="extraWorkRequestFactory.isMonthClosed()"/>
						</logic:equal>
					</fr:layout>
				</fr:view>				
				<p>
					<bean:message key="label.extraWork.monthAmount" bundle="ASSIDUOUSNESS_RESOURCES"/>:
					<fmt:formatNumber maxFractionDigits="3" minFractionDigits="3" pattern="#.###">
						<bean:write name="extraWorkRequestFactory" property="monthAmount"/>
					</fmt:formatNumber>
				</p>
				<p>
					<bean:message key="label.extraWork.unitFinalbalance" bundle="ASSIDUOUSNESS_RESOURCES"/>:
					<fmt:formatNumber maxFractionDigits="3" minFractionDigits="3" pattern="#.###">
						<bean:write name="extraWorkRequestFactory" property="finalUnitBalance"/>
					</fmt:formatNumber>
				</p>
				<p>
					<logic:equal name="extraWorkRequestFactory" property="isMonthClosedForExtraWork" value="false">
					<logic:equal name="extraWorkRequestFactory" property="paymentConfirmed" value="false">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
							<bean:message key="button.confirmPayment" />
						</html:submit>
					</logic:equal>
					</logic:equal>
					<logic:equal name="extraWorkRequestFactory" property="isMonthClosedForExtraWork" value="false">
					<logic:equal name="extraWorkRequestFactory" property="paymentConfirmed" value="true">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible" property="cancelPayment">
							<bean:message key="button.cancelPayment" />
						</html:submit>
					</logic:equal>
					</logic:equal>
				</p>			
			</fr:form>		
		</logic:notEmpty>
	</logic:notEmpty>
	<logic:empty name="extraWorkRequestFactory" property="unit">
		<p class="mtop15">
			<em><bean:message key="message.inexistentCostCenter"/></em>
		</p>
	</logic:empty>
	</logic:notEmpty>
</logic:present>
