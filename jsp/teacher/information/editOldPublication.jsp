<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/oldPublication">
<br/>
<h3>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.insertPublication" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.editPublication" />
	</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.managementInsert" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.managementEdit" />
	</logic:notPresent></p>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="infoTeacher#idInternal"/>
	<html:hidden property="method" value="edit"/>
<table>
	<tr>
		<td><bean:message key="message.publications.publication" /></td>
	</tr>
	<tr>
		<td><html:textarea property="publication" cols="90%" rows="4"/></td>
	<tr/>
</table>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
</logic:present>