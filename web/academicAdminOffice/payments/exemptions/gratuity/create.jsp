<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions" /></h2>
	<br />


	<logic:messagesPresent message="true" property="context">
		<ul>
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="context">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul>
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	
	<fr:hasMessages for="createGratuityExemptionBean-percentage-or-amount" type="conversion">
		<ul>
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
		</ul>
	</fr:hasMessages>


	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="createGratuityExemptionBean" property="gratuityEvent.person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />

	<bean:define id="eventId" name="createGratuityExemptionBean" property="gratuityEvent.idInternal" />
	<bean:define id="personId" name="createGratuityExemptionBean" property="gratuityEvent.person.idInternal" />
	<fr:form action="<%="/exemptionsManagement.do?personId=" + personId + "&amp;eventId=" + eventId%>">

		<input alt="input.method" type="hidden" name="method" value="" />

		<fr:edit id="createGratuityExemptionBean" name="createGratuityExemptionBean" visible="false" />

		<fr:edit id="createGratuityExemptionBean-exemptionType" name="createGratuityExemptionBean"
			schema="CreateGratuityExemptionBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>

		<br />
	<%--	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions.other" />:</strong>
		<fr:edit id="createGratuityExemptionBean-percentage-or-amount" name="createGratuityExemptionBean"
			schema="CreateGratuityExemptionBean.edit-user-defined-percentage-or-amount">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:edit>
		
	 --%>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='createGratuityExemption';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.gratuityExemption.create" />
		</html:submit>
		
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='showExemptions';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.cancel" />
		</html:cancel>

	</fr:form>

</logic:present>
