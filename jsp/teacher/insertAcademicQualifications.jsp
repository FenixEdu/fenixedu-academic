<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="siteView"> 
<html:form action="/teacherInformation">
<bean:define id="siteTeacherInformation" name="siteView" property="component"/>
<br/>
<h3><bean:message key="message.teacherInformation.insertQualifications" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teacherInformation.managementInsert" /></p>

<html:form action="/insertQualification">
	<%--<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertSummary"/>
	<html:hidden property="objectCode"/>--%>
	<table>
	
	<tr>
		<td><bean:message key="message.teacherInformation.year" /></td>
	</tr>
	<tr>
		<td><html:text property="qualificationYear"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.teacherInformation.school" /></td>
	</tr>
	<tr>
		<td><html:text property="qualificationSchool"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.teacherInformation.qualificationsDegree " /></td>
	</tr>
	<tr>
		<td><html:text property="qualificationDegree"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.teacherInformation.qualificationsCourse " /></td>
	</tr>
	<tr>
		<td><html:text property="qualificationCourse"/></td>
	<tr/>
</table>
<br/>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>                    		         	
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
</logic:present>
