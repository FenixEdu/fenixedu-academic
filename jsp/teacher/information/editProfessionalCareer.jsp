<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/professionalCareer">
<br/>
<h3>
<logic:present name="infoProfessionalCareer">
<bean:message key="message.professionalCareer.edit" />
</logic:present>
<logic:notPresent name="infoProfessionalCareer">
<bean:message key="message.professionalCareer.insert" />
</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.professionalCareer.managementEdit" /></p>
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit"/>
	<html:hidden property="careerId"/>
	<html:hidden property="careerType"/>
	<html:hidden property="teacherId"/>
<table>
	<tr>
		<td><bean:message key="message.professionalCareer.years" /></td>
	</tr>
	<tr>
		<td>
			<html:text property="beginYear"/>&nbsp;-&nbsp;
			<html:text property="endYear"/>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.professionalCareer.entity" /></td>
	</tr>
	<tr>
		<td><html:text property="entity"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.professionalCareer.function" /></td>
	</tr>
	<tr>
		<td><html:text property="function"/></td>
	<tr/>
</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>