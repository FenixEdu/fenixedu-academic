<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoStudentsTestQuestionsList">
	<center>
	<logic:empty name="infoStudentsTestQuestionsList">
		<h2><bean:message key="message.testMark.no.Available"/></h2>
	</logic:empty>
	</center>
		
	<logic:notEmpty name="infoStudentsTestQuestionsList" >
	<html:form action="/testDistribution">
	<html:hidden property="method" value="showDistributedTests"/>
	<html:hidden property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	<br/>
	<br/>
	
		<logic:iterate id="studentList" name="infoStudentsTestQuestionsList" type="java.util.List" indexId="studentListIndex">
			<bean:define id="mark" value="0"/>
			<logic:iterate id="studentTestQuestion" name="studentList" type="DataBeans.InfoStudentTestQuestion" indexId="studentTestQuestionIndex">
				<logic:equal name="studentTestQuestionIndex" value="0">
					<bean:define id="distributedTest" name="studentTestQuestion" property="distributedTest" type="DataBeans.InfoDistributedTest"/>
					<logic:equal name="studentListIndex" value="0">
						<h2><bean:write name="distributedTest" property="title"/></h2>
						<table><tr>
							<td class="listClasses-header"><bean:message key="label.number"/></td>		
							<bean:define id="questionNumber" name="distributedTest" property="numberOfQuestions"/>
							<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){ 
							out.write(new String("<td class='listClasses-header'><b>P"+i+"</b></td>"));				
							} %>
							<td class="listClasses-header"><bean:message key="label.mark"/></td>
						</tr>
					</logic:equal>
					<tr>
					<td class="listClasses">
						<bean:define id="student" name="studentTestQuestion" property="student" type="DataBeans.InfoStudent"/>
						<bean:write name="student" property="number"/>
					</td>
				</logic:equal>
				<td class="listClasses">
				
				<bean:define id="question" name="studentTestQuestion" property="question" type="DataBeans.InfoQuestion"/>
				<bean:define id="responsed" name="studentTestQuestion" property="response"/>
				<bean:define id="correct" value="false"/>
				<bean:define id="questionValue" name="studentTestQuestion" property="testQuestionValue"/>
				<bean:define id="optionNumber" name="question" property="optionNumber"/>

				<logic:iterate id="correctResponse" name="question" property="correctResponse">
					<logic:equal name="correctResponse" value="<%= responsed.toString() %>">
						<bean:define id="correct" value="true"/>
					</logic:equal>
				</logic:iterate>
				<logic:equal name="correct" value="true">
					<bean:write name="questionValue"/>
					<bean:define id="mark" value="<%= (new Double(Double.parseDouble(mark.toString())+ Double.parseDouble(questionValue.toString()))).toString() %>"/>
				</logic:equal>
				<logic:notEqual name="correct" value="true">
					<logic:notEqual name="responsed" value="0">
						<bean:define id="value" value="<%= (new Double(-((Integer.parseInt(questionValue.toString()))*java.lang.Math.pow((Integer.parseInt(optionNumber.toString())-1), -1)))).toString() %>"/>
						<bean:define id="formattedValue" value="<%= (new java.text.DecimalFormat("#0.##").format(new Double(value.toString()))).toString() %>"/>
						<bean:write name="formattedValue"/>
						<bean:define id="mark" value="<%= (new Double(Double.parseDouble(mark.toString())+ Double.parseDouble(value.toString()))).toString() %>"/>
					</logic:notEqual>
					<logic:equal name="responsed" value="0">
						<bean:write name="responsed"/>
					</logic:equal>
				</logic:notEqual>
				</td>
			</logic:iterate>
			<% if (new Double(mark).doubleValue() < 0) { %>
				<bean:define id="mark" value="0"/>
			<% } %>
			<bean:define id="formattedMark" value="<%= (new java.text.DecimalFormat("#0.##").format(new Double(mark.toString()))).toString() %>"/>
			<td class="listClasses"><bean:write name="formattedMark"/></td>
			
		</tr>
		</logic:iterate>
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
<logic:notPresent name="infoStudentsTestQuestionsList">
<center>
	<h2><bean:message key="message.testMark.no.Available"/></h2>
</center>
</logic:notPresent>