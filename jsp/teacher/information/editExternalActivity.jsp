<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/externalActivity">
<br/>
<h3>
<logic:present name="infoExternalActivity">
<bean:message key="message.externalActivities.edit" />
</logic:present>
<logic:notPresent name="infoExternalCareer">
<bean:message key="message.externalActivities.insert" />
</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.externalActivities.managementEdit" /></p>
	<html:hidden property="page" value="1"/>
	<html:hidden property="externalActivityId"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="method" value="edit"/>
<table>
	<tr>
		<td><bean:message key="message.externalActivities.activity" /></td>
	</tr>
	<tr>
		<td><html:textarea property="activity" cols="100%" rows="4"/></td>
	<tr/>
</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
</logic:present>