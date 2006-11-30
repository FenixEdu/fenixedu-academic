<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions" /></h2>
	<br />
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="gratuityExemption" property="gratuityEvent.person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />

	<bean:define id="personId" name="gratuityExemption" property="gratuityEvent.person.idInternal" />
	<fr:form action="<%="/payments.do?personId=" + personId%>">

		<bean:define id="gratuityExemptionClassName" name="gratuityExemption" property="class.simpleName" />
		<fr:edit id="gratuityExemption" name="gratuityExemption" visible="false" nested="true" />
		<fr:view name="gratuityExemption" schema="<%= gratuityExemptionClassName  + ".view"%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:view>

		<input type="hidden" name="method" value="" />

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"
			onclick="this.form.method.value='deleteExemption';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.gratuityExemption.delete" />
		</html:submit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"
			onclick="this.form.method.value='showGratuityEvents';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>

	</fr:form>


</logic:present>
