<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="TREASURY">

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal" />
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<fr:form action='<%= "/payments.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>'>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />

	<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.eventsWithInstallments" /></h2>
	
	<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
	<br/><br/>

	
	<logic:messagesPresent message="true" property="context">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="context" bundle="TREASURY_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<logic:messagesPresent message="true"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:hasMessages for="paymentsManagementDTO" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message/></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	

	<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person" /></strong>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<p class="mbottom05 mtop15"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.currentEvents" /></strong></p>
	<logic:notEmpty name="paymentsManagementDTO" property="entryDTOs">
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO"
			visible="false" />

		<fr:edit id="payment-entries" name="paymentsManagementDTO"
			property="entryDTOs" schema="entryDTO.edit-with-installments">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareShowEventsWithInstallmentsInvalid"/>
		</fr:edit>
		<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePrintGuideWithInstallments';"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.guide"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePaymentWithInstallments';"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.payment"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>
		</p>
	</logic:notEmpty>

	<logic:empty name="paymentsManagementDTO" property="entryDTOs">
		<p class="mvert15">
			<em><bean:message bundle="TREASURY_RESOURCES" key="label.payments.events.noEvents" />.</em>
		</p>
	</logic:empty>

</fr:form>

</logic:present>
