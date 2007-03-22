<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="person" name="createOtherPartyPaymentBean" property="event.person" />
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.otherPartyPayment" /></h2>
	
	<logic:messagesPresent message="true">
		<ul class="nobullet list2">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>


	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	
	<bean:define id="eventId" name="createOtherPartyPaymentBean" property="event.idInternal" />
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/payments.do?personId=" + personId + "&amp;eventId=" + eventId%>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" value=""/>
		<fr:edit id="createOtherPartyPayment" name="createOtherPartyPaymentBean" schema="CreateOtherPartyPaymentBean.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thmiddle thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareCreateOtherPartyPaymentInvalid"/>
		</fr:edit>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePrintGuideForOtherParty';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.guide" />
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='confirmCreateOtherPartyPayment';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payment" />
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='showOtherPartyPaymentsForEvent';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back" />
			</html:cancel>
		</p>
	</fr:form>

</logic:present>
