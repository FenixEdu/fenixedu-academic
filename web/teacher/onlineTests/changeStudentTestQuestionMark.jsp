<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.changeQuestionMark"/></h2>

<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentTestQuestionMark"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionCode" property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
<br/>
<br/>
<table>
<tr>
<td><bean:message key="label.student.classification" /></td>
<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.questionValue" size="1" property="questionValue" /></td>
</tr>
</table>
<table>
	<tr>
		<td><b><bean:message key="label.modifyFor"/></b></td>
	</tr>
	<logic:iterate id="studentsType" name="studentsTypeList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="studentsType" property="label"/></td>
			<bean:define id="disabled" value="false"/>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.studentsType" property="studentsType" value="<%=studentsType.getValue()%>"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<br/>
<table align="center">
<tr>
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.change"/></html:submit></td>
	<td><html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
	<%--
	<td>
		<html:form action="/testsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showStudentTest"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
	</td></html:form>
	--%>
</tr>
</table>