<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
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

	<bean:define id="personId" name="person" property="idInternal" />
	<bean:define id="eventId" name="event" property="idInternal" />
	<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions"/></strong></p>
	<logic:notEmpty name="event" property="improvementOfApprovedEnrolmentPenaltyExemption">
		<bean:define id="penaltyExemption" name="event" property="improvementOfApprovedEnrolmentPenaltyExemption" />
		<fr:view name="penaltyExemption" schema="ImprovementOfApprovedEnrolmentPenaltyExemption.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
			</fr:layout>
		</fr:view>
		<bean:define id="penaltyExemptionId" name="penaltyExemption" property="idInternal" />
		<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + penaltyExemptionId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
		</html:link>
	</logic:notEmpty>
	<logic:empty name="event" property="improvementOfApprovedEnrolmentPenaltyExemption">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions.noPenaltyExemptions" />
			</em>
		</p>
		<p>
			<html:link action="<%="/exemptionsManagement.do?method=prepareCreateImprovementOfApprovedEnrolmentPenaltyExemption&amp;personId=" + personId + "&amp;eventId=" + eventId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
			</html:link>
		</p>
	</logic:empty>
	

	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
			</html:submit>
		</p>
	</fr:form>

</logic:present>
