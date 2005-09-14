<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>


<br />
<html:form action="/generateNewStudentsPasswords.do?method=generatePasswords" target="_blank">
	<html:hidden property="page" value="1" />

	<h3><bean:message key="label.newPasswordForStudentRegistration" bundle="MANAGER_RESOURCES"/></h3>

	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="label.studentRange" bundle="MANAGER_RESOURCES"/>
			</td>		
		</tr>	
		<tr>
			<td><br/></td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.fromNumber" bundle="MANAGER_RESOURCES" /></b>
			</td>
			<td>
				<html:text property="fromNumber"/>
			</td>		
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.toNumber" bundle="MANAGER_RESOURCES" /></b>
			</td>
			<td>
				<html:text property="toNumber"/>
			</td>		
		</tr>
	</table>



	<br /><br />
	<html:submit value="Gerar Passwords" styleClass="inputbutton" property="OK"/>
</html:form> 