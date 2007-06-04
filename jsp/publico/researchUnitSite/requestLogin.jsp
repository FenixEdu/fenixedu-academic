<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h1><bean:message key="label.loginRequest" bundle="SITE_RESOURCES"/></h1>

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
		<p class="mbottom0"><bean:message key="label.loginRequest.introduction" bundle="SITE_RESOURCES"/></p>
		<p class="mtop0"><bean:message key="label.loginRequest.docID" bundle="SITE_RESOURCES"/></p>
		<p class="mbottom05">
			<fr:view name="loginRequest" property="user.person.idDocumentType"/>:
		</p>
		<fr:form action="<%= "/loginRequest.do?method=register&oid=" + requestID %>">
			<fr:edit id="documentID" name="bean" slot="string" type="java.lang.String"> 
				<fr:layout>
					<fr:property value="classes" name="tstyle5 mtop05 thmiddle"/>
				</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:present>
	
	<logic:notPresent name="loginRequest">
		<p><span class="error0"><bean:message key="error.invalid.loginRequest" bundle="SITE_RESOURCES"/></span></p>
	</logic:notPresent>
</logic:notPresent>


<logic:present name="loginBean">
	<p class="mbottom0"><bean:message key="label.loginRequest.instrutions" bundle="SITE_RESOURCES"/></p>
	<fr:form action="<%= "/loginRequest.do?method=finish"%>">
	<fr:edit id="edit.loginBean" name="loginBean" schema="edit.loginBean"> 
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mtop0"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="rowClasses" value=",,,,,bold,,"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/loginRequest.do?method=cycleLoginScreen"%>"/>
	</fr:edit>
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>