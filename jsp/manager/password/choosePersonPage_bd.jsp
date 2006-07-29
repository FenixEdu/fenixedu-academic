<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<span class="error"><html:errors/></span>


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
