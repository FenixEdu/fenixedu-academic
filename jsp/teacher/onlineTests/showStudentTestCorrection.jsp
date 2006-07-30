<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:present name="infoStudentTestQuestionList">
	<center><logic:empty name="infoStudentTestQuestionList">
		<h2><bean:message key="message.test.no.available" /></h2>
	</logic:empty> <logic:notEmpty name="infoStudentTestQuestionList">

		<logic:present name="successfulChanged">
			<span class="error"><!-- Error messages go here --><bean:message key="message.successfulChanged" /></span>
			<br />
			<table>
				<logic:iterate id="changed" name="successfulChanged">
					<logic:iterate id="student" name="changed" property="infoStudentList">
						<tr>
							<tr>
								<td><strong><bean:write name="changed" property="infoDistributedTest.title" /></strong></td>
								<td><bean:write name="student" property="number" /></td>
							</tr>
					</logic:iterate>
				</logic:iterate>
			</table>
		</logic:present>
		<logic:present name="insuccessfulAdvisoryDistribution">
			<span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistributionForAll" /></span>
		</logic:present>
		<logic:present name="studentWithoutAdvisory">
			<bean:size id="infoStudentListSize" name="studentWithoutAdvisory" />
			<logic:notEqual name="infoStudentListSize" value="0">
				<table>
					<tr>
						<td><span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistribution" /></span></td>
					</tr>
					<logic:iterate id="student" name="studentWithoutAdvisory">
						<tr>
							<td><span class="error"><!-- Error messages go here --><bean:write name="student" property="number" /></span></td>
						</tr>
					</logic:iterate>
				</table>
			</logic:notEqual>
		</logic:present>

		<html:form action="/studentTestManagement">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTestMarks" />

			<logic:iterate id="testQuestion" name="infoStudentTestQuestionList"
				type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion" />
			<bean:define id="distributedTest" name="testQuestion" property="distributedTest"
				type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest" />
			<bean:define id="testCode" name="distributedTest" property="idInternal" />

			<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= testCode.toString() %>" />

			<h2><bean:write name="distributedTest" property="title" /></h2>
			<b><bean:write name="distributedTest" property="testInformation" /></b></center>
	<br />
	<br />
	<bean:define id="testType" name="distributedTest" property="testType.type" />
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
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
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