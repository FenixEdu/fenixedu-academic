<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDistributedTests"/>

	<bean:size id="questionNumber" name="component" property="answeredPercentage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	<table>
		<tr>
		<td class="listClasses-header"></td>
		<% for(int i=1; i<=new Integer(questionNumber.toString()).intValue();i++ ){
			out.write(new String("<th class='listClasses-header'><b>P"+i+"</b></th>"));
		} %>
		</tr>
		<tr>
		<logic:iterate id="answered" name="component" property="answeredPercentage" indexId="answeredIndex">
			<logic:equal name="answeredIndex" value="0">
				<td class="listClasses"><bean:message key="label.aswered"/></td>
			</logic:equal>
			<td class="listClasses"><bean:write name="answered"/></td>
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
		<tr></tr>
		<logic:notEmpty name="component" property="correctAnswersPercentage">
		<tr>
		<logic:iterate id="correctAnswers" name="component" property="correctAnswersPercentage" indexId="correctAnswersIndex">
			<logic:equal name="correctAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.correctAnswers"/>*</td>
			</logic:equal>
			<td class="listClasses"><bean:write name="correctAnswers"/></td>
		</logic:iterate>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="component" property="partiallyCorrectAnswersPercentage">
		<tr>
		<logic:iterate id="partiallyCorrect" name="component" property="partiallyCorrectAnswersPercentage" indexId="partiallyCorrectIndex">
			<logic:equal name="partiallyCorrectIndex" value="0">
				<td class="listClasses"><bean:message key="label.partiallyCorrect"/>**</td>
			</logic:equal>
			<td class="listClasses"><bean:write name="partiallyCorrect"/></td>
		</logic:iterate>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="component" property="wrongAnswersPercentage">
		<tr>
		<logic:iterate id="wrongAnswers" name="component" property="wrongAnswersPercentage" indexId="wrongAnswersIndex">
			<logic:equal name="wrongAnswersIndex" value="0">
				<td class="listClasses"><bean:message key="label.incorrect"/>*</td>
			</logic:equal>
			<td class="listClasses"><bean:write name="wrongAnswers"/></td>
		</logic:iterate>
		</tr>
		</logic:notEmpty>
	</table>
	<logic:notEmpty name="component" property="correctAnswersPercentage">
		<h6>*<bean:message key="message.feedbackScope"/><h6>
	</logic:notEmpty>
	<logic:empty name="component" property="correctAnswersPercentage">
			<logic:notEmpty name="component" property="wrongAnswersPercentage">
				<h6>*<bean:message key="message.feedbackScope"/><h6>
			</logic:notEmpty>
	</logic:empty>
	<logic:notEmpty name="component" property="partiallyCorrectAnswersPercentage">
		<h6>**<bean:message key="message.feedbackScope"/>&nbsp;<bean:message key="message.partiallyCorrectScope"/><h6>
	</logic:notEmpty>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	
</logic:present>
<logic:notPresent name="siteView">
<center>
	<h2><bean:message key="message.testMark.no.Available"/></h2>
</center>
</logic:notPresent>