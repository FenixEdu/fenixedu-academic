<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="link.tests"/></h2>

<bean:define id="infoDistributedTestsList" name="infoSiteDistributedTests" property="infoDistributedTests"/>

<logic:empty name="infoDistributedTestsList">
	<span class="error"><bean:message key="message.noStudentDoneTests"/></span>
</logic:empty>

<logic:notEmpty name="infoDistributedTestsList">
<table>
	<tr>
		<td class="listClasses"><b><bean:message key="label.title"/></b></td>
		<td class="listClasses"><b><bean:message key="label.testBeginDate"/></b></td>
		<td class="listClasses"><b><bean:message key="label.testEndDate"/></b></td>
	</tr>
	<logic:iterate id="distributedTest" name="infoSiteDistributedTests" property="infoDistributedTests" type="DataBeans.InfoDistributedTest">
	<tr>
		<bean:define id="test" name="distributedTest" property="infoTest"/>
		<td class="listClasses">
			<html:link page="/studentTests.do?method=showTestCorrection" paramId="testCode" paramName="distributedTest" paramProperty="idInternal">
				<bean:write name="test" property="title"/>
			</html:link>
		</td>
		<td class="listClasses"><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
		<td class="listClasses"><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>