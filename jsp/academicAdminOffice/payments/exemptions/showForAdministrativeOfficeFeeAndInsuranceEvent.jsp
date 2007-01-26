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
	<bean:define id="eventId" name="event" property="idInternal" />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions"/></strong>
	<logic:notEmpty name="event" property="administrativeOfficeFeeAndInsurancePenaltyExemption">
		<bean:define id="penaltyExemption" name="event" property="administrativeOfficeFeeAndInsurancePenaltyExemption" />
		<fr:view name="penaltyExemption" schema="AdministrativeOfficeFeeAndInsurancePenaltyExemption.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,,nowrap" />
			</fr:layout>
		</fr:view>
		<bean:define id="penaltyExemptionId" name="penaltyExemption" property="idInternal" />
		<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + penaltyExemptionId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
		</html:link>
	</logic:notEmpty>
	<logic:empty name="event" property="administrativeOfficeFeeAndInsurancePenaltyExemption">
		<p>
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions.noPenaltyExemptions" />
			</em>
		</p>
		<html:link action="<%="/exemptionsManagement.do?method=prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemption&amp;personId=" + personId + "&amp;eventId=" + eventId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
		</html:link>
	</logic:empty>
	
	<br/><br/>
	
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>
	</fr:form>

</logic:present>
