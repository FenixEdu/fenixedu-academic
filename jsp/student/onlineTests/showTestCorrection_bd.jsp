<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="studentTestQuestionList">
	<center>
	<logic:empty name="studentTestQuestionList">
		<h2><bean:message key="message.studentTest.no.available"/></h2>
	</logic:empty>
	
	<logic:notEmpty name="studentTestQuestionList" >
	<html:form action="/studentTests">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>

	<logic:iterate id="testQuestion" name="studentTestQuestionList" type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
		
	<bean:define id="objectCode" name="distributedTest" property="testScope.domainObject.idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%= testCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<%if(((Integer)testType).intValue()!=3){%>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<%}%>
	<jsp:include page="showStudentTest_bd.jsp">
		<jsp:param name="pageType" value="correction"/>
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<table align="center">
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="studentTestQuestionList">
<center>
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</center>
</logic:notPresent>