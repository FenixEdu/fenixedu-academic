<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions" /></h2>
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


<fr:hasMessages for="create-penalty-exemption-bean" type="conversion">
	<ul>
		<fr:messages>
			<li><span class="error0"><fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="event" name="createPenaltyExemptionBean"	property="event" />

<bean:define id="person" name="event" property="person" />
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br />

<bean:define id="eventId" name="event" property="externalId" />
<fr:form action='<%="/exemptionsManagement.do?eventId=" + eventId%>'>

	<html:hidden property="method" value="" />

	<fr:edit id="create-penalty-exemption-bean"
		schema="CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean.edit" name="createPenaltyExemptionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="invalid"
			path='<%="/exemptionsManagement.do?method=prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid&amp;eventId=" + eventId%>'/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='createAdministrativeOfficeFeeAndInsurancePenaltyExemption';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" 
		onclick="this.form.method.value='showExemptions';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.cancel" />
	</html:cancel>
</fr:form>
