<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="label.manager.findPerson" /></h2>
<br />
<span class="error"><html:errors/></span>

<html:form action="/findPerson" focus="username">
<html:hidden property="method" value="findPerson" />
<html:hidden property="page" value="1" />
<table>
	<tr>
		<td colspan="2" class="infoop">
			<bean:message key="info.manager.findPerson"/>
		</td>		
	</tr>
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="property.login.username" />
		</td>
		<td>
			<html:text property="username" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message key="label.nameWord" />
		</td>
		<td>
			<html:text property="name" size="50"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message key="label.identificationDocumentNumber" />:
		</td>
		<td>
			<html:text property="documentIdNumber" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message key="label.emailWord" />
		</td>
		<td>
			<html:text property="email" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>	
</table>

<html:submit styleClass="inputbutton">
	<bean:message key="button.search"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>	
</html:form>