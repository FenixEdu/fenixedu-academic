<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
<bean:message key="message.professionalCareer.insertProfessional" />
</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.professionalCareer.managementEdit" /></p>
	<span class="error">
		<html:errors/>
	</span>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#idInternal" property="infoTeacher#idInternal"/>
<table>
	<tr>
		<td><bean:message key="message.professionalCareer.years" /></td>
	</tr>
	<tr>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginYear" property="beginYear"/>&nbsp;-&nbsp;
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.endYear" property="endYear"/>
		</td>
	<tr/>
	<tr>
		<td><bean:message key="message.professionalCareer.entity" /></td>
	</tr>
	<tr>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.entity" property="entity"/></td>
	<tr/>
	<tr>
		<td><bean:message key="message.professionalCareer.function" /></td>
	</tr>
	<tr>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.function" property="function"/></td>
	<tr/>
</table>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>