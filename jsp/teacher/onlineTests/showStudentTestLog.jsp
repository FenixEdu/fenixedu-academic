<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="studentTestLogList">
<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTestMarks"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>

	<logic:empty name="studentTestLogList">
		<h2><bean:message key="message.test.no.log"/></h2>
	</logic:empty>

	<logic:notEmpty name="studentTestLogList">
		<logic:iterate id="log" name="studentTestLogList" indexId="index">
			<logic:equal name="index" value="0">
			<bean:define id="number" name="log" property="student.number"/>
			<h2><bean:message key="title.showStudentTestLog"/>&nbsp;<bean:write name="number"/></h2>
			<br/>
			<table>
				<tr><td class="infoop"><bean:message key="message.showStudentTestLog.information" /></td></tr>
			</table>
			<br/>
			<table>
			<bean:define id="questionNumber" value="<%=(pageContext.findAttribute("questionNumber")).toString()%>"/>
			<tr>
				<th class="listClasses-header"><b>Data</b></th>
				<th class="listClasses-header"><b>Evento</b></th>
				<% for(int i=1; i<=new Integer(questionNumber).intValue()-1;i++ ){ 
					out.write(new String("<th class='listClasses-header'><b>P"+i+"</b></th>"));				
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
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>	
</logic:present>
