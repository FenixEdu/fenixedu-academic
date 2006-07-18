<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<%if (pageContext.findAttribute("exerciseCode")!=null){%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" value="<%=(pageContext.findAttribute("exerciseCode")).toString()%>"/>
<%}%>
<span class="error"><html:errors/></span>
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
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td>
</html:form>
</tr>
</table>
