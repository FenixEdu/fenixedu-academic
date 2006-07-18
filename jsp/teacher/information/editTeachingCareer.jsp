<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#idInternal" property="infoTeacher#idInternal"/>
<table>
	<tr>
		<td><bean:message key="message.teachingCareer.years" /></td>
	</tr>
	<tr>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginYear" property="beginYear"/>&nbsp;-&nbsp;
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.endYear" property="endYear"/>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.category" /></td>
	</tr>
	<tr>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.infoCategory#idInternal" property="infoCategory#idInternal">
				<html:options collection="categories" property="idInternal" labelProperty="shortName"/>
			</html:select>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.teachingCareer.CourseOrPosition" /></td>
	</tr>
	<tr>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.courseOrPosition" property="courseOrPosition"/></td>
	<tr/>
</table>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</logic:present>
</html:form>
<logic:notPresent name="categories">
	N�&atilde;o existem as categorias
</logic:notPresent>