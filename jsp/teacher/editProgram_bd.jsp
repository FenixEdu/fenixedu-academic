<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
<bean:define id="program" name="siteView" property="component"/>

<h2><bean:message key="title.program"/></h2>
<html:form action="/programManagerDA">
<html:hidden property="page" value="1"/>	
<table>		
	<tr>
		<td colspan="2"><strong><bean:message key="label.program" /></strong></td>
	</tr>
	<tr>
		<td><html:textarea rows="10'" cols="80" property="program" /></td>
		<td> <span class="error" ><html:errors property="program"/></span></td>
	</tr>
	<tr>
		<td colspan="2"><strong><bean:message key="label.program.eng" /></strong></td>
	</tr>
	<tr>
		<td><html:textarea rows="10'" cols="80" property="programEn"/></td>
		<td> <span class="error" ><html:errors property="programEn"/></span></td>
	</tr>
</table>
<br />
<bean:define id="curricularCourseCode" name="curricularCourseCode"/>
<html:hidden property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"/>
<html:hidden property="method" value="editProgram"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>
</html:form>
</logic:present>