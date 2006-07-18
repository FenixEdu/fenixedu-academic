<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>	

<logic:present name="infoSiteStudentDistributedTests">
	
	<bean:define id="infoDistributedTestsToDo" name="infoSiteStudentDistributedTests" property="infoDistributedTestsToDo"/>

	<logic:empty name="infoDistributedTestsToDo">
		<table>
	</logic:empty>
	<logic:notEmpty name="infoDistributedTestsToDo">
	<table>
		<tr>
		<td colspan="3" ><h2><bean:message key="link.toDoTests"/></h2></td>
		</tr>
		<tr>
			<td class="listClasses"><b><bean:message key="label.title"/></b></td>
			<td class="listClasses"><b><bean:message key="label.testBeginDate"/></b></td>
			<td class="listClasses"><b><bean:message key="label.testEndDate"/></b></td>
		</tr>
		<logic:iterate id="distributedTest" name="infoDistributedTestsToDo" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest">
		<tr>
			<td class="listClasses">
		
				<html:link page="/studentTests.do?method=prepareToDoTest" paramId="testCode" paramName="distributedTest" paramProperty="idInternal">
				<bean:write name="distributedTest" property="title"/>
				</html:link>
								
			</td>
			<td class="listClasses"><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
			<td class="listClasses"><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
		</tr>
		</logic:iterate>
	</logic:notEmpty>
	
	<bean:define id="infoDoneDistributedTests" name="infoSiteStudentDistributedTests" property="infoDoneDistributedTests"/>
	
	<logic:empty name="infoDoneDistributedTests">
		</table>
	</logic:empty>
	<logic:notEmpty name="infoDoneDistributedTests">
		<tr>
			<td colspan="3"><br/><br/><h2><bean:message key="link.doneTests"/></h2></td>
		</tr>
		<tr>
			<td class="listClasses"><b><bean:message key="label.title"/></b></td>
			<td class="listClasses"><b><bean:message key="label.testBeginDate"/></b></td>
			<td class="listClasses"><b><bean:message key="label.testEndDate"/></b></td>
		</tr>
		<logic:iterate id="distributedTest" name="infoDoneDistributedTests" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest">
		<tr>
			<td class="listClasses">
				<html:link page="/studentTests.do?method=showTestCorrection" paramId="testCode" paramName="distributedTest" paramProperty="idInternal">
				<bean:write name="distributedTest" property="title"/>
				</html:link>
				
			</td>
			<td class="listClasses"><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
			<td class="listClasses"><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
		</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="infoSiteStudentDistributedTests">
	N�o h�!!!
</logic:notPresent>