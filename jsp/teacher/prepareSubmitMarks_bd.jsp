<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<span class="error"><!-- Error messages go here --><html:errors property="error.default" /></span>

<h2><bean:message key="label.submit.listMarks" /></h2>
<br /><br />

<logic:present name="siteView">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.submit.information" /></td>
		</tr>
	</table>

<html:form action="/marksList">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
<table>
<tr>
	<td>
		<bean:message key="label.data.avaliacao"/>:
	</td>
	<td> 
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" property="day" size="2" maxlength="2" />&nbsp;/&nbsp;
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" property="month" size="2" maxlength="2"/>&nbsp;/&nbsp;
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year" size="4" maxlength="4"/>
	</td>
</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submitMarks"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>