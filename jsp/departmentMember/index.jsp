<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:messagesPresent property="message.not-authorized">
	<h4>
		<bean:message key="message.not-authorized"/>
	</h4>
</logic:messagesPresent>

<logic:messagesPresent property="message.unknown-error">
	<bean:message key="message.unknown-error"/>
</logic:messagesPresent>

<logic:messagesNotPresent>

	<h2><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h2>
	<p><bean:message key="label.departmentMember.welcome" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
	
</logic:messagesNotPresent>

