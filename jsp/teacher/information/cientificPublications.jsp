<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="siteView"> 
<html:form action="/teacherInformation">
<bean:define id="siteTeacherInformation" name="siteView" property="component"/>
<br/>
<h3><bean:message key="message.cientificPublications" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.cientificPublications.management" /></p>
<bean:message key="message.publications.managementInsertExplanation" />
<bean:message key="message.publications.managementCleanExplanation" />
<bean:message key="message.publications.managementInsertPubExplanation" />
<bean:message key="message.publications.managementSaveExplanation" />
<bean:message key="message.publications.managementSeeExplanation" />
<table border="1">
<logic:iterate id="" name="" property="">
<tr>
	<td><bean:write name="" property="" /></td>
	<td><%-- link editar%--><%--<div class="gen-button">
	<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="label.edit" />
	</html:link></div>--%>
	</td>
	<td><%-- link apagar%--><%--<div class="gen-button">
	<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="label.clear" />
	</html:link></div>--%>
	</td>
</tr>
</logic:iterate>
</table>
<%--<div class="gen-button">
	<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="label.insertPublication" />
	</html:link></div>--%>
<br />
<h3>
<table>
<tr align="center">	
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.continue"/>
	</html:submit>
	</td>
	<td>
		<html:reset styleClass="inputbutton">
		<bean:message key="button.seeInformation"/>
	</html:reset>
	</td>
</tr>
</table>
</h3>
</html:form>
</logic:present>
