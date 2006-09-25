<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<logic:present name="infoSiteStudentsTestMarks">
	
	<bean:define id="distributedTest" name="infoSiteStudentsTestMarks" property="infoDistributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="infoStudentTestQuestionList" name="infoSiteStudentsTestMarks" property="infoStudentTestQuestionList"/>
	
	<center>
	<logic:empty name="infoStudentTestQuestionList">
		<h2><bean:message key="message.testMark.no.Available"/></h2>
	</logic:empty>
	</center>
		
	<logic:notEmpty name="infoStudentTestQuestionList">
	<html:form action="/testDistribution">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDistributedTests"/>
	<bean:define id="studentCode" value="0" type="java.lang.Object"/>
	<bean:define id="studentIndex" value="0"/>
	<bean:define id="finalMark" value="0"/>
	<bean:define id="questionNumber" name="distributedTest" property="numberOfQuestions"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>

		<bean:size id="numberOfStudentTestQuestion" name="infoStudentTestQuestionList"/>
		<bean:define id="numberOfStudents" value="<%=numberOfStudentTestQuestion.toString()%>"/>
					
		<logic:iterate id="studentTestQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionMark" indexId="index">
			<bean:define id="maximumMark" name="studentTestQuestion" property="maximumMark"/>
			<logic:equal name="index" value="0">
				<h2><bean:write name="distributedTest" property="title"/></h2>
				<br/>
				<table>
					<tr><td class="infoop"><bean:message key="message.showTestMarks.information" /></td></tr>
				</table>
				<br/>
				<br/>
				<b>
					<bean:message key="message.exist"/>&nbsp;
					<bean:write name="numberOfStudents"/>&nbsp;
					<bean:message key="message.studentsWithDistributedTest"/>
				</b>
				<br/>
				<br/>
				<bean:define id="distributedTestCode" name="distributedTest" property="idInternal"/>
				<html:link page="<%= "/testsManagement.do?method=downloadTestMarks&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.exportToExcel"/></html:link>
				<br/>
				<br/>
				<table>
					<tr>
						<th class="listClasses-header"><bean:message key="label.number"/></th>
						<th class="listClasses-header"><bean:message key="label.name"/></th>
						<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){
							out.write(new String("<th class='listClasses-header'><b>P"+i+"</b></th>"));
						} %>
						<th class="listClasses-header"><bean:message key="label.mark"/></th>
						<logic:greaterThan name="maximumMark" value="0">
							<th class="listClasses-header"><bean:message key="label.mark"/><bean:message key="label.percentage"/></th>
						</logic:greaterThan>
					</tr>
			</logic:equal>
			<% if (new Integer(studentIndex).equals(new Integer(0))) {%>
				<bean:define id="studentCode" name="studentTestQuestion" property="studentIdInternal"/>
				<tr>
				<td class="listClasses"><bean:write name="studentTestQuestion" property="studentNumber"/></td>
				<td class="listClasses"><bean:write name="studentTestQuestion" property="studentName"/></td>
			<% } %>
			
			<bean:define id="finalMark" value="0"/>
			<bean:define id="finalMarkValue" value="0"/>
			<logic:iterate id="mark" name="studentTestQuestion" property="testQuestionMarks">
				<bean:define id="markValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(mark.toString())).toString()) %>"/>
				<bean:define id="finalMark" value="<%=new Double((new Double(finalMark)).doubleValue()+(new Double(mark.toString())).doubleValue()).toString()%>"/>
				<td class="listClasses"><bean:write name="markValue"/></td>
				<bean:define id="finalMarkValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(finalMark.toString())).toString()) %>"/>
			</logic:iterate>
			
			<logic:lessEqual name="finalMarkValue" value="0">
				<td class="listClasses">0</td>
			</logic:lessEqual>
			<logic:greaterThan name="finalMarkValue" value="0">
				<td class="listClasses">2<bean:write name="finalMarkValue"/></td>
			</logic:greaterThan>
				
				<logic:greaterThan name="maximumMark" value="0">
					<bean:define id="finalMarkPercentage" value="<%= (new java.text.DecimalFormat("#%").format(Double.parseDouble(finalMark.toString())*java.lang.Math.pow(Double.parseDouble(maximumMark.toString()), -1)).toString()) %>"/>
					<logic:lessEqual name="finalMarkPercentage" value="0%">
						<td class="listClasses">0%</td>
					</logic:lessEqual>
					<logic:greaterThan name="finalMarkPercentage" value="0%">
						<td class="listClasses"><bean:write name="finalMarkPercentage"/></td>
					</logic:greaterThan>
				</logic:greaterThan>
				
				<td><html:link page="<%= "/studentTestManagement.do?method=showStudentTest&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showStudentTest"/></html:link></td>
				<td><html:link page="<%= "/testsManagement.do?method=showStudentTestLog&amp;studentCode=" +studentCode+ "&amp;distributedTestCode=" +pageContext.findAttribute("distributedTestCode")+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>"><bean:message key="link.showLog"/></html:link></td>
				</tr>
		</logic:iterate>
		</table>
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
<logic:notPresent name="infoSiteStudentsTestMarks">
<center>
	<h2><bean:message key="message.testMark.no.Available"/></h2>
</center>
</logic:notPresent>