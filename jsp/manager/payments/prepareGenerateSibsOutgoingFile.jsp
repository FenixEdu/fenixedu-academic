<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:link action="/degreePaymentsManagement.do?method=generateSibsOutgoingFile">
	<bean:message key="label.payments.generateSibsOutgoingFile" bundle="MANAGER_RESOURCES" />
</html:link>