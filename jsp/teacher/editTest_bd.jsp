<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.editTest"/></h2>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="infoTest" name="component" property="infoTest"/>
<bean:define id="infoTestQuestionList" name="component" property="infoTestQuestions"/>

<html:form action="/testEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editTest"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<table>
	<tr>
		<td><b><bean:message key="label.title"/></b></td>
		<td><bean:write name="infoTest" property="title"/></td>
	</tr>
	<logic:notEqual name="infoTest" property="information" value="">
		<tr>
			<td><b><bean:message key="label.information"/></b></td>
			<td><bean:write name="infoTest" property="information"/></td>
		</tr>
	</logic:notEqual>
</table>
<br/>
<table>
	<tr>
		<td><div class="gen-button">
		<html:link page="<%= "/testEdition.do?method=prepareEditTestHeader&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="link.editTestHeader" />
		</html:link></div>
		&nbsp;
		<div class="gen-button">
		<html:link page="<%= "/testsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="label.test.insertQuestion"/>
		</html:link></div>
		&nbsp;
		<div class="gen-button">
		<html:link page="<%= "/testsManagement.do?method=showTests&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="link.showTests"/>
		</html:link></div></td>
	</tr>
</table>
<br/>
<table>
	<tr><td><hr></td></tr>
	<logic:iterate id="testQuestion" name="infoTestQuestionList" type="DataBeans.InfoTestQuestion">
	<tr>
		<td><b><bean:message key="message.tests.question" /></b>&nbsp;<bean:write name="testQuestion" property="testQuestionOrder"/></td></tr>
		<tr><td><b><bean:message key="message.tests.questionValue" /></b>&nbsp;<bean:write name="testQuestion" property="testQuestionValue"/></td></tr>
		<bean:define id="thisQuestion" name="testQuestion" property="question" type="DataBeans.InfoQuestion"/>
		<bean:define id="questionCode" name="thisQuestion" property="idInternal"/>
		<tr><td><table><tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testQuestionEdition.do?method=prepareEditTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="link.editTestQuestion" />
			</html:link></div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testEdition.do?method=deleteTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="link.removeTestQuestion" />
			</html:link></div></td>
		</tr></table><tr><td>
			<br/><td>
				<%int index=0; String imgType=new String();%>
				<logic:iterate id="questionBody" name="thisQuestion" property="question">
				<% if (((String)questionBody).startsWith("Content-Type: ")){
					index++;
					imgType = ((String)questionBody).substring(new String("Content-Type: ").length());
				%>
				<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" + questionCode+"&amp;imgCode="+index +"&amp;imgType="+imgType%>"/>
				<% }else if (((String)questionBody).equals("<flow>")){%>
					</td></tr>
					<tr><td>
				<% }else{%>
					<bean:write name="questionBody"/>
				<% } %>
				</logic:iterate>
			</td></tr><tr><td>
			<bean:define id="cardinality" name="thisQuestion" property="questionCardinality"/>
			<table><td>
				<%int indexOption=0;%>
				<logic:iterate id="optionBody" name="thisQuestion" property="options">
				<% if (((String)optionBody).startsWith("Content-Type:")){
					index++;
					imgType = ((String)optionBody).substring(new String("Content-Type: ").length());
				%>
				<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" + questionCode+"&amp;imgCode="+index +"&amp;imgType="+imgType%>"/>
				<% }else if (((String)optionBody).equals("<response_label>")){ 
					indexOption++;
					if(cardinality.equals("Single")){
					%>
					</td></tr><tr><td>
					<html:radio property="option" value="<%= (new Integer(indexOption)).toString() %>"/>
					</td><td>
					<% }else if(cardinality.equals("Multiple")){ %>
					</td></tr><tr><td>
					<html:multibox property="option" value="<%= (new Integer(indexOption)).toString() %>">
					</html:multibox>
					</td><td>
					<% }else if(cardinality.equals("Multiple_old")){ %>
					</td></tr><tr><td>
					<html:multibox property="option" value="<%= (new Integer(indexOption)).toString() %>">
					</html:multibox>
					</td><td>
					<%}%>
				<% }else{%>
				<bean:write name="optionBody"/>
				<% } %>
				</logic:iterate>
			</td></table></td>	
	</tr>
	<tr><td><hr></td></tr>
	</logic:iterate>
</table>
<br/>
<bean:size id="infoTestQuestionListSize" name="component" property="infoTestQuestions"/>
<logic:notEqual name="infoTestQuestionListSize" value="0">
	<table>
		<tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="label.test.insertQuestion" />
			</html:link></div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showTests&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="link.showTests" />
			</html:link></div></td>
		</tr>
	</table>
</logic:notEqual>
</html:form>
