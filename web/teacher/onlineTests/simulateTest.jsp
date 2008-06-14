<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoStudentTestQuestionList">
<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.test.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:present name="successfulChanged">
		<span class="error"><!-- Error messages go here --><bean:message key="message.successfulChanged"/></span>
		<br/>
		<table>
		<logic:iterate id="changed" name="successfulChanged">
		<tr><td><bean:write name="changed" property="label"/></td>
		<td><bean:write name="changed" property="value"/></td></tr>
		</logic:iterate>
		</table>
	</logic:present>
	<logic:present name="insuccessfulAdvisoryDistribution">
		<span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistributionForAll"/></span>
	</logic:present>
	<logic:present name="infoStudentList">
		<bean:size id="infoStudentListSize" name="infoStudentList"/>
		<logic:notEqual name="infoStudentListSize" value="0">
			<table><tr><td><span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistribution"/></span></td></tr>
			<logic:iterate id="student" name="infoStudentList">
				<tr><td><span class="error"><!-- Error messages go here --><bean:write name="student" property="number"/></span></td></tr>
			</logic:iterate>
			</table>
		</logic:notEqual>
	</logic:present>
	
	<html:form action="/studentTestManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="simulateTest"/>

	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
		
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= testCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testInformation" property="testInformation" name="distributedTest" property="testInformation"/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testType" property="testType" value="<%=testType.toString()%>"/>
	<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableCorrection" property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.imsFeedback" property="imsFeedback" name="distributedTest" property="imsFeedback"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<jsp:include page="showStudentTest.jsp">
		<jsp:param name="pageType" value="doTest"/>
		<jsp:param name="correctionType" value=""/>
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.feedback"/></html:submit></td>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='showSimulationCorrection';"><bean:message key="label.correction"/></html:submit></td>
	</html:form>
	<html:form action="/studentTestManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTests"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= request.getParameter("objectCode") %>"/>
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</html:form>
	</tr>
	</table>
	
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.test.no.available"/></h2>
</center>
</logic:notPresent>