<%@ page language="java" %>
<%@ page import="javax.swing.ImageIcon" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:size id="distrubutedTestsSize" name="infoStudentList"/>

<logic:equal name="distrubutedTestsSize" value="0">
	<span class="error"><bean:message key="message.tests.no.students.distributedTests"/></span>
</logic:equal>

<logic:notEqual name="distrubutedTestsSize" value="0">
<table>
	<tr>
		<td width="100" class="listClasses-header"><bean:message key="label.number"/></td>
		<td width="100" class="listClasses-header"><bean:message key="label.name"/></td>
	</tr>
	<logic:iterate id="student" name="infoStudentList" type="DataBeans.InfoStudent">
		<bean:define id="person" name="student" property="infoPerson"/>
		<bean:define id="studentCode" name="student" property="idInternal"/>
		<tr>
			<td class="listClasses">
				<html:link page="<%= "/testsManagement.do?method=showStudentTest&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>">
					<bean:write name="student" property="number"/>
				</html:link>
			</td>
			<td class="listClasses"><bean:write name="person" property="nome"/></td>
		</tr>
	</logic:iterate>
</table>
</logic:notEqual>
