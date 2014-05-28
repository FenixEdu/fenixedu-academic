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

<script language="Javascript" type="text/javascript">
<!--

function selectQuestionType(){
	var questionType = document.forms[0].questionType;
	var cardinalityType = document.forms[0].cardinalityType;
	var disable=false;
	
	if(questionType[0].checked==false) 
		disable=true;
	for (var i=0; i<document.forms[0].cardinalityType.length; i++){
		var cardinalityType = document.forms[0].cardinalityType[i];
		if(disable == true) cardinalityType.disabled=true; else cardinalityType.disabled=false;
	}
}
// -->
</script>
<h2><bean:message key="title.exerciseType"/></h2>
<br/>

<html:form action="/exerciseType">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseQuestionType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<%if (pageContext.findAttribute("exerciseCode")!=null){%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" value="<%=(pageContext.findAttribute("exerciseCode")).toString()%>"/>
<%}%>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<table>
	<tr><td class="infoop"><bean:message key="message.chooseQuestionType.information" /></td></tr>
</table>
<table>
<logic:iterate id="questionType" name="questionsTypes" type="org.apache.struts.util.LabelValueBean" indexId="index">
	<tr>
		<td><bean:write name="questionType" property="label"/></td>
		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.questionType" property="questionType"  value="<%=questionType.getValue()%>" onclick="selectQuestionType()"/></td>
	</tr>
	<logic:equal name="index" value="0">

		<logic:iterate id="cardinalityType" name="cardinalityTypes" type="org.apache.struts.util.LabelValueBean">
			<tr><td/><td/>
			<td><bean:write name="cardinalityType" property="label"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.cardinalityType" property="cardinalityType" value="<%=cardinalityType.getValue()%>"/></td>
			</tr>
		</logic:iterate>
	</logic:equal>
</logic:iterate>
</table>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.chooseQuestionScope.information" /></td></tr>
</table>
<table>
	<tr>
		<td><bean:message key="label.evaluationQuestion"/></td>
		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.evaluationQuestion" property="evaluationQuestion" value="true"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.inquiryQuestion"/></td>
		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.evaluationQuestion" property="evaluationQuestion" value="false"/></td>
	</tr>
</table>
<br/>
<br/>
<table>
<tr>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit></td>
</html:form>
<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td>
</html:form>
</tr>
</table>
