<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>


<br />
<html:form action="/generateNewPassword.do?method=findPerson">
	<html:hidden property="page" value="1" />


	<h3><bean:message key="label.operator.choosePerson" /></h3>
	<br>

	<table>
		<tr>
			<td>
				Username
			</td>
			<td>
				<html:text property="username" />
			</td>
		
		</tr>
	</table>




	<html:submit value="Seguinte" styleClass="inputbutton" property="OK"/>
</html:form> 
