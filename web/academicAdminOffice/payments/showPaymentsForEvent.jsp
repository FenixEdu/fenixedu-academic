<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	
<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.extract" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="event" property="positiveEntries">	
	<fr:view name="event" property="positiveEntries" schema="entry.view-with-payment-mode">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
			<fr:property name="sortBy" value="whenRegistered=desc"/>
		</fr:layout>
	</fr:view>	
	
</logic:notEmpty>

	
<bean:define id="personId" name="person" property="idInternal" />
<fr:form action='<%= "/payments.do?method=showEventsWithPayments&amp;personId=" + personId %>'>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
</fr:form>

</logic:present>
