

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
%>

<logic:present name="infoStudentTestQuestionList">
	<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="DataBeans.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal"/>
	
	<html:form action="/studentTests">
	<html:hidden property="method" value="doTest"/>
	
	<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
		<br/><br/>
		<b><bean:write name="date"/></b>
	</center>
	<br/>
	<br/>
	<table width="100%" border="0" cellpadding="0" cellspacing="10">
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion">
			<tr><td><hr></td></tr>
			<bean:define id="question" name="testQuestion" property="question" type="DataBeans.InfoQuestion"/>
			<bean:define id="questionCode" name="question" property="idInternal"/>
			<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
			<tr>
				<td><b><bean:message key="label.test.questionOrder" />:</b>&nbsp;<bean:write name="questionOrder"/></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.questionValue" />:</b>&nbsp;<bean:write name="testQuestion" property="testQuestionValue"/></td>
			</tr>
			<tr>
				<td>
					<bean:define id="index" value="0"/>
				<bean:define id="imageLabel" value="false"/>
				<logic:iterate id="questionBody" name="question" property="question">
				<bean:define id="questionLabel" name="questionBody" property="label"/>
				
				<% if (((String)questionLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>"/>
					
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
				</td>
			</tr><tr>
				<td>
					<bean:define id="cardinality" name="question" property="questionCardinality"/>
			<table><td>
				<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
				<bean:define id="indexOption" value="0"/>
				<logic:iterate id="optionBody" name="question" property="options">
					<bean:define id="optionLabel" name="optionBody" property="label"/>
					<% if (((String)optionLabel).startsWith("image/")){ %>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>"/>
					<% } else if (((String)optionLabel).equals("image_label")){%>
						<bean:write name="optionBody" property="value"/>
						<br/>
					<% } else if (((String)optionLabel).equals("response_label")){ %>				
						<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
						<%	if(cardinality.equals("Single")){ %>
							</td></tr><tr><td>
							<bean:define id="testType" name="distributedTest" property="testType.type"/>
							<bean:define id="responsed" name="testQuestion" property="response"/>
							
							<%if(((Integer)testType).intValue()==1 && ((Integer)responsed).intValue()!=0){%>
								<html:radio property='<%="option["+ optionOrder+"]"%>' value="<%= indexOption.toString() %>" disabled="true"/>
							<%}else{%>
								<html:radio property='<%="option["+ optionOrder+"]"%>' value="<%= indexOption.toString() %>"/>
							<%}%>
							</td><td>
						<% }else if(cardinality.equals("Multiple")){ %>
							</td></tr><tr><td>
								<html:multibox property='<%="option["+ optionOrder+"]"%>' value="<%= indexOption.toString() %>">
								
								</html:multibox>
							</td><td>
						<%}%>
					<% } else {%>
					<bean:write name="optionBody" property="value"/>
					<% } %>
						
				</logic:iterate>
				</td></tr></table></td>	
			</tr>
		</logic:iterate>
		<tr><td><hr></td></tr>
	</table>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton" property="submit"><bean:message key="button.submitTest"/></html:submit></td>
		<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
		<html:form action="/studentTests">
		<html:hidden property="method" value="testsFirstPage"/>
		<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
		<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
		<td><html:submit styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit></td></html:form>
	</tr>
	</table>	
	</logic:notEmpty>
</logic:present>
<center>
<logic:notPresent name="infoStudentTestQuestionList">
<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:notPresent>
</center>