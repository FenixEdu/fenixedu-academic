<%@ page language="java" %>
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
	</tr>
</table>
<br/>
<br/>
<table><tr>
	<td><html:submit styleClass="inputbutton"> <bean:message key="label.change"/></html:submit></html:form></td>
	<td>
		<html:form action="/testEdition">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="editTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="link.goBack"/></html:submit>
	</td>
</tr></table>
<br/>
<br/>
<h2><bean:message key="title.example"/></h2>
<bean:define id="questionCode" name="infoQuestion" property="idInternal"/>
<bean:define id="index" value="0"/>
<bean:define id="imageLabel" value="false"/>
<table>
	<tr><td>
	<logic:iterate id="questionBody" name="infoQuestion" property="question">
				<bean:define id="questionLabel" name="questionBody" property="label"/>
				
				<% if (((String)questionLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciceCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>"/>
					
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
<bean:define id="cardinality" name="infoQuestion" property="questionCardinality"/>
<table>
	<tr><td>
	<logic:iterate id="optionBody" name="infoQuestion" property="options">
					<bean:define id="optionLabel" name="optionBody" property="label"/>
					<% if (((String)optionLabel).startsWith("image/")){ %>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciceCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>"/>
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