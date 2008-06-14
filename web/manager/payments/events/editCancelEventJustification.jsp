<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message key="label.payments.cancelEvent"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

	<bean:define id="personId" name="cancelEventBean"
		property="event.person.idInternal" />

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="cancelEventBean" property="event.person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.event" /></strong></p>
	<fr:edit id="cancelEventBean" name="cancelEventBean"
		schema="CancelEventBean.editJustification">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="success"
			path="<%="/payments.do?method=cancelEvent&personId=" + personId.toString() %>" />
		<fr:destination name="cancel"
			path="<%="/payments.do?method=backToShowEvents&personId=" + personId.toString() %>" />
	</fr:edit>

</logic:present>