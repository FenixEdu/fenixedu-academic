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
<b>
<bean:message key="message.exist"/>&nbsp;
<bean:write name="distrubutedTestsSize"/>&nbsp;
<bean:message key="message.studentsWithDistributedTest"/>
</b>
<br/>
<br/>
<table>
	<tr>
		<td width="100" class="listClasses-header"><bean:message key="label.number"/></td>
		<td class="listClasses-header"><bean:message key="label.name"/></td>
	</tr>
	<logic:iterate id="student" name="infoStudentList" type="DataBeans.InfoStudent">
		<bean:define id="person" name="student" property="infoPerson"/>
		<bean:define id="studentCode" name="student" property="idInternal"/>
		<tr>
			<td class="listClasses"><bean:write name="student" property="number"/></td>
			<td class="listClasses"><bean:write name="person" property="nome"/></td>
			<td><html:link page="<%= "/testsManagement.do?method=showStudentTest&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showStudentTest"/></html:link></td>
			<td><html:link page="<%= "/testsManagement.do?method=showStudentTestLog&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showLog"/></html:link></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<html:form action="/testDistribution">
<html:hidden property="method" value="showDistributedTests"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>	
</logic:notEqual>
