<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>"/>




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

			<logic:present name="student" property="delegateFunction">
				<bean:define id="degree" name="student" property="delegateFunction.unit.degree" />
				<bean:define id="degreeId" name="degree" property="externalId" />
				<li>
					<html:link page="<%= "/evaluationsForDelegates.faces?degreeID=" + degreeId%>">
						<bean:message key="link.evaluations" bundle="DELEGATES_RESOURCES"/>
					</html:link>
					</li>
			</logic:present>
		</logic:present>

		<li class="navheader">
			<bean:message bundle="DELEGATES_RESOURCES" key="label.delegates.comunication"/>
		</li>
		<li>
			<html:link page="<%= "/sendEmailRedirect.do"%>">
				<bean:message key="link.sendEmailToDelegates" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/sendEmailToDelegateStudents.do?method=prepare">
				<bean:message key="link.sendEmailToStudents" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
		
		<li class="navheader"><bean:message key="label.participate" bundle="DELEGATES_RESOURCES"/></li>
		<li>
			<html:link page="/delegateInquiry.do?method=showCoursesToAnswerPage">
				<bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/>
			</html:link>
		</li>
					
	</ul>
</logic:present>