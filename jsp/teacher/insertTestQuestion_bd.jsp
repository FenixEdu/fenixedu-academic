<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table>
	<tr>
		<td class="infoop"><bean:message key="message.showExercise.information" /></td>
	</tr>
</table>
<br/>
<h2><bean:message key="title.insertTestQuestionInformation"/></h2>
<br/>
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="iquestion" name="component" property="infoQuestion"/>
<bean:define id="metadata" name="iquestion" property="infoMetadata"/>
<bean:define id="exerciseCode" name="iquestion" property="idInternal"/>
<bean:define id="metadataCode" name="metadata" property="idInternal"/>

<span class="error"><html:errors/></span>

<html:form action="/questionsManagement">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="insertTestQuestion"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="metadataCode" value="<%= metadataCode.toString() %>"/>
<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
<table>
	<logic:notEqual name="metadata" property="description" value="">
		<tr>
			<td><b><bean:message key="label.description"/>:</b></td>
			<td><bean:write name="metadata" property="description"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="difficulty" value="">
		<tr>
			<td><b><bean:message key="label.test.difficulty"/>:</b></td>
			<td><bean:write name="metadata" property="difficulty"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="learningTime" value="">
		<tr>
			<td><b><bean:message key="label.test.learningTime"/>:</b></td>
			<td><bean:write name="metadata" property="learningTimeFormatted"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="level" value="">
		<tr>
			<td><b><bean:message key="label.exam.enrollment.year"/>:</b></td>
			<td><bean:write name="metadata" property="level"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="mainSubject" value="">
		<tr>
			<td><b><bean:message key="label.test.materiaPrincipal"/>:</b></td>
			<td><bean:write name="metadata" property="mainSubject"/></td>
		</tr>
	</logic:notEqual>	
	<logic:notEqual name="metadata" property="secondarySubject" value="">
		<tr>
			<td><b><bean:message key="label.test.materiaSecundaria"/>:</b></td>
			<td><bean:write name="metadata" property="secondarySubject"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="author" value="">
		<tr>
			<td><b><bean:message key="message.tests.author"/></b></td>
			<td><bean:write name="metadata" property="author"/></td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name="metadata" property="numberOfMembers" value="">
		<tr>
			<td><b><bean:message key="label.test.quantidadeExercicios"/></b></td>
			<td><bean:write name="metadata" property="numberOfMembers"/></td>
		</tr>
	</logic:notEqual>
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
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><html:submit styleClass="inputbutton"> <bean:message key="button.insert"/></html:submit></td>
		</html:form>
		<html:form action="/testsManagement">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="showAvailableQuestions"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
		<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
		<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
		<td>
			<html:submit styleClass="inputbutton"><bean:message key="link.goBack"/></html:submit>
		</td>
		
	<tr/>
</table>
<br/>
<br/>
<h2><bean:message key="title.example"/></h2>
<bean:define id="questionCode" name="iquestion" property="idInternal"/>
<bean:define id="index" value="0"/>
<bean:define id="imageLabel" value="false"/>
<table>
	<tr><td>
	<logic:iterate id="questionBody" name="iquestion" property="question">
				<bean:define id="questionLabel" name="questionBody" property="label"/>
				
				<% if (((String)questionLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>"/>
					
					<logic:equal name="imageLabel" value="true">
						</td><td>
					</logic:equal>
				<% } else if (((String)questionLabel).equals("image_label")){%>
									
					<logic:equal name="imageLabel" value="false">
						<bean:define id="imageLabel" value="true"/>
						<table><tr><td>
					</logic:equal>
				
					<bean:write name="questionBody" property="value"/>
					<br/>
				<% }else if (((String)questionLabel).equals("flow")){%>
					<logic:equal name="imageLabel" value="true">
						</td></tr></table>
						<bean:define id="imageLabel" value="false"/>
					</logic:equal>
					</td></tr>
					<tr><td>
				<% }else{%>
					<bean:write name="questionBody" property="value"/>
				<% } %>
				</logic:iterate>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
				</logic:equal>
	</td></tr>
</table>
<br/>
<bean:define id="indexOption" value="0"/>
<bean:define id="cardinality" name="iquestion" property="questionCardinality"/>
<table>
	<tr><td>
	<logic:iterate id="optionBody" name="iquestion" property="options">
					<bean:define id="optionLabel" name="optionBody" property="label"/>
					<% if (((String)optionLabel).startsWith("image/")){ %>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>"/>
					<% } else if (((String)optionLabel).equals("image_label")){%>
						<bean:write name="optionBody" property="value"/>
						<br/>
					<% } else if (((String)optionLabel).equals("response_label")){ %>				
						<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
						<%	if(cardinality.equals("Single")){ %>
							</td></tr><tr><td>
								<html:radio property="option" value="<%= indexOption.toString() %>"/>
							</td><td>
						<% }else if(cardinality.equals("Multiple")){ %>
							</td></tr><tr><td>
								<html:multibox property="option" value="<%= indexOption.toString() %>">
								</html:multibox>
							</td><td>
						<%}%>
					<% } else {%>
					<bean:write name="optionBody" property="value"/>
					<% } %>
						
				</logic:iterate>
	</td></tr>
</table>
</html:form>
</logic:present>