<%@ page language="java" %>
<%@ page import="javax.swing.ImageIcon" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.editTestQuestion"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="testQuestion" name="component" property="infoTestQuestion"/>
<bean:define id="infoQuestion" name="component" property="infoQuestion"/>
<bean:define id="exerciceCode" name="infoQuestion" property="idInternal"/>
<bean:define id="testQuestionCode" name="testQuestion" property="idInternal"/>
<span class="error"><html:errors/></span>

<html:form action="/testQuestionEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editTestQuestion"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="testQuestionCode" value="<%= testQuestionCode.toString() %>"/>
<html:hidden property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
<table>
	<tr>
	<td><b><bean:message key="message.tests.questionValue"/></b></td>
	<td><html:text size="1" name="testQuestion" property="testQuestionValue"/></td>
	</tr><tr>
		<td>
		<bean:message key="message.testOrder"/>
		</td>
		<td>
		<html:select property="testQuestionOrder">
		<html:option value="-2"><bean:message key="label.noMofify"/></html:option>
		<html:options name="testQuestionValues" labelName="testQuestionNames"/>
		<html:option value="-1"><bean:message key="label.end"/></html:option>
		</html:select>
		</td>
		<td>
		<html:submit styleClass="inputbutton"> <bean:message key="button.insert"/></html:submit>
		</td>
	</tr>
</table>
<br/>
<br/>
<div class="gen-button">
<html:link page="<%= "/testEdition.do?method=editTest&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
<bean:message key="link.goBack" />
</html:link></div>
<br/>
<br/>
<h2><bean:message key="title.insertTestQuestionExercice"/></h2>
<%int index=0; String imgType=new String();%>
<table>
	<tr><td>
	<logic:iterate id="questionBody" name="infoQuestion" property="question">
	<% if (((String)questionBody).startsWith("Content-Type:")){
	index++;
	imgType = ((String)questionBody).substring(new String("Content-Type: ").length());
	%>
	<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" +exerciceCode+"&amp;imgCode="+index+"&amp;imgType="+imgType%>"/>
	<% }else if (((String)questionBody).equals("<flow>")){%>
	</td></tr>
	<tr><td>
	<% }else{%>
	<bean:write name="questionBody"/>
	<% } %>
	</logic:iterate>
	</td></tr>
</table>
<br/>
<bean:define id="cardinality" name="infoQuestion" property="questionCardinality"/>
<%int indexOption=0;%>
<table>
	<tr><td>
	<logic:iterate id="optionBody" name="infoQuestion" property="options">
	<% if (((String)optionBody).startsWith("Content-Type:")){
		index++;
		imgType = ((String)optionBody).substring(new String("Content-Type: ").length());
	%>
	<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" +exerciceCode+ "&amp;imgCode="+index+"&amp;imgType="+imgType%>"/>
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
		<% } %>
	<% }else{%>
	<bean:write name="optionBody"/>
	<% } %>
	</logic:iterate>
	</td></tr>
</table>
</html:form>
</logic:present>