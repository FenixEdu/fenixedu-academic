<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="STUDENT">
    <h2><bean:message key="label.title.calendar" bundle="MESSAGING_RESOURCES" /></h2>

	<logic:notEmpty name="bean" property="registrations">
		<fr:form action="/ICalTimeTable.do?method=show">
			<fr:edit name="bean" id="bean" schema="registration.selection"/>

			<html:submit><bean:message key="messaging.submit.button" bundle="MESSAGING_RESOURCES" /></html:submit>
		</fr:form>
	</logic:notEmpty>
	<logic:empty  name="registrations">
		<p class="mvert15"><em><bean:message key="message.no.registration" bundle="STUDENT_RESOURCES"/></em></p>
	</logic:empty>
</logic:present>

