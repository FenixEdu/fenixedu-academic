<%@ page language="java" %>
<%@ page import="javax.swing.ImageIcon" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showTests"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>

<bean:size id="testsSize" name="component" property="infoTests"/>
<logic:equal name="testsSize" value="0">
	<span class="error"><bean:message key="message.tests.no.tests"/></span>
</logic:equal>
<logic:notEqual name="testsSize" value="0">
<table>
	<tr>
		<td width="100" class="listClasses-header"><bean:message key="label.test.title"/></td>
		<td width="100" class="listClasses-header"><bean:message key="label.test.creationDate"/></td>
		<td width="100" class="listClasses-header"><bean:message key="label.test.lastModifiedDate"/></td>
		<td width="100" class="listClasses-header"><bean:message key="label.test.numberOfQuestions"/></td>
	</tr>
	<logic:iterate id="tests" name="component" property="infoTests" type="DataBeans.InfoTest">
	<tr>
		<td class="listClasses"><bean:write name="tests" property="title"/></td>
		<td class="listClasses"><bean:write name="tests" property="creationDateFormatted"/></td>
		<td class="listClasses"><bean:write name="tests" property="lastModifiedDateFormatted"/></td>
		<td class="listClasses"><bean:write name="tests" property="numberOfQuestions"/></td>
		<bean:define id="testCode" name="tests" property="idInternal" />
		<td>
			<div class="gen-button">
			<html:link page="<%= "/testEdition.do?method=editTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
			<bean:message key="link.show" />
			</html:link></div>
		</td>
		<td>
			<div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=deleteTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
			<bean:message key="link.remove" />
			</html:link></div>
		</td>
		
	</tr>
	</logic:iterate>
</table>
</logic:notEqual>

</logic:present>