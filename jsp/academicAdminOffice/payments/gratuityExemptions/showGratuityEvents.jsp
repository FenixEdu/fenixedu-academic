<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions" /></h2>
	<br />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />
	<logic:notEmpty name="gratuityEvents">
		<fr:view name="gratuityEvents" schema="GratuityEvent.view">
			<fr:layout name="tabular">
				
				<fr:property name="classes" value="tstyle4" />
				
				<fr:property name="linkFormat(createExemption)" value="/payments.do?method=prepareCreateExemption&gratuityEventId=${idInternal}" />
				<fr:property name="key(createExemption)" value="label.payments.gratuityExemptions.createExemption" />
				<fr:property name="bundle(createExemption)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="visibleIf(createExemption)" value="gratuityExemptionNotAvailable" />
				
				<fr:property name="linkFormat(viewExemption)" value="/payments.do?method=viewExemption&gratuityEventId=${idInternal}" />
				<fr:property name="key(viewExemption)" value="label.payments.gratuityExemptions.viewExemption" />
				<fr:property name="bundle(viewExemption)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="visibleIf(viewExemption)" value="gratuityExemptionAvailable" />
				
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="gratuityEvents">
		<span class="error0"><!-- Error messages go here --><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.gratuityExemptions.noGratuityEvents" /></span>
	</logic:empty>
	
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/payments.do?method=backToShowOperations&personId=" + personId%>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>
	</fr:form>

</logic:present>
