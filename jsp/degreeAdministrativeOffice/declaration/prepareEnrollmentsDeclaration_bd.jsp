<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<h2><bean:message key="title.student.print.enrollmentsDeclaration"/></h2>
<html:form action="/generateEnrollmentsDeclaration.do?method=generate" target="_blank">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<br/>
<table>
<tr>
	<td><bean:message key="label.choose.firstStudent"/></td>
	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.firstStudentNumber" size="8" maxlength="8" property="firstStudentNumber"/></td>
</tr>	
<tr>
	<td><bean:message key="label.choose.lastStudent"/></td>
	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.lastStudentNumber" size="8" maxlength="8" property="lastStudentNumber"/></td>
</tr>
</table>
<br/><br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">Continuar</html:submit>
</html:form>