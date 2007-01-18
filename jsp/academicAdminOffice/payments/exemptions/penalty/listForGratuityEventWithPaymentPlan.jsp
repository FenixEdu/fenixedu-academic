<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions" /></h2>
	
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<bean:define id="person" name="gratuityEvent" property="person" />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<bean:define id="personId" name="person" property="idInternal" />
	<logic:notEmpty name="gratuityEvent" property="penaltyExemptions">
			<strong><bean:message  key="label.payments.exemptions" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
			<fr:view name="gratuityEvent" property="penaltyExemptions" schema="InstallmentPenaltyExemption.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 tdleftm mtop025" />
					<fr:property name="columnClasses" value=",," />
					<fr:property name="linkFormat(deletePenaltyExemption)" value="<%="/payments.do?penaltyExemptionId=${idInternal}&amp;method=deletePenaltyExemption&amp;personId=" + personId%>" />
					<fr:property name="key(deletePenaltyExemption)" value="label.delete" />
					<fr:property name="bundle(deletePenaltyExemption)" value="ACADEMIC_OFFICE_RESOURCES" />
					<fr:property name="sortBy" value="installment.startDate=asc" />
				</fr:layout>
			</fr:view>
	</logic:notEmpty>

	<logic:empty name="gratuityEvent" property="penaltyExemptions">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.penaltyExemptions.noPenaltyExemptions" />.</em>
	</logic:empty>
	
	<br/>
	<br/>
	<bean:define id="gratuityEventId" name="gratuityEvent" property="idInternal" />
	<fr:form action='<%= "/payments.do?personId=" + personId + "&amp;gratuityEventId=" + gratuityEventId %>'>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='prepareCreatePenaltyExemption';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='showGratuityEvents';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back" />
		</html:cancel>
	</fr:form>

</logic:present>
