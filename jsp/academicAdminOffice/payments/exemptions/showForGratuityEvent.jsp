<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" /></h2>
	<br />
	
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />
	
	<strong><em>
		<app:labelFormatter name="event" property="description">
			<app:property name="enum" value="ENUMERATION_RESOURCES"/>
			<app:property name="application" value="APPLICATION_RESOURCES"/>
		</app:labelFormatter>
		</em>
	</strong>
	
	<br /><br />
	<bean:define id="personId" name="person" property="idInternal" />
	<bean:define id="gratuityEventId" name="event" property="idInternal" />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions"/></strong>
	<logic:notEmpty name="event" property="gratuityExemption">
		<bean:define id="gratuityExemption" name="event" property="gratuityExemption" />
		<bean:define id="gratuityExemptionClassName" name="gratuityExemption" property="class.simpleName" />
		<fr:view name="gratuityExemption" schema="<%= gratuityExemptionClassName + ".view"%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,,nowrap" />
			</fr:layout>
		</fr:view>
		<bean:define id="gratuityExemptionId" name="gratuityExemption" property="idInternal" />
		<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + gratuityExemptionId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
		</html:link>
	</logic:notEmpty>
	<logic:empty name="event" property="gratuityExemption">
		<p>
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions.noGratuityExemption" />
			</em>
		</p>
		<html:link action="<%="/exemptionsManagement.do?method=prepareCreateGratuityExemption&amp;personId=" + personId + "&amp;eventId=" + gratuityEventId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
		</html:link>
	</logic:empty>
	
	<br/><br/>
	
	<logic:equal name="hasPaymentPlan" value="true">
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions"/></strong>
		<logic:notEmpty name="event" property="installmentPenaltyExemptions">
			<fr:view name="event" property="installmentPenaltyExemptions" schema="InstallmentPenaltyExemption.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4" />
					<fr:property name="columnClasses" value=",,,nowrap" />
					<fr:property name="linkFormat(deletePenaltyExemption)" value="<%="/exemptionsManagement.do?exemptionId=${idInternal}&amp;method=deleteExemption&amp;personId=" + personId%>" />
					<fr:property name="key(deletePenaltyExemption)" value="label.delete" />
					<fr:property name="bundle(deletePenaltyExemption)" value="ACADEMIC_OFFICE_RESOURCES" />
					<fr:property name="sortBy" value="installment.startDate=asc" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="event" property="installmentPenaltyExemptions">
			<p>
				<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions.noPenaltyExemptions" />
				</em>
			</p>
		</logic:empty>
		<html:link action="<%="/exemptionsManagement.do?method=prepareCreateInstallmentPenaltyExemption&amp;personId=" + personId + "&amp;eventId=" + gratuityEventId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
		</html:link>
	</logic:equal>
	
	<br/><br/>
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>
	</fr:form>

</logic:present>
