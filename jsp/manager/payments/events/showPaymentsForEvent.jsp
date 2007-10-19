<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="event" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.payedEvents2" /></strong></p>
	
	<logic:empty name="event" property="positiveEntries">
		<bean:message key="label.payments.not.found"
			bundle="ACADEMIC_OFFICE_RESOURCES" />
	</logic:empty>
	
	<logic:notEmpty name="event" property="positiveEntries">	
		<fr:view name="event" property="positiveEntries" schema="entry.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
				<fr:property name="columnClasses"
					value=",,,aright,aright,aright,acenter" />
					
				<fr:property name="linkFormat(annul)" value="/payments.do?method=prepareAnnulTransaction&amp;transactionId=${accountingTransaction.idInternal}&amp;eventId=${accountingTransaction.event.idInternal}" />
				<fr:property name="key(annul)" value="label.payments.annul" />
				<fr:property name="bundle(annul)" value="MANAGER_RESOURCES" />
				<fr:property name="order(annul)" value="0" />
			</fr:layout>
		</fr:view>

	</logic:notEmpty>
	
	<br/><br/>

	<bean:define id="personId" name="event" property="person.idInternal" />

	<fr:form
		action="<%="/payments.do?method=backToShowEvents&amp;personId=" + personId.toString()%>">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
		</html:cancel>
	</fr:form>


</logic:present>