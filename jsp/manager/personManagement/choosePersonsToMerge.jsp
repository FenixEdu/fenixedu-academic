<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/mergePersons" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choosePersons" />
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" /> 1:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person1Username" property="person1Username" size="25"/>
			</td>		
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" /> 2:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person2Username" property="person2Username" size="25"/>
			</td>		
		</tr>	
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="button.next"/>
				</html:submit>
			</td>		
		</tr>
	</table>

</html:form>