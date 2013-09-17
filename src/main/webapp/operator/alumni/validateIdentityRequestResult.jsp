<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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
