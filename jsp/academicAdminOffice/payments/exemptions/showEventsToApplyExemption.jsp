<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" /></h2>

	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events"/></strong></p>
	<logic:notEmpty name="eventsToApplyExemption">
		<fr:view name="eventsToApplyExemption" schema="AccountingEvent.view">
			<fr:layout name="tabular">
				
				<fr:property name="classes" value="tstyle4 mtop05" />
				
				<fr:property name="linkFormat(showExemptions)" value="/exemptionsManagement.do?method=showExemptions&eventId=${idInternal}" />
				<fr:property name="key(showExemptions)" value="label.payments.exemptions" />
				<fr:property name="bundle(showExemptions)" value="ACADEMIC_OFFICE_RESOURCES" />
								
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="eventsToApplyExemption">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions.noEventsToApplyExemption" />
			</em>
		</p>
	</logic:empty>
	
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/payments.do?method=backToShowOperations&amp;personId=" + personId%>">
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
			</html:submit>
		</p>
	</fr:form>

</logic:present>
