<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.loginRequest" bundle="SITE_RESOURCES"/></h2>

	<logic:messagesPresent message="true">
			<p>
			<html:messages id="messages" message="true" bundle="SITE_RESOURCES">
				<span class="error0"><bean:write name="messages"/></span>
			</html:messages>
			</p>
	</logic:messagesPresent>

<logic:notPresent name="loginBean">
		
	<logic:present name="loginRequest">
		<bean:define id="requestID" name="loginRequest" property="idInternal"/>
		<fr:view name="loginRequest" property="user.person.idDocumentType"/>: 
		<fr:form action="<%= "/loginRequest.do?method=register&oid=" + requestID %>">
			<fr:edit id="documentID" name="bean" slot="string" type="java.lang.String" /> 
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:present>
	
	<logic:notPresent name="loginRequest">
		<bean:message key="error.invalid.loginRequest" bundle="SITE_RESOURCES"/>
	</logic:notPresent>
</logic:notPresent>

<logic:present name="loginBean">

	<fr:form action="<%= "/loginRequest.do?method=finish"%>">
	<fr:edit id="edit.loginBean" name="loginBean" schema="edit.loginBean"> 
		<fr:layout>
			<fr:property name="classes" value="tstyle5"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/loginRequest.do?method=cycleLoginScreen"%>"/>
	</fr:edit>
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>