<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.personalizationOptions"/></h2>
<html:form action="/alternativeSite">
<br />
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_1.gif" alt="0" />
		</td>
		<td class="infoop"><bean:message key="message.siteandmail.information" />
		</td>
	</tr>
</table>
<br />
<table width="100%">
	<td width="200px">
		<bean:message key="message.siteAddress"/>
	</td>
	<td><html:text property="siteAddress" size="30"/>
	</td>
	<td><span class="error" ><html:errors property="siteAddress"/></span>
	</td>
</tr>
<tr>
	<td width="200px">
	  <bean:message key="message.mailAddress"/>
	<td>
	  <html:text property="mail" size="30"/>
	</td>
	<td><span class="error" >
	  <html:errors property="mail"/></span>
    </td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_2.gif" alt="0" />
	</td>
	<td class="infoop">
	  <bean:message key="message.initialStatement.explanation" />
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td width="200px" valign="top">
		<bean:message key="message.initialStatement"/>
	</td>	
	<td><html:textarea name="<%=SessionConstants.INFO_SITE%>" property="initialStatement" rows="4" cols="56"/>
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_3.gif" alt="0" />
	</td>
	<td class="infoop">
	  <bean:message key="message.introduction.explanation" />
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td width="200px" valign="top">
		<bean:message key="message.introduction"/>
	</td>	
	<td><html:textarea name="<%=SessionConstants.INFO_SITE%>" property="introduction" rows="4" cols="56"/></td>
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
</html:form>