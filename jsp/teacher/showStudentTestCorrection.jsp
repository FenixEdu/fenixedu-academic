<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoStudentTestQuestionList">
<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.test.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:present name="successfulChanged">
		<span class="error"><bean:message key="message.successfulChanged"/></span>
		<br/>
		<table>
		<logic:iterate id="changed" name="successfulChanged">
		<tr><td><bean:write name="changed" property="label"/></td>
		<td><bean:write name="changed" property="value"/></td></tr>
		</logic:iterate>
		</table>
	</logic:present>
	<logic:present name="insuccessfulAdvisoryDistribution">
		<span class="error"><bean:message key="message.insuccessfulAdvisoryDistributionForAll"/></span>
	</logic:present>
	<logic:present name="infoStudentList">
		<bean:size id="infoStudentListSize" name="infoStudentList"/>
		<logic:notEqual name="infoStudentListSize" value="0">
			<table><tr><td><span class="error"><bean:message key="message.insuccessfulAdvisoryDistribution"/></span></td></tr>
			<logic:iterate id="student" name="infoStudentList">
				<tr><td><span class="error"><bean:write name="student" property="number"/></span></td></tr>
			</logic:iterate>
			</table>
		</logic:notEqual>
	</logic:present>
	
	<html:form action="/studentTestManagement">
	<html:hidden property="method" value="showTestMarks"/>

	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="DataBeans.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
		
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal"/>
	<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden property="distributedTestCode" value="<%= testCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<%if(((Integer)testType).intValue()!=3){%>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<%}%>
	<jsp:include page="showStudentTest.jsp">
		<jsp:param name="pageType" value="correction"/>
		<jsp:param name="correctionType" value="studentCorrection"/>
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.test.no.available"/></h2>
</center>
</logic:notPresent>