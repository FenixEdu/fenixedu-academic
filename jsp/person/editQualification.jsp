<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>

<html:form action="/qualificationForm">
<span class="error">
	<html:errors/>
</span>
<br/>
<h3>
<logic:present name="infoQualification">
<bean:message key="message.teacherInformation.editQualifications" />
</logic:present>
<logic:notPresent name="infoQualification">
<bean:message key="message.teacherInformation.insertQualifications" />
</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teacherInformation.managementEdit" /></p>
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="infoPerson#idInternal"/>
	<html:hidden property="countryIdInternal"/>
	<html:hidden property="branch" />
	<html:hidden property="specializationArea" />
	<html:hidden property="degreeRecognition" />
	<html:hidden property="tempEquivalenceDate" />
	<html:hidden property="equivalenceSchool" />
	<table>
		<tr>
			<td><bean:message key="message.teacherInformation.year" /></td>
		</tr>
		<tr>
			<td>
				<html:text property="tempDate"/>&nbsp;(dd/mm/aaaa)
			</td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.school" /></td>
		</tr>
		<tr>
			<td><html:text property="school"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsTitle" /></td>
		</tr>
		<tr>
			<td><html:text property="title"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsDegree" /></td>
		</tr>
		<tr>
			<td><html:text property="degree"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsMark" /></td>
		</tr>
		
		<tr>
			<td><html:text property="mark"/></td>
		<tr/>
	</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</logic:present>
</html:form>