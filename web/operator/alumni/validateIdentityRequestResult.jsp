<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<!-- validateIdentityRequestResult.jsp -->

<logic:present role="OPERATOR">

	<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
	<h2><bean:message key="alumni.validate.identity.request" bundle="MANAGER_RESOURCES"/></h2>

	<bean:define id="message" name="identityRequestResult" type="java.lang.String"/>
	<p>
		<bean:message key="<%= message %>" bundle="MANAGER_RESOURCES"/>
	</p>
	
	<html:link page="/alumni.do?method=prepareIdentityRequestsList">
		<bean:message key="label.back" bundle="MANAGER_RESOURCES"/>
	</html:link>
	
</logic:present>
