<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/teachingCareer">
<br/>
<h3>
<logic:present name="infoTeachingCareer">
<bean:message key="message.teachingCareer.edit" />
</logic:present>
<logic:notPresent name="infoTeachingCareer">
<bean:message key="message.teachingCareer.insertCareer" />
</logic:notPresent>
<logic:present name="categories">
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teachingCareer.managementEdit" /></p>
	<span class="error">
		<html:errors/>
	</span>
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="infoTeacher#idInternal"/>
<table>
	<tr>
		<td><bean:message key="message.teachingCareer.years" /></td>
	</tr>
	<tr>
		<td>
			<html:text property="beginYear"/>&nbsp;-&nbsp;
			<html:text property="endYear"/>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.category" /></td>
	</tr>
	<tr>
		<td>
			<html:select property="infoCategory#idInternal">
				<option></option>
				<html:options collection="categories" property="idInternal" labelProperty="shortName"/>
			</html:select>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.CourseOrPosition" /></td>
	</tr>
	<tr>
		<td><html:text property="courseOrPosition"/></td>
	<tr/>
</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</logic:present>
</html:form>
<logic:notPresent name="categories">
	Não existem as categorias
</logic:notPresent>