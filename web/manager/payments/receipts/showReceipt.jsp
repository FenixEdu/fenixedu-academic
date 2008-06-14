<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<bean:define id="personId" name="receipt" property="person.idInternal" />
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
		key="label.payments.management" /></h2>


	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<p class="mtop15 mbottom05"><strong><bean:message
		bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="receipt" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>


	<fr:view name="receipt" schema="receipt.view-with-number-and-year">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop15" />
			<fr:property name="columnClasses" value=",tdhl1" />
		</fr:layout>
	</fr:view>


	<fr:view name="receipt" property="entries" schema="entry.view">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle4 mtop05 mbottom0 width700px" />
			<fr:property name="columnClasses"
				value="width8em acenter, width30em acenter,width8em acenter,width15em aright" />
			<fr:property name="sortBy" value="whenRegistered=desc" />
		</fr:layout>
	</fr:view>
	
	<br/>

	<fr:form
		action="<%="/payments.do?method=showReceipts&amp;personId=" + personId%>">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
		</html:cancel>
	</fr:form>

</logic:present>
