<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />
<html:form action="/generateNewStudentsPasswords.do?method=generatePasswords" target="_blank">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

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
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.fromNumber" property="fromNumber"/>
			</td>		
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.toNumber" bundle="MANAGER_RESOURCES" /></b>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.toNumber" property="toNumber"/>
			</td>		
		</tr>
	</table>



	<br /><br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Gerar Passwords" styleClass="inputbutton" property="OK"/>
</html:form> 