<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" /></h2>
<br />
<span class="error"><html:errors/></span>

<html:form action="/mergePersons" >
	<html:hidden property="method" value="choosePersons" />
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" /> 1:
			</td>
			<td>
				<html:text property="person1Username" size="25"/>
			</td>		
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" /> 2:
			</td>
			<td>
				<html:text property="person2Username" size="25"/>
			</td>		
		</tr>	
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="button.next"/>
				</html:submit>
			</td>		
		</tr>
	</table>

</html:form>