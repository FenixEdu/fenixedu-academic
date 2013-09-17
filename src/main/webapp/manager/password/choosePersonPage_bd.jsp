<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />
<html:form action="/generateNewPassword.do?method=findPerson">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />


	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.operator.choosePerson" /></h3>
	<br/>

	<table>
		<tr>
			<td>
				Username
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" />
			</td>
		
		</tr>
	</table>




	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Seguinte" styleClass="inputbutton" property="OK"/>
</html:form> 
