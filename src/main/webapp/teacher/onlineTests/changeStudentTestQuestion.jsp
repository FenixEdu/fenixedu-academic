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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="title.changeTestQuestion"/></h2>

<script language="Javascript" type="text/javascript">
<!--

function selectDelete(){
	var e = document.forms[0].studentsType;
	e[3].checked=true;	
}
		
function selectAnotherExercise(){
	var e = document.forms[0].studentsType;
	
	if(document.forms[0].changesType[0].checked==true){
		e[2].disabled=true;
		if(e[2].checked==true){
			e[1].checked=true;
}
	} else {
		e[2].disabled=false;
	}
}

function selectStudents() { 
	var studentType = document.forms[0].studentsType;
	var deleteType = document.forms[0].deleteVariation;
	var disable = false;
	
	if(studentType[3].checked==false)
		disable=true;
	for (var i=0; i<document.forms[0].deleteVariation.length; i++){
		var e = document.forms[0].deleteVariation[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
	
}

// -->
</script>

<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentTestQuestion"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionCode" property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
<table>
	<tr><td class="infoop"><bean:message key="message.changeStudentTestQuestion.information" /></td></tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.modifyBy"/></b></td>
	</tr>
	<logic:iterate id="changesType" name="changesTypeList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="changesType" property="label"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.changesType" property="changesType" value="<%=changesType.getValue()%>" onclick="selectAnotherExercise()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.modifyFor"/></b></td>
	</tr>
	<logic:iterate id="studentsType" name="studentsTypeList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="studentsType" property="label"/></td>
			<bean:define id="disabled" value="false"/>
			<logic:equal name="studentsType" property="value" value="3">
				<bean:define id="disabled" value="true"/>
			</logic:equal>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.studentsType" property="studentsType" value="<%=studentsType.getValue()%>" disabled="<%=new Boolean(disabled).booleanValue()%>" onclick="selectStudents()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.deleteVariation"/></b></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="button.yes"/></td><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.deleteVariation" property="deleteVariation" value="true" onclick="selectDelete()"/></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="button.no"/></td><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.deleteVariation" property="deleteVariation" value="false"/></td>
	</tr>
</table>
<br/>
<br/>
<table align="center">
<tr>
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.change"/></html:submit></td>
	<td><html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
	<%--
	<td>
		<html:form action="/testsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showStudentTest"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
	</td></html:form>
	--%>
</tr>
</table>