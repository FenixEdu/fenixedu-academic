<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>
<br />
<span class="error"><html:errors/></span>

<html:form action="/findPerson" focus="username">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="findPerson" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
<table>
	<tr>
		<td colspan="2" class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="info.manager.findPerson"/>
		</td>		
	</tr>
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.nameWord" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="50"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.identificationDocumentNumber" />:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.documentIdNumber" property="documentIdNumber" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.emailWord" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>	
</table>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message bundle="MANAGER_RESOURCES" key="button.search"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
</html:reset>	
</html:form>