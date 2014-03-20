<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="registrationOID" value="${registration.externalId}" />
<c:set var="hasExecutionCourse" value="${!empty executionCourse}" />

<h4 class="text-center"><bean:message key="link.shift.enrolment" bundle="STUDENT_RESOURCES" /></h4>

<c:if test="${hasExecutionCourse}">
	<h6 class="text-center">
		${executionCourse.name}<br />
		(<html:link page="/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment&registrationOID=${registrationOID}">
			<bean:message bundle="STUDENT_RESOURCES" key="link.student.seeAllClasses" />
		</html:link>)
	</h6>
</c:if>

<ul class="nav nav-pills nav-stacked">
	<c:forEach var="schoolClass" items="${schoolClassesToEnrol}">
		<li class="${schoolClass == selectedSchoolClass ? 'active': ''}">
			<c:if test="${hasExecutionCourse}">
				<html:link page="/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment&registrationOID=${registrationOID}&classId=${schoolClass.externalId}&executionCourseID=${executionCourse.externalId}">
					<bean:message key="label.class" />&nbsp;${schoolClass.nome}		
				</html:link>
			</c:if>
			
			<c:if test="${!hasExecutionCourse}">
				<html:link page="/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment&registrationOID=${registrationOID}&classId=${schoolClass.externalId}">
					<bean:message key="label.class" />&nbsp;${schoolClass.nome}	
				</html:link>
			</c:if>
		</li>
	</c:forEach> 
	<br />
</ul>

<div class="text-center">
	<html:link styleClass="btn btn-default" page="/studentShiftEnrollmentManager.do?method=start&registrationOID=${registrationOID}">
		« <bean:message key="button.back" bundle="STUDENT_RESOURCES" />
	</html:link>
</div>
