<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.changeTestQuestion"/></h2>

<script language="Javascript" type="text/javascript">
<!--

function selectDelete(){
	var e = document.forms[0].studentsType;
	e[2].checked=true;
		
}

function selectStudents() { 
	var studentType = document.forms[0].studentsType;
	var deleteType = document.forms[0].deleteVariation;
	var disable = false;
	
	if(studentType[2].checked==false)
		disable=true;
	for (var i=0; i<document.forms[0].deleteVariation.length; i++){
		var e = document.forms[0].deleteVariation[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
	
}

// -->
</script>

<html:form action="/testsManagement">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="changeStudentTestQuestion"/>
<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
<html:hidden property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
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
			<td><html:radio property="changesType" value="<%=changesType.getValue()%>"/></td>
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
			<td><html:radio property="studentsType" value="<%=studentsType.getValue()%>" onclick="selectStudents()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.deleteVariation"/></b></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="label.manager.yes"/></td><td><html:radio property="deleteVariation" value="true" onclick="selectDelete()"/></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="label.manager.no"/></td><td><html:radio property="deleteVariation" value="false"/></td>
	</tr>
</table>
<br/>
<br/>
<table align="center">
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="label.change"/></html:submit></td>
	<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
	<%--
	<td>
		<html:form action="/testsManagement">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="showStudentTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
		<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
	</td></html:form>
	--%>
</tr>
</table>