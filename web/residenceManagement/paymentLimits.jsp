<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>



<fr:form action="/residenceManagement.do?method=importData">
<fr:edit id="paymentLimits" name="paymentLimits" schema="residence.bean.selectYear">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="postback" path="/residenceManagement.do?method=configurePaymentLimits"/>
</fr:edit>

<logic:present name="paymentLimits" property="residenceYear">
	<fr:edit name="paymentLimits" property="residenceYear.sortedMonths" schema="edit.payment.limit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thleft thlight"/>
		</fr:layout>
	</fr:edit>
</logic:present>

<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>