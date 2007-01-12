<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="person" name="event" property="person" />
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.otherPartyPayment" /></h2>
	<br />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />
	<bean:define id="entries" name="event" property="otherPartyEntries" />
	<logic:notEmpty name="entries">
		<strong><bean:message  key="label.payments.otherPartyPayment.details" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong>
		<fr:view name="entries" schema="entry.view-for-other-parties">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />							
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="entries">
		<span class="error0">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.otherPartyPayment.noEntriesForEvent" />
		</span>
		<br/><br/>
	</logic:empty>
	
	<bean:define id="eventId" name="event" property="idInternal" />
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/payments.do?personId=" + personId + "&amp;eventId=" + eventId%>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='prepareCreateOtherPartyPayment';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.create" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back" />
		</html:submit>
	</fr:form>

</logic:present>
