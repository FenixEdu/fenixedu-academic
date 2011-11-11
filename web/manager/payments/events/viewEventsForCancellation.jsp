<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events" /></h2>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events" /></strong></p>
	<logic:empty name="person" property="events">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.events.noEvents" />
	</logic:empty>

	<logic:notEmpty name="person" property="events">
		<fr:view name="person" property="events" schema="AccountingEvent.view.by.manager">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
				<fr:property name="columnClasses"
					value=",acenter,,aright,aright,aright,acenter" />
				
				<fr:property name="linkFormat(cancel)" value="/payments.do?method=prepareCancelEvent&amp;eventId=${idInternal}" />
				<fr:property name="key(cancel)" value="label.cancel" />
				<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES" />
				<fr:property name="order(cancel)" value="5" />
				<fr:property name="visibleIfNot(cancel)" value="cancelled" />

			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
	<bean:define id="personId" name="person" property="idInternal" />
	
	<fr:form
		action="<%="/payments.do?method=showOperations&amp;personId=" + personId%>">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
		</html:cancel>
	</fr:form>

</logic:present>