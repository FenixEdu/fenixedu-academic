<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em class="invisible"><bean:message key="title.student.portalTitle"/></em>
<h2 class="mbottom15"><bean:message key="link.tests"/></h2>
<logic:present name="tests">
	<logic:notEmpty name="tests">
		<h3 class="mbottom05"><bean:message key="link.toDoTests"/></h3>
		<table class="tstyle1 thlight mtop05">
			<tr>
				<th><bean:message key="label.title"/></th>
				<th><bean:message key="label.testBeginDate"/></th>
				<th><bean:message key="label.testEndDate"/></th>
				<th><bean:message key="message.studentTestLog.checksumReport"/></th>
			</tr>
			<logic:iterate id="registrationDistributedTests" name="tests" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.RegistrationDistributedTests">
				<bean:define id="student" name="registrationDistributedTests" property="registration.externalId"/>
				<logic:iterate id="distributedTest" name="registrationDistributedTests" property="distributedTestsToDo" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest">
					<bean:define id="testCode" name="distributedTest" property="externalId"/>
					<tr>
						<td>
							<html:link page="<%="/studentTests.do?method=prepareToDoTest&testCode="+testCode.toString()+"&student="+student.toString()%>">
								<bean:write name="distributedTest" property="title"/>
							</html:link>
						</td>
						<td><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
						<td><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
						<td class="acenter">
							<% 
							net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
							net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog studentTestLog = distributedTest.getLastSubmissionStudentTestLog(student.toString());
								if(studentTestLog!=null && studentTestLog.getChecksum()!=null){ %>
							<bean:define id="logId" value="<%= studentTestLog.getExternalId().toString() %>"/>
							<html:link page="<%="/studentTests.do?method=exportChecksum&logId="+logId.toString()%>">
								<bean:message key="message.studentTestLog.checksumReport" />
							</html:link>
							<%} else {%>
								-
							<%}%>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
		
		<h3 class="mbottom05"><bean:message key="link.doneTests"/></h3>
		<table class="tstyle1 thlight mtop05">
			<tr>
				<th><bean:message key="label.title"/></th>
				<th><bean:message key="label.testBeginDate"/></th>
				<th><bean:message key="label.testEndDate"/></th>
				<th><bean:message key="message.studentTestLog.checksumReport"/></th>
			</tr>
			<logic:iterate id="registrationDistributedTests" name="tests" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.RegistrationDistributedTests">
				<bean:define id="student" name="registrationDistributedTests" property="registration.externalId"/>
				<logic:iterate id="distributedTest" name="registrationDistributedTests" property="distributedTestsDone" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest">
					<bean:define id="testCode" name="distributedTest" property="externalId"/>
					<tr>
						<td>
							<html:link page="<%="/studentTests.do?method=showTestCorrection&testCode="+testCode.toString()+"&student="+student.toString()%>">
								<bean:write name="distributedTest" property="title"/>
							</html:link>
						</td>
						<td><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
						<td><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
						<td class="acenter">
							<% 
							net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
							net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog studentTestLog = distributedTest.getLastSubmissionStudentTestLog(student.toString());
								if(studentTestLog!=null && studentTestLog.getChecksum()!=null){ %>
							<bean:define id="logId" value="<%= studentTestLog.getExternalId().toString() %>"/>
							<html:link page="<%="/studentTests.do?method=exportChecksum&logId="+logId.toString()%>">
								<bean:message key="message.studentTestLog.checksumReport" />
							</html:link>
							<%} else {%>
								-
							<%}%>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
<%--
<logic:present name="testToDoList">
	<logic:notEmpty name="testToDoList">
		<h3 class="mbottom05"><bean:message key="link.toDoTests"/></h3>
		<table class="tstyle1 thlight mtop05">
			<tr>
				<th><bean:message key="label.title"/></th>
				<th><bean:message key="label.testBeginDate"/></th>
				<th><bean:message key="label.testEndDate"/></th>
				<th><bean:message key="message.studentTestLog.checksumReport"/></th>
			</tr>
			<logic:iterate id="distributedTest" name="testToDoList" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest">
				<bean:define id="student" name="distributedTest" property="student"/>
				<bean:define id="testCode" name="distributedTest" property="externalId"/>
				<tr>
					<td>
						<html:link page="<%="/studentTests.do?method=prepareToDoTest&testCode="+testCode.toString()+"&student="+student.toString()%>">
							<bean:write name="distributedTest" property="title"/>
						</html:link>
					</td>
					<td><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
					<td><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
					<td class="acenter">
						<% 
						net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
						net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog studentTestLog = distributedTest.getLastSubmissionStudentTestLog(user.getPerson().getStudent().getNumber());
							if(studentTestLog!=null && studentTestLog.getChecksum()!=null){ %>
						<bean:define id="logId" value="<%= studentTestLog.getExternalId().toString() %>"/>
						<html:link page="<%="/studentTests.do?method=exportChecksum&logId="+logId.toString()%>">
							<bean:message key="message.studentTestLog.checksumReport" />
						</html:link>
						<%} else {%>
							-
						<%}%>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
<logic:present name="doneTestsList">
	<logic:notEmpty name="doneTestsList">
		<h3 class="mbottom05"><bean:message key="link.doneTests"/></h3>
		<table class="tstyle1 thlight mtop05">
		<tr>
			<th><bean:message key="label.title"/></th>
			<th><bean:message key="label.testBeginDate"/></th>
			<th><bean:message key="label.testEndDate"/></th>
			<th><bean:message key="message.studentTestLog.checksumReport"/></th>
		</tr>
		<logic:iterate id="distributedTest" name="doneTestsList" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest">
		<tr>
			<td>
				<html:link page="/studentTests.do?method=showTestCorrection" paramId="testCode" paramName="distributedTest" paramProperty="externalId">
				<bean:write name="distributedTest" property="title"/>
				</html:link>
			</td>
			<td><bean:write name="distributedTest" property="beginDateTimeFormatted"/></td>
			<td><bean:write name="distributedTest" property="endDateTimeFormatted"/></td>
			<td class="acenter">
				<% 
				net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
				net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog studentTestLog = distributedTest.getLastSubmissionStudentTestLog(user.getPerson().getStudent().getNumber());
					if(studentTestLog!=null && studentTestLog.getChecksum()!=null){ %>
				<bean:define id="logId" value="<%= studentTestLog.getExternalId().toString() %>"/>
				<html:link page="<%="/studentTests.do?method=exportChecksum&logId="+logId.toString()%>">
					<bean:message key="message.studentTestLog.checksumReport" />
				</html:link>
				<%} else {%>
					-
				<%}%>
			</td>
		</tr>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

</table>
 --%>