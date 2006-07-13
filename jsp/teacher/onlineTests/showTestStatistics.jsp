<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoInquiryStatistics" name="component" property="infoInquiryStatistics"/>

<html:form action="/testDistribution">
<html:hidden property="method" value="showDistributedTests"/>
<html:hidden property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	
	<logic:iterate id="infoInquiryStatisticsList" name="infoInquiryStatistics" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics"/>
	<bean:define id="testQuestion" name ="infoInquiryStatisticsList" property="infoStudentTestQuestion" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>


	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
	<bean:define id="student" name="testQuestion" property="student" type="net.sourceforge.fenixedu.dataTransferObject.InfoStudent"/>
	<bean:define id="person" name="student" property="infoPerson" type="net.sourceforge.fenixedu.dataTransferObject.InfoPerson"/>
	<bean:define id="studentCode" name="person" property="username"/>
	<bean:define id="studentId" name="student" property="idInternal"/>
	
	<center>
	<h2><bean:write name="distributedTest" property="title"/></h2>
	<b><bean:write name="distributedTest" property="testInformation"/></b>	
	</center>
	<br/>
	<br/>

	<table width="100%" border="0" cellpadding="0" cellspacing="10">
	<logic:iterate id="infoInquiryStatisticsList" name="infoInquiryStatistics" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics">
		<bean:define id="testQuestion" name ="infoInquiryStatisticsList" property="infoStudentTestQuestion" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
		<bean:define id="index" value="0"/>
		<tr><td><hr></td></tr>
		<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion"/>
		<bean:define id="questionCode" name="question" property="idInternal"/>
		<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
		<bean:define id="questionType" name="question" property="questionType.type"/>
		<tr><td><b><bean:message key="message.tests.question" />:</b>&nbsp;<bean:write name="questionOrder"/></td></tr>
		<tr><td>
			<logic:iterate id="questionBody" name="question" property="question"/>
		</td></tr>
		<tr>
				<td>
					<bean:define id="index" value="0"/>
				<bean:define id="imageLabel" value="false"/>
				<logic:iterate id="questionBody" name="question" property="question">
				<bean:define id="questionLabel" name="questionBody" property="label"/>
				
				<% if (((String)questionLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>" altKey="questionLabel" bundle="IMAGE_RESOURCES" />
					
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
					<bean:define id="cardinality" name="question" property="questionType.cardinalityType.type"/>
			<table><td>
				<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
				<bean:define id="indexOption" value="0"/>
				<bean:define id="correct" value="false"/>
				<logic:iterate id="questionOption" name="question" property="options">
				<logic:iterate id="optionBody" name="questionOption" property="optionContent">
					<bean:define id="optionLabel" name="optionBody" property="label"/>
					<% if (((String)optionLabel).startsWith("image/")){ %>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>" altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
					<% } else if (((String)optionLabel).equals("image_label")){%>
						<bean:write name="optionBody" property="value"/>
						<br/>
					<% } else if (((String)optionLabel).equals("response_label")){ %>				
						<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
						
						<%	if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
							<%	if(((Integer)cardinality).intValue()==1 ){ %>  <%--CardinalityType.SINGLE--%>
							</td></tr><tr><td>
							<logic:iterate id="statistic" name="infoInquiryStatisticsList" property="optionStatistics" offset="<%=new Integer(new Integer(indexOption).intValue()-1).toString()%>" length="1"/>
							<b><bean:write name="statistic" property="value"/>)</b>
							</td><td>
						<% }else if(((Integer)cardinality).intValue()==2){ %><%--CardinalityType.MULTIPLE--%>
							</td></tr><tr><td>
							<logic:iterate id="statistic" name="infoInquiryStatisticsList" property="optionStatistics" offset="<%=new Integer(new Integer(indexOption).intValue()-1).toString()%>" length="1"/>
							<b><bean:write name="statistic" property="value"/>)</b>
							</td><td>
						<%}
						}else{%> <%--QuestionType.STR ou QuestionType.NUM--%>
							<html:text property="selected" value="" disabled="true"/>
					<%  }
					 } else {%>
					<bean:write name="optionBody" property="value"/>
					<% } %>
				</logic:iterate>
				</logic:iterate>
				</td></tr></table></td>	
			</tr>
			<tr><td><table>
			<bean:size id="size" name="infoInquiryStatisticsList" property="optionStatistics"/>
			<%if(((Integer)questionType).intValue()<=1 ){ %> 
			<logic:iterate id="statistic" name="infoInquiryStatisticsList" property="optionStatistics" offset="<%=new Integer(size.intValue()-1).toString()%>" length="1">
			<tr>
			<td><b><bean:write name="statistic" property="label"/>:</b>
			<bean:write name="statistic" property="value"/></td>
			</tr>
			</logic:iterate>
			<%}else{ %>
				<logic:iterate id="statistic" name="infoInquiryStatisticsList" property="optionStatistics"indexId="i">
				<tr>
				<td><b><bean:write name="statistic" property="label"/>
				<logic:equal name="i" value="<%=new Integer(size.intValue()-1).toString()%>">:</logic:equal>
				<logic:notEqual name="i" value="<%=new Integer(size.intValue()-1).toString()%>">)</logic:notEqual>
				</b>
				<bean:write name="statistic" property="value"/></td>
				</tr>
			</logic:iterate>
			<%} %> 
			</table></td></tr>
	</logic:iterate>
	<tr><td><hr></td></tr>
	</table>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
</logic:present>
<logic:notPresent name="siteView">
<center>
	<h2><bean:message key="message.testMark.no.Available"/></h2>
</center>
</logic:notPresent>