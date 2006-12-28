<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="person" name="createOtherPartyPaymentBean" property="event.person" />
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.otherPartyPayment" /></h2>
	<br />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br /><br/>
	
	<bean:define id="eventId" name="createOtherPartyPaymentBean" property="event.idInternal" />
	<bean:define id="personId" name="person" property="idInternal" />
	<html:form action="<%="/payments.do?eventId=" + eventId + "&amp;personId=" + personId%>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
		
		<fr:edit id="confirmCreateOtherPartyPayment" name="createOtherPartyPaymentBean" visible="false" nested="true"/>
		
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.viewDetails" /></strong>
		<fr:view name="createOtherPartyPaymentBean" schema="CreateOtherPartyPaymentBean.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:view>
		<span class="warning0"><bean:message  key="label.payments.confirmCreateOtherPartyPayment" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		<br/><br/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='createOtherPartyPayment';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.yes" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='prepareCreateOtherPartyPayment';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.no" />
		</html:submit>
	</html:form>


</logic:present>
