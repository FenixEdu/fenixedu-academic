<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoStudentTestLogList">
<html:form action="/testsManagement">
<html:hidden property="method" value="showTestMarks"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>

	<logic:empty name="infoStudentTestLogList">
		<h2><bean:message key="message.test.no.log"/></h2>
	</logic:empty>

	<logic:notEmpty name="infoStudentTestLogList">
		<logic:iterate id="log" name="infoStudentTestLogList" indexId="index">
			<logic:equal name="index" value="0">
			<bean:define id="number" name="log" property="infoStudent.number"/>
			<h2><bean:message key="title.showStudentTestLog"/>&nbsp;<bean:write name="number"/></h2>
			<br/>
			<table>
				<tr><td class="infoop"><bean:message key="message.showStudentTestLog.information" /></td></tr>
			</table>
			<br/>
			<table>
			<bean:define id="questionNumber" value="<%=(pageContext.findAttribute("questionNumber")).toString()%>"/>
			<tr>
				<td class="listClasses-header"><b>Data</b></td>
				<td class="listClasses-header"><b>Evento</b></td>
				<% for(int i=1; i<=new Integer(questionNumber).intValue()-1;i++ ){ 
					out.write(new String("<td class='listClasses-header'><b>P"+i+"</b></td>"));				
				} %>
			</tr>
			</logic:equal>
			<tr>
				<td class="listClasses"><bean:write name="log" property="dateFormatted"/></td>
				<logic:iterate id="event" name="log" property="eventList" indexId="eventIndex">
					<td class="listClasses"><bean:write name="event"/></td>
				</logic:iterate>
				<bean:define id="questionNumber" value="<%=(pageContext.findAttribute("questionNumber")).toString()%>"/>
				<logic:notEqual name="eventIndex" value="<%=new Integer(new Integer(questionNumber).intValue()-1).toString()%>">
					<bean:define id="thisEvent" name="eventIndex"/>
					<% for(int i=((Integer)thisEvent).intValue(); i<new Integer(questionNumber).intValue()-1;i++ ){ 
						out.write(new String("<td class='listClasses'>-</td>"));
					} %>
				</logic:notEqual>
				
			</tr>
		</logic:iterate>
		</table>

	<br/>
	</logic:notEmpty>
	<html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>	
</logic:present>
