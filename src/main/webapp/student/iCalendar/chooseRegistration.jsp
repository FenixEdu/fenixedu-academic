<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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

