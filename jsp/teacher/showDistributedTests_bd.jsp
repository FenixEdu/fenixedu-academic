<%@ page language="java" %>
<%@ page import="javax.swing.ImageIcon" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showDistributedTests"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<logic:present name="successfulDistribution">
	<logic:equal name="successfulDistribution" value="true">
		<span class="error"><bean:message key="message.successfulDistribution"/></span>
	</logic:equal>
</logic:present>
<br/>
<br/>
<bean:size id="distrubutedTestsSize" name="component" property="infoDistributedTests"/>
<logic:equal name="distrubutedTestsSize" value="0">
	<span class="error"><bean:message key="message.tests.no.distributedTests"/></span>
</logic:equal>
<logic:notEqual name="distrubutedTestsSize" value="0">
<table>
	<tr>
		<td class="infoop"><bean:message key="message.showDistributedTests.information" /></td>
	</tr>
</table>
<br/>
<table>
	<tr>
		<td class="listClasses-header"><bean:message key="label.test.title"/></td>
		<td class="listClasses-header"><bean:message key="message.testBeginDate"/></td>
		<td class="listClasses-header"><bean:message key="message.testEndDate"/></td>
	</tr>
	<logic:iterate id="distributedTest" name="component" property="infoDistributedTests" type="DataBeans.InfoDistributedTest">
	<tr>
		<bean:define id="distributedTestCode" name="distributedTest" property="idInternal" />
		<td class="listClasses">
		<html:link page="<%= "/distributedTestEdition.do?method=prepareEditDistributedTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
				<bean:write name="distributedTest" property="title"/>
			</html:link>
		</td>
		<td class="listClasses"><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
		<td class="listClasses"><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
		<td>
		<div class="gen-button">
		<html:link page="<%= "/testDistribution.do?method=showTestMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
		<bean:message key="label.test.marks" />
		</html:link></div>
		</td>
	</tr>
	</logic:iterate>
</table>
</logic:notEqual>

</logic:present>