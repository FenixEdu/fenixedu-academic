<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<table border="0" style="text-align: left;">

<html:form action="/alternativeSite">

<logic:present name="<%=SessionConstants.ALTERNATIVE_SITE%>">	
	<html:link href="<%=(String)session.getAttribute(SessionConstants.ALTERNATIVE_SITE)%>">
		<%=(String)session.getAttribute(SessionConstants.ALTERNATIVE_SITE)%>
	</html:link>	
</logic:present>
<br/>

<logic:present name="<%=SessionConstants.MAIL%>">	
	<html:link href="<%= "mailto:" + (String)session.getAttribute(SessionConstants.MAIL)%>">
		<%=(String)session.getAttribute(SessionConstants.MAIL)%>
	</html:link>
</logic:present>
<br/>

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

<html:hidden property="method" value="edit"/>
<html:hidden property="page" value="1"/>
<tr align="center">
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.confirm"/>
	</html:submit>
	</td>
</tr>
</html:form>

</table>





