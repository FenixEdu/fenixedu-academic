<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.extraWork" /></em>
<h2><bean:message key="link.paymentRequests" /></h2>

<p class="mtop15"><span class="error0"><html:errors /></span></p>


<logic:present name="extraWorkRequestFactory">
	<fr:form
		action="/extraWorkPaymentRequest.do?method=chooseUnitYearMonth"
		encoding="multipart/form-data">
		<fr:edit id="1" name="extraWorkRequestFactory"
			schema="choose.unitYearMonth">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit></p>
		<logic:notEmpty name="extraWorkRequestFactory" property="unitCode">
		<logic:notEmpty name="extraWorkRequestFactory" property="unit">
			<%-- %>
			<fr:edit id="2" name="extraWorkRequestFactory" schema="choose.extraWorkEmployee"/>
			--%>
			<logic:empty name="extraWorkRequestFactory" property="employeeList">
				<p class="mtop15">
					<em><bean:message key="message.noPaymentRequests"/></em>
				</p>
			</logic:empty>
			<logic:notEmpty name="extraWorkRequestFactory" property="employeeList">
			<bean:define id="year" name="extraWorkRequestFactory" property="year"/>
			<bean:define id="month" name="extraWorkRequestFactory" property="month"/>
			<bean:define id="unitCode" name="extraWorkRequestFactory" property="unitCode"/>
			<fr:view name="extraWorkRequestFactory" property="employeeList"
				schema="show.employeePersonalInformation2">

				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 printborder" />
					<fr:property name="columnClasses" value="acenter,," />
					<fr:property name="link(view)" value="<%="/extraWorkPaymentRequest.do?method=chooseEmployee&year="+year.toString()+"&month="+month.toString()+"&unitCode="+unitCode.toString()%>" />
					<fr:property name="key(view)" value="link.paymentRequest" />
					<fr:property name="param(view)" value="employeeNumber" />
					<fr:property name="bundle(view)" value="ASSIDUOUSNESS_RESOURCES" />
				</fr:layout>
			</fr:view>
			</logic:notEmpty>
		</logic:notEmpty>
		<logic:empty name="extraWorkRequestFactory" property="unit">
			<p class="mtop15">
				<em><bean:message key="message.inexistentCostCenter"/></em>
			</p>
		</logic:empty>
		</logic:notEmpty>
	</fr:form>
</logic:present>
