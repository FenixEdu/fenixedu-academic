<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>
<br/>

<fr:form action="/sendEmail.do?method=send">

	<bean:define id="allowChangeSender" type="java.lang.Boolean" name="sendEmailBean" property="allowChangeSender"/>
	<% final String schemaName = allowChangeSender.booleanValue()
			? "net.sourceforge.fenixedu.dataTransferObject.SendEmailBean.common"
			: "net.sourceforge.fenixedu.dataTransferObject.SendEmailBean.common.hidden.sender"; %>

	<fr:edit id="sendEmailForm" name="sendEmailBean" schema="<%= schemaName %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		</fr:layout>
	</fr:edit>

	<fr:edit id="sendEmailResult" name="sendEmailBean" schema="net.sourceforge.fenixedu.dataTransferObject.SendEmailBean.recipients">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight mtop05"/>
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='moveEnrolments';">
		<bean:message bundle="MANAGER_RESOURCES" key="button.send" />
	</html:submit>

</fr:form>