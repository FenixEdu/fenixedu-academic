<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
	
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="infoDistributedTestMarks" name="component" property="infoDistributedTestMarks"/>
	<bean:define id="correctAnswersPercentage" name="component" property="correctAnswersPercentage"/>
	<bean:define id="wrongAnswersPercentage" name="component" property="wrongAnswersPercentage"/>
	<bean:define id="notAnsweredPercentage" name="component" property="notAnsweredPercentage"/>

	<center>
	<logic:empty name="infoDistributedTestMarks">
		<h2><bean:message key="message.testMark.no.Available"/></h2>
	</logic:empty>
	</center>
		
	<logic:notEmpty name="infoDistributedTestMarks" >
	<html:form action="/testDistribution">
	<html:hidden property="method" value="showDistributedTests"/>
	<html:hidden property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	

		<bean:define id="studentCode" value="0" type="java.lang.Object"/>
		<bean:define id="distributedTestCode" value="0" type="java.lang.Object"/>
		<logic:iterate id="studentTestQuestionList" name="infoDistributedTestMarks" type="DataBeans.InfoDistributedTestMarks" indexId="studentTestQuestionListIndex">
			<logic:iterate id="studentTestQuestion" name="studentTestQuestionList" property="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion" indexId="studentTestQuestionIndex">
				<logic:equal name="studentTestQuestionListIndex" value="0"><logic:equal name="studentTestQuestionIndex" value="0">
					<h2><bean:write name="studentTestQuestion" property="distributedTest.title"/></h2>
					<br/>
					<table>
					<tr><td class="infoop"><bean:message key="message.showTestMarks.information" /></td></tr>
					</table>
					<br/>
					<br/>
					<b>
					<bean:size id="infoDistributedTestMarksSize" name="infoDistributedTestMarks"/>
					<bean:message key="message.exist"/>&nbsp;
					<bean:write name="infoDistributedTestMarksSize"/>&nbsp;
					<bean:message key="message.studentsWithDistributedTest"/>
					</b>
					<br/>
					<br/>
					<bean:define id="distributedTestCode" name="studentTestQuestion" property="distributedTest.idInternal"/>
					<html:link page="<%= "/testsManagement.do?method=downloadTestMarks&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.exportToExel"/></html:link>
					<br/>
					<br/>
					<table>
					<tr>
						<td class="listClasses-header"><bean:message key="label.number"/></td>
						<td class="listClasses-header"><bean:message key="label.name"/></td>
						<bean:define id="questionNumber" name="studentTestQuestion" property="distributedTest.numberOfQuestions"/>
						<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){
							out.write(new String("<td class='listClasses-header'><b>P"+i+"</b></td>"));
						} %>
						<td class="listClasses-header"><bean:message key="label.mark"/></td>
					</tr>
				</logic:equal></logic:equal>
				<logic:equal name="studentTestQuestionIndex" value="0">
					<bean:define id="student" name="studentTestQuestion" property="student" type="DataBeans.InfoStudent"/>
					<bean:define id="studentCode" name="student" property="idInternal"/>
					<tr><td class="listClasses"><bean:write name="student" property="number"/></td>
					<td class="listClasses"><bean:write name="student" property="infoPerson.nome"/></td>
				</logic:equal>
				<bean:define id="mark" name="studentTestQuestion" property="testQuestionMark"/>
				<bean:define id="markValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(mark.toString())).toString()) %>"/>
				<td class="listClasses"><bean:write name="markValue"/></td>
			</logic:iterate>
			<bean:define id="finalMark" name="studentTestQuestionList" property="studentTestMark"/>
			<td class="listClasses"><bean:write name="finalMark"/></td>
			<td><html:link page="<%= "/testsManagement.do?method=showStudentTest&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showStudentTest"/></html:link></td>
			<td><html:link page="<%= "/testsManagement.do?method=showStudentTestLog&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showLog"/></html:link></td>
			</tr>
		</logic:iterate>
		</table><br/><br/>
		<table>
		<tr>
		<td class="listClasses-header"></td>
		<bean:define id="questionNumber" name="studentTestQuestion" property="distributedTest.numberOfQuestions"/>
		<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){
			out.write(new String("<td class='listClasses-header'><b>P"+i+"</b></td>"));
		} %>
		</tr>
		<tr>
		<logic:iterate id="correctAnswers" name="correctAnswersPercentage" indexId="correctAnswersIndex">
			<logic:equal name="correctAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.correct"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="correctAnswers"/></td>
		</logic:iterate>
		</tr>
		<tr>
		<logic:iterate id="wrongAnswers" name="wrongAnswersPercentage" indexId="wrongAnswersIndex">
			<logic:equal name="wrongAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.incorrect"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="wrongAnswers"/></td>
		</logic:iterate>
		</tr>
		<tr>
		<logic:iterate id="notAnswered" name="notAnsweredPercentage" indexId="notAnsweredIndex">
			<logic:equal name="notAnsweredIndex" value="0">
				<td class="listClasses"><bean:message key="label.notAswered"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="notAnswered"/></td>
		</logic:iterate>
		</tr>
	</table>

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
<logic:notPresent name="siteView">
<center>
	<h2><bean:message key="message.testMark.no.Available"/></h2>
</center>
</logic:notPresent>