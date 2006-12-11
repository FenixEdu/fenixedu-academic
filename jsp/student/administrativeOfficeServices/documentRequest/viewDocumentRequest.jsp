<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement.consult"/></h2>

<logic:messagesPresent message="true">
	<span class="error0"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 mtop15" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<html:form action="/documentRequest.do?method=viewDocumentRequests">
	<html:hidden property="registrationId"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.back" /></html:submit>
</html:form>