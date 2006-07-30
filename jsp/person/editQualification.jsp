<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>

<html:form action="/qualificationForm">
<span class="error"><!-- Error messages go here -->
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPerson#idInternal" property="infoPerson#idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.countryIdInternal" property="countryIdInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.branch" property="branch" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.specializationArea" property="specializationArea" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeRecognition" property="degreeRecognition" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tempEquivalenceDate" property="tempEquivalenceDate" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.equivalenceSchool" property="equivalenceSchool" />
	<table>
		<tr>
			<td><bean:message key="message.teacherInformation.year" /></td>
		</tr>
		<tr>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.tempDate" property="tempDate"/>&nbsp;(aaaa)
			</td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.school" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.school" property="school"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsTitle" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsDegree" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.degree" property="degree"/></td>
		<tr/>
		<tr>
			<td><bean:message key="message.teacherInformation.qualificationsMark" /></td>
		</tr>
		
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mark" property="mark"/></td>
		<tr/>
	</table>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>