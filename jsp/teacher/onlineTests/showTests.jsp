<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.showTests" /></h2>

<logic:present name="infoTestList">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />

	<bean:size id="testsSize" name="infoTestList" />
	<logic:equal name="testsSize" value="0">
		<span class="error"><bean:message key="message.tests.no.tests" /></span>
	</logic:equal>
	<logic:notEqual name="testsSize" value="0">
		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showTests.information" /></td>
			</tr>
		</table>
		<br />
		<div class="gen-button"><html:link
			page="<%= "/testsManagement.do?method=prepareCreateTest&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
			<bean:message key="link.createTest" />
		</html:link></div>
		<br />
		<span class="error"><html:errors property="InvalidDistribution" /></span>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.test.title" /></th>
				<th class="listClasses-header"><bean:message key="label.test.creationDate" /></th>
				<th class="listClasses-header"><bean:message key="label.test.lastModifiedDate" /></th>
				<th width="100" class="listClasses-header"><bean:message key="label.test.numberOfQuestions" /></th>
			</tr>
			<logic:iterate id="tests" name="infoTestList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTest">
				<tr>
					<td class="listClasses"><bean:write name="tests" property="title" /></td>
					<td class="listClasses"><bean:write name="tests" property="creationDateFormatted" /></td>
					<td class="listClasses"><bean:write name="tests" property="lastModifiedDateFormatted" /></td>
					<td class="listClasses"><bean:write name="tests" property="numberOfQuestions" /></td>
					<bean:define id="testCode" name="tests" property="idInternal" />
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testEdition.do?method=editTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
						<bean:message key="label.edit" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testsManagement.do?method=prepareDeleteTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
						<bean:message key="link.remove" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testEdition.do?method=editAsNewTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
						<bean:message key="label.duplicate" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testDistribution.do?method=prepareDistributeTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + testCode %>">
						<bean:message key="link.student.room.distribution" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/studentTestManagement.do?method=chooseTestSimulationOptions&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" +testCode %>">
						<bean:message key="label.Simulate" />
					</html:link></div>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>

</logic:present>
