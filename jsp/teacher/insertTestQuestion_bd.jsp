<%@ page language="java" %>
<%@ page import="javax.swing.ImageIcon" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.insertTestQuestionInformation"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="iquestion" name="component" property="infoQuestion"/>
<bean:define id="metadata" name="iquestion" property="infoMetadata"/>
<bean:define id="exerciceCode" name="iquestion" property="idInternal"/>
<bean:define id="metadataCode" name="metadata" property="idInternal"/>

<span class="error"><html:errors/></span>

<html:form action="/questionsManagement">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="insertTestQuestion"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="metadataCode" value="<%= metadataCode.toString() %>"/>
<table>
	<logic:notEqual name="metadata" property="difficulty" value="">
		<tr>
			<td><b><bean:message key="message.tests.difficulty"/></b></td>
			<td><bean:write name="metadata" property="difficulty"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="level" value="">
		<tr>
			<td><b><bean:message key="message.tests.level"/></b></td>
			<td><bean:write name="metadata" property="level"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="mainSubject" value="">
		<tr>
			<td><b><bean:message key="message.tests.mainSubject"/></b></td>
			<td><bean:write name="metadata" property="mainSubject"/></td>
		</tr>
	</logic:notEqual>	
	<bean:size id="secondarySubjectSize" name="metadata" property="secondarySubject"/>
	<logic:notEqual name="secondarySubjectSize" value="0">
		<tr><td><b><bean:message key="message.tests.secondarySubject"/></b></td>
		<logic:iterate id="secondarySubject" name="metadata" property="secondarySubject">
			<td><bean:write name="secondarySubject"/></td>
		</tr>
		<tr><td></td>
		</logic:iterate>
	</logic:notEqual>
	<bean:size id="authorSize" name="metadata" property="author"/>
	<logic:notEqual name="authorSize" value="0">
		<tr><td><b><bean:message key="message.tests.author"/></b></td>
		<logic:iterate id="author" name="metadata" property="author">
			<td><bean:write name="author"/></td>
		</tr>
		<tr><td></td>
		</logic:iterate>
	</logic:notEqual>
	<tr>
		<bean:size id="quantidadeExercicios" name="metadata" property="members"/>
		<td><b><bean:message key="message.tests.quantidadeExercicios"/></b></td>
		<td><bean:write name="quantidadeExercicios"/></td>
	</tr>
	<tr>
	<td><b><bean:message key="message.tests.questionCardinality"/></b></td>
	<td><bean:write name="iquestion" property="questionCardinality"/></td>		
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><bean:message key="message.tests.questionValue"/></td>
		<td><html:text size="1" name="iquestion" property="questionValue"/></td>
	</tr><tr>
		<td>
		<bean:message key="message.testOrder"/>
		</td>
		<td>
		<html:select property="questionOrder">
		<html:option value="-1"><bean:message key="label.end"/></html:option>
		<html:options name="testQuestionValues" labelName="testQuestionNames"/>
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
<html:link page="<%= "/testsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
<bean:message key="link.goBack" />
</html:link></div>
<br/>
<br/>
<h2><bean:message key="title.insertTestQuestionExercice"/></h2>
<%int index=0; String imgType=new String();%>
<table>
	<tr><td>
	<logic:iterate id="questionBody" name="iquestion" property="question">
	<% if (((String)questionBody).startsWith("Content-Type:")){
	index++;
	imgType = ((String)questionBody).substring(new String("Content-Type: ").length());
	%>
	<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" + exerciceCode +"&amp;imgCode="+index +"&amp;imgType="+imgType%>"/>
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
<%int indexOption=0;%>
<bean:define id="cardinality" name="iquestion" property="questionCardinality"/>
<table>
	<tr><td>
	<logic:iterate id="optionBody" name="iquestion" property="options">
	<% if (((String)optionBody).startsWith("Content-Type:")){
		index++;
		imgType = ((String)optionBody).substring(new String("Content-Type: ").length());
	%>
	<html:img align="middle" src="<%= "/ciapl/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" + exerciceCode+"&amp;imgCode="+index +"&amp;imgType="+imgType%>"/>
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
		<%}%>
	<% }else{%>
	<bean:write name="optionBody"/>
	<% } %>
	</logic:iterate>
	</td></tr>
</table>
</html:form>
</logic:present>