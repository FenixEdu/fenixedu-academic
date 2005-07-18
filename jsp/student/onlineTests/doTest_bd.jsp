<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>

<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
%>

<logic:present name="infoStudentTestQuestionList">
	<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal"/>
	<bean:define id="student" name="testQuestion" property="student"/>
	<bean:define id="studentCode" name="student" property="number"/>
	
	<html:form action="/studentTests">
	<html:hidden property="method" value="doTest"/>
	
	<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
	<html:hidden property="studentCode" value="<%= studentCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
		<br/><br/>
		<b><bean:write name="date"/></b>
	</center>
	<jsp:include page="showStudentTest_bd.jsp">
		<jsp:param name="pageType" value="doTest" />
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton" property="submit"><bean:message key="button.submitTest"/></html:submit></td>
		<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
		<html:form action="/studentTests">
		<html:hidden property="method" value="testsFirstPage"/>
		<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
		<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
		<td><html:submit styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit></td></html:form>
	</tr>
	</table>	
	</logic:notEmpty>
</logic:present>
<center>
<logic:notPresent name="infoStudentTestQuestionList">
<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:notPresent>
</center>