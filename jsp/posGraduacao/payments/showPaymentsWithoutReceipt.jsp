<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:form action="/payments.do">
	<html:hidden property="method" />

	<h2><bean:message
		key="label.masterDegree.administrativeOffice.payments.paymentsWithoutReceipt" /></h2>
	<br />
	
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>


	<logic:notEmpty name="createReceiptBean" property="entries">
		<strong>METER AQUI</strong>
		<fr:view name="createReceiptBean" property="party"
			schema="party.view-with-name">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
			</fr:layout>
		</fr:view>


		<strong>METER AQUI</strong>
		<fr:edit id="createReceiptBean" name="createReceiptBean"
			schema="createReceiptBean.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
			</fr:layout>
		</fr:edit>

		<strong>METER AQUI</strong>
		<fr:edit id="createReceiptBean-entries-part" name="createReceiptBean"
			property="entries" schema="selectableEntryBean.view">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright" />
			</fr:layout>
		</fr:edit>
		
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='createReceipt';"><bean:message key="button.masterDegree.administrativeOffice.payments.receipt"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="createReceiptBean" property="entries">
		<span class="error">
			<bean:message key="label.masterDegree.administrativeOffice.payments.noPaymentsWithoutReceipt" />
		</span>
	</logic:empty>

</html:form>
