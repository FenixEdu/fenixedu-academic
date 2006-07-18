<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.showDistributedTests" /></h2>
<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0; i<document.forms[0].distributedTestCodes.length; i++){
		var e = document.forms[0].distributedTestCodes[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}
// -->
</script>
<logic:present name="infoDistributedTests">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />

	<logic:present name="successfulDistribution">
		<logic:equal name="successfulDistribution" value="true">
			<span class="error"><bean:message key="message.successfulDistribution" /></span>
		</logic:equal>
	</logic:present>
	<logic:present name="successfulEdition">
		<logic:equal name="successfulEdition" value="true">
			<span class="error"><bean:message key="message.successfulTestEdition" /></span>
		</logic:equal>
	</logic:present>
	<logic:present name="successfulTestDeletion">
		<logic:equal name="successfulTestDeletion" value="true">
			<span class="error"><bean:message key="message.successfulTestDeletion" /></span>
		</logic:equal>
	</logic:present>
	<logic:present name="infoStudentList">
		<logic:notEmpty name="infoStudentList">
			<br/>
			<span class="error"><bean:message key="message.insuccessfulAdvisoryDistribution" /></span>
			<logic:iterate id="infoStudent" name="infoStudentList">
				<br/>
				<bean:write name="infoStudent" property="number"/>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
	<br />
	<br />
	<bean:size id="distrubutedTestsSize" name="infoDistributedTests" />
	<logic:equal name="distrubutedTestsSize" value="0">
		<span class="error"><bean:message key="message.tests.no.distributedTests" /></span>
	</logic:equal>
	<logic:notEqual name="distrubutedTestsSize" value="0">
		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showDistributedTests.information" /></td>
			</tr>
		</table>
		<br />
		<html:form action="/distributedTestMarks">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="downloadTestMarks" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
			<span class="error"><html:errors /></span>
			<table>
				<tr>
					<td class="listClasses-header"></td>
					<th class="listClasses-header"><bean:message key="label.test.title" /></th>
					<th class="listClasses-header"><bean:message key="message.testBeginDate" /></th>
					<th class="listClasses-header"><bean:message key="message.testEndDate" /></th>
				</tr>
				<logic:iterate id="distributedTest" name="infoDistributedTests" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest">
					<tr>
						<bean:define id="testType" name="distributedTest" property="testType.type" />
						<bean:define id="distributedTestCode" name="distributedTest" property="idInternal" />
						<td class="listClasses"><%if (((Integer) testType).intValue() != 3) {

        %> <html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.distributedTestCodes" property="distributedTestCodes">
							<bean:write name="distributedTestCode" />
						</html:multibox> <%} else {

        %> <html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.distributedTestCodes" property="distributedTestCodes" disabled="true">
							<bean:write name="distributedTestCode" />
						</html:multibox> <%}

        %></td>
						<td class="listClasses"><html:link
							page="<%= "/distributedTestEdition.do?method=prepareEditDistributedTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
							<bean:write name="distributedTest" property="title" />
						</html:link></td>
						<td class="listClasses"><bean:write name="distributedTest" property="beginDateTimeFormatted" /></td>
						<td class="listClasses"><bean:write name="distributedTest" property="endDateTimeFormatted" /></td>
						<td>
						<div class="gen-button"><html:link
							page="<%= "/testDistribution.do?method=prepareDeleteDistributedTest&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
							<bean:message key="link.remove" />
						</html:link></div>
						</td>
						<%if (((Integer) testType).intValue() != 3) {

            %>
						<td>
						<div class="gen-button"><html:link
							page="<%= "/testDistribution.do?method=showTestMarksStatistics&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
							<bean:message key="label.test.statistics" />
						</html:link></div>
						</td>
						<td>
						<div class="gen-button"><html:link
							page="<%= "/testDistribution.do?method=showTestMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
							<bean:message key="label.test.marks" />
						</html:link></div>
						</td>
						<%--	<td><div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=downloadTestMarks&amp;distributedTestCode=" +distributedTestCode+ "&amp;objectCode=" +pageContext.findAttribute("objectCode")%>">
			<bean:message key="link.export"/>
			</html:link></div></td> --%>
						<%} else {

            %>
						<td>
						<div class="gen-button"><html:link
							page="<%= "/testDistribution.do?method=showTestStatistics&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;distributedTestCode=" + distributedTestCode %>">
							<bean:message key="label.test.statistics" />
						</html:link></div>
						</td>
						<td></td>
						<%}

    %>
					</tr>
				</logic:iterate>
			</table>
			<br />
			<html:link href="javascript:invertSelect()">
				<bean:message key="label.selectAllTests" />
			</html:link>
			<br />
			<br />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="link.export" />
			</html:submit>
		</html:form>
	</logic:notEqual>

</logic:present>
