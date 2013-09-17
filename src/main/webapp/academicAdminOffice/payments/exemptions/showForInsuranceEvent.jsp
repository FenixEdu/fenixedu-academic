<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list2">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
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

<h3>
	<app:labelFormatter name="event" property="description">
		<app:property name="enum" value="ENUMERATION_RESOURCES"/>
		<app:property name="application" value="APPLICATION_RESOURCES"/>
	</app:labelFormatter>
</h3>

<bean:define id="personId" name="person" property="externalId" />
<bean:define id="eventId" name="event" property="externalId" />

<p class="mtop2 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentExemptions"/></strong></p>
<logic:notEmpty name="event" property="insuranceExemption">
	<bean:define id="insuranceExemption" name="event" property="insuranceExemption" />
	<fr:view name="insuranceExemption" schema="InsuranceExemption.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
	<bean:define id="exemptionId" name="insuranceExemption" property="externalId" />
	<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + exemptionId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
	</html:link>
</logic:notEmpty>
<logic:empty name="event" property="insuranceExemption">
	<p>
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentExemptions.noPaymentExemptions" />
		</em>
	</p>
	<html:link action="<%="/exemptionsManagement.do?method=prepareCreateInsuranceExemption&amp;personId=" + personId + "&amp;eventId=" + eventId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
	</html:link>
</logic:empty>

<bean:define id="personId" name="person" property="externalId" />
<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>
	</p>
</fr:form>
