<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.curriculum"/></h2>
<html:errors/>
<html:form action="/viewCurriculum" focus="studentNumber">
	<html:hidden property="method" value="getStudentCP"/>
	<html:hidden property="page" value="1"/>
	<logic:present name="executionDegreeId">
		<html:hidden property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<table>
		<tr>
			<td><bean:message key="label.choose.student"/>&nbsp;</td>
			<td>
				<input type="text" name="studentNumber" size="5" maxlength="5"/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
</html:form>
