<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:notPresent name="loginBean">
	<logic:present name="loginRequest">
		<bean:define id="requestID" name="loginRequest" property="idInternal"/>
		<fr:view name="loginRequest" property="user.person.idDocumentType"/>: 
		<fr:edit id="documentID" name="bean" slot="string" type="java.lang.String" action="<%= "/loginRequest.do?method=register&oid=" + requestID %>"/> 
	</logic:present>
	
	<logic:notPresent name="loginRequest">
		<bean:message key="error.invalid.loginRequest" bundle="SITE_RESOURCES"/>
	</logic:notPresent>
</logic:notPresent>

<logic:present name="loginBean">
	<fr:edit id="edit.loginBean" name="loginBean" schema="edit.loginBean" action="<%= "/loginRequest.do?method=finish"%>"/> 
</logic:present>