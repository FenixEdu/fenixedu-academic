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
<h3><bean:message key="message.teachingCareer.editCareer" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teachingCareer.managementEdit" /></p>

<html:form action="/insertCareer">
	<%--<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertSummary"/>
	<html:hidden property="objectCode"/>--%>
<table>
	<tr>
		<td><bean:message key="message.teachingCareer.years" /></td>
	</tr>
	<tr>
		<td><html:text property="careerYears"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.category" /></td>
	</tr>
	<tr>
		<td><html:text property="careerCategory"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.CourseOrPosition" /></td>
	</tr>
	<tr>
		<td><html:text property="careerCourseOrPosition"/></td>
	<tr/>
</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
</logic:present>