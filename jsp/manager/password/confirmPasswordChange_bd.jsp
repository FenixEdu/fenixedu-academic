<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<span class="error"><html:errors/></span>


<br />







<logic:present name="infoPerson">
	
	<table>
		<tr>
			<td>
				<bean:message key="label.name" />
			</td>
			<td>
				<bean:write name="infoPerson" property="nome" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.identificationDocumentNumber" />
			</td>
			<td>
				<bean:write name="infoPerson" property="numeroDocumentoIdentificacao" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.identificationDocumentType" />
			</td>
			<td>
				<bean:write name="infoPerson" property="tipoDocumentoIdentificacao" />
			</td>
		</tr>
	</table>
	
	<br />
	<h2>
	
	<bean:define id="personID" name="infoPerson" property="idInternal" />
	<bean:define id="username" name="infoPerson" property="username" />
	<html:link page="<%= "/generateNewPassword.do?method=generatePassword&page=0&personID="
					+ pageContext.findAttribute("personID")
					+ "&username="
					+ pageContext.findAttribute("username")
			%>"  target="_blank">
			<bean:message key="link.operator.changePassword" />
		</html:link>
	</h2>
	
</logic:present>




