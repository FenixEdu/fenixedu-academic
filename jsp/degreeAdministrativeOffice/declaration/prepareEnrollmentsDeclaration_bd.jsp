<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors/></span>
<h2><bean:message key="title.student.print.enrollmentsDeclaration"/></h2>
<html:form action="/generateEnrollmentsDeclaration.do?method=generate" target="_blank">
<html:hidden property="page" value="1"/>
<br>
<table>
<tr>
	<td><bean:message key="label.choose.firstStudent"/></td>
	<td><html:text size="8" maxlength="8" property="firstStudentNumber"/></td>
</tr>	
<tr>
	<td><bean:message key="label.choose.lastStudent"/></td>
	<td><html:text size="8" maxlength="8" property="lastStudentNumber"/></td>
</tr>
</table>
<br><br>
<html:submit styleClass="inputbutton">Continuar</html:submit>
</html:form>