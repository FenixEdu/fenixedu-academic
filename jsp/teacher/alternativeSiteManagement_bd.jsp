<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h3><bean:message key="title.personalizationOptions"/></h3>
	<table border="0" style="text-align: left;">


<html:form action="/alternativeSite">



<table>
<tr>
	<td>
		<h2><bean:message key="message.siteAddress"/></h2>
	</td>
	<td>
		<html:text property="siteAddress" size="30"/>
	</td>
	<td>
		<span class="error" ><html:errors property="siteAddress"/></span>
	</td>
</tr>	
<tr>
	<td>
		<h2><bean:message key="message.mailAddress"/></h2>
	</td>	
	<td>
		<html:text property="mail" size="30"/>
	</td>
	<td>
		<span class="error" ><html:errors property="mail"/></span>
	</td>
</tr>
<tr>
	<td>
		<h2><bean:message key="message.initialStatement"/></h2>
	</td>	
	<td>
		<html:textarea name="<%=SessionConstants.INFO_SITE%>" property="initialStatement" rows="4" cols="56"/>
	</td>
</tr>
<tr>
	<td>
		<h2><bean:message key="message.introduction"/></h2>
	</td>	
	<td>
		<html:textarea name="<%=SessionConstants.INFO_SITE%>" property="introduction" rows="4" cols="56"/>
	</td>
</tr>
</table>
<h3><table>
<html:hidden property="method" value="edit"/>
<html:hidden property="page" value="1"/>
<tr align="center">	
	<td>
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
	</td>
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>
	</td>
</tr>
</table></h3>
<bean:message key="message.initialStatement.explanation" />
</html:form>







