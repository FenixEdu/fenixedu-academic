<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<span class="error"><html:errors property="error.default" /></span>

<h2><bean:message key="label.submit.listMarks" /></h2>

<span class="error"><bean:message key="message.submit.warning" /></span>
<br /><br />

<logic:present name="siteView">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.submit.information" /></td>
		</tr>
	</table>

<html:form action="/marksList">
<html:hidden property="page" value="1"/>	
<table>
<tr>
	<td>
		<bean:message key="label.data.avaliacao"/>:
	</td>
	<td> 
		<html:text property="day" size="2" maxlength="2" />&nbsp;/&nbsp;
		<html:text property="month" size="2" maxlength="2"/>&nbsp;/&nbsp;
		<html:text property="year" size="4" maxlength="4"/>
	</td>
</tr>
</table>
<br />

<html:hidden property="method" value="submitMarks"/>
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>