<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>


<br />
<html:form action="/findPerson.do?method=findPerson">
	<html:hidden property="page" value="1" />

	<h3><bean:message key="link.operator.newPassword" /></h3>

	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="label.operator.choosePerson"/>
			</td>		
		</tr>	
		<tr>
			<td>
				<b><bean:message key="property.login.username" /></b>
			</td>
			<td>
				<html:text property="username" />
			</td>		
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.person.identificationDocumentNumber" /></b>
			</td>
			<td>
				<html:text property="documentIdNumber" />
			</td>		
		</tr>
	</table>



	<br /><br />
	<html:submit value="Seguinte" styleClass="inputbutton" property="OK"/>
</html:form> 
