<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<logic:present name="studentTestLogList">
<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTestMarks"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
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
				<% for(int i=1; i<=new Integer(questionNumber).intValue();i++ ){ 
					out.write(new String("<th class='listClasses-header'><b>P"+i+"</b></th>"));				
				} %>
				<th class="listClasses-header"><b>Código Validação</b></th>
			</tr>
			</logic:equal>
			<tr>
				<td class="listClasses"><bean:write name="log" property="dateFormatted"/></td>
				
				<logic:iterate id="event" name="log" property="eventList" indexId="eventIndex">
					<logic:notEmpty name="event">
						<td class="listClasses"><bean:write name="event"/></td>
					</logic:notEmpty>
					<logic:empty name="event">
						<td class='listClasses'>-</td>
					</logic:empty>
					
				</logic:iterate>
				<bean:define id="questionNumber" value="<%=(pageContext.findAttribute("questionNumber")).toString()%>"/>
				<logic:notEqual name="eventIndex" value="<%=new Integer(new Integer(questionNumber).intValue()).toString()%>">
					<bean:define id="thisEvent" name="eventIndex"/>
					<% for(int i=((Integer)thisEvent).intValue(); i<new Integer(questionNumber).intValue();i++ ){ 
						out.write(new String("<td class='listClasses'>-</td>"));
					} %>
				</logic:notEqual>
				<td class="listClasses"><bean:write name="log" property="checksum"/></td>
			</tr>
		</logic:iterate>
		</table>

	<br/>
	</logic:notEmpty>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>	
</logic:present>
