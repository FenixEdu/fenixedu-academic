<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<logic:present name="siteView">	
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="distributedTest" name="component" property="infoDistributedTest"/>
	<h2><bean:write name="distributedTest" property="title"/></h2>
	<br/>
	<table>
		<tr><td class="infoop"><bean:message key="message.showTestMarksStatistics.information" /></td></tr>
	</table>
	<br/>
	<br/>
	
	<html:form action="/testDistribution">
	<html:hidden property="method" value="showDistributedTests"/>

	<bean:size id="questionNumber" name="component" property="correctAnswersPercentage"/>
	<html:hidden property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	<table>
		<tr>
		<td class="listClasses-header"></td>
		<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){
			out.write(new String("<td class='listClasses-header'><b>P"+i+"</b></td>"));
		} %>
		</tr>
		<tr>
		<logic:iterate id="correctAnswers" name="component" property="correctAnswersPercentage" indexId="correctAnswersIndex">
			<logic:equal name="correctAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.correct"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="correctAnswers"/></td>
		</logic:iterate>
		</tr>
		<tr>
		<logic:iterate id="wrongAnswers" name="component" property="wrongAnswersPercentage" indexId="wrongAnswersIndex">
			<logic:equal name="wrongAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.incorrect"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="wrongAnswers"/></td>
		</logic:iterate>
		</tr>
		<tr>
		<logic:iterate id="notAnswered" name="component" property="notAnsweredPercentage" indexId="notAnsweredIndex">
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