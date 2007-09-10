<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>




<logic:present role="DELEGATE">
	<ul>
		<%--<li>
			<html:link page="/readCurricularCourses.do">
				<bean:message key="link.delegate.coursesEvalutation" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>--%>
		<li class="navheader">
			<bean:message bundle="DELEGATES_RESOURCES" key="label.delegates.consult"/>
		</li>
		<li>
			<html:link page="/findDelegates.do?method=prepare&searchByName=true">
				<bean:message key="link.delegates" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/viewStudents.do?method=prepareShowStudentsByCurricularCourse">
				<bean:message key="link.students" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
		
		<logic:present name="userView" property="person.student">
			<bean:define id="student" name="userView" property="person.student" />
			<bean:define id="degree" name="student" property="lastActiveRegistration.degree" />
			<bean:define id="degreeId" name="degree" property="idInternal" />
			<li>
				<html:link page="<%= "/evaluationsForDelegates.faces?degreeID=" + degreeId%>">
					<bean:message key="link.evaluations" bundle="DELEGATES_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<br />
		<li class="navheader">
			<bean:message bundle="DELEGATES_RESOURCES" key="label.delegates.comunication"/>
		</li>
		<li>
			<html:link page="<%= "/sendEmailToDelegates.do?method=prepare"%>">
				<bean:message key="link.sendEmailToDelegates" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/sendEmailToDelegateStudents.do?method=prepare">
				<bean:message key="link.sendEmailToStudents" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</logic:present>