<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet list2">
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:hasMessages for="createGratuityExemptionBean-percentage-or-amount" type="conversion">
		<ul class="nobullet list2">
			<fr:messages>
				<li><span class="error0"><fr:message/></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>


	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="createGratuityExemptionBean" property="gratuityEvent.person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>



	<bean:define id="eventId" name="createGratuityExemptionBean" property="gratuityEvent.idInternal" />
	<bean:define id="personId" name="createGratuityExemptionBean" property="gratuityEvent.person.idInternal" />
	<fr:form action="<%="/exemptionsManagement.do?personId=" + personId + "&amp;eventId=" + eventId%>">
		<input alt="input.method" type="hidden" name="method" value="" />
		<fr:edit id="createGratuityExemptionBean" name="createGratuityExemptionBean" visible="false" />
		<fr:edit id="createGratuityExemptionBean-exemptionType" name="createGratuityExemptionBean"
			schema="CreateGratuityExemptionBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>

		<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions.other" /></strong></p>
		<fr:edit id="createGratuityExemptionBean-percentage-or-amount" name="createGratuityExemptionBean"
			schema="CreateGratuityExemptionBean.edit-user-defined-percentage-or-amount">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05 thmiddle" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createGratuityExemption';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.gratuityExemption.create" />
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit"
				onclick="this.form.method.value='showExemptions';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.cancel" />
			</html:cancel>
		</p>
	</fr:form>

</logic:present>
