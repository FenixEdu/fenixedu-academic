<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<div class="well" style="width: 70%">
	<bean:message bundle="MANAGER_RESOURCES" key="info.manager.findPerson"/>
</div>

<fr:form action="/findPerson.do?method=findPerson">
<table>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" />
		</td>
		<td>
			<input type="text" name="username" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.nameWord" />
		</td>
		<td>
			<input type="text" name="name" size="50"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.identificationDocumentNumber" />:
		</td>
		<td>
			<input type="text" name="documentIdNumber" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.emailWord" />
		</td>
		<td>
			<input type="text" name="email" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.mechanographical.number" />
		</td>
		<td>
			<input type="text" name="mechanoGraphicalNumber" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>	
</table>

	<html:submit bundle="HTMLALT_RESOURCES">
		<bean:message bundle="MANAGER_RESOURCES" key="button.search"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>	
</fr:form>