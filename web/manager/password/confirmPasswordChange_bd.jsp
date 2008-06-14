<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />







<logic:present name="infoPerson">
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.name" />
			</td>
			<td>
				<bean:write name="infoPerson" property="nome" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.identificationDocumentNumber" />
			</td>
			<td>
				<bean:write name="infoPerson" property="numeroDocumentoIdentificacao" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.identificationDocumentType" />
			</td>
			<td>
				<bean:define id="idType" name="infoPerson" property="idDocumentType"/>
				<bean:message bundle="MANAGER_RESOURCES" bundle="ENUMERATION_RESOURCES" key='<%=idType.toString()%>'/>
			</td>
		</tr>
	</table>
	
	<br />
	<h2>
	
	<bean:define id="personID" name="infoPerson" property="idInternal" />
	<bean:define id="username" name="infoPerson" property="username" />
	<bean:define id="link">/generateNewPassword.do?method=generatePassword&page=0&personID=<%= pageContext.findAttribute("personID") %>&amp;username=<%= pageContext.findAttribute("username") %></bean:define>
	<html:link module="/manager" page="<%= link %>" target="_blank">
			<bean:message bundle="MANAGER_RESOURCES" key="link.operator.changePassword" />
		</html:link>
	</h2>
	
</logic:present>




