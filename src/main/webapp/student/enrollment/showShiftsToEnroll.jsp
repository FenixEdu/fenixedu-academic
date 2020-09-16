<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="org.fenixedu.academic.servlet.taglib.sop.v3.TimeTableType" %>

<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />

<bean:define id="hoursPattern">HH : mm</bean:define>
<bean:define id="infoLessons" name="infoLessons"/>
<bean:define id="registrationOID" name="registrationOID"/>
<bean:define id="classId" name="selectedSchoolClass" property="externalId"/>
<bean:define id="infoClasslessonsEndTime" name="infoClasslessonsEndTime"/>
<bean:define id="infoLessonsEndTime" name="infoLessonsEndTime"/>
<bean:define id="infoClasslessons" name="infoClasslessons"/>
<bean:define id="executionSemesterID" name="executionSemesterID" type="java.lang.String"/>


<div class="col-md-12">
<h2><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.title" /></h2>

<div class="infoop2">		
	<ul>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.instructions1"/> <img src="<%= request.getContextPath() + "/images/add1.gif" %>" alt="<bean:message bundle="STUDENT_RESOURCES"  key="add1" />" /> <bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.instructions2" /> <img src="<%= request.getContextPath() + "/images/remove1.gif" %>" alt="<bean:message bundle="STUDENT_RESOURCES"  key="remove1" />" /> <bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.instructions3" /></li>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.instructions4"/></li>
	</ul>
</div>



<div class="row">
<div class="col-md-8 col-lg-10">
</div>
<div class="col-lg-2 col-md-4 col-sm-12">
	<div class="jumbotron" style="padding-left: 20px; padding-right: 20px">
		<%@include file="listClasses.jsp" %>
	</div>
</div>
</div>


<logic:present name="executionCourse">
	<p class="mbottom05"><bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.visibleCourse"/>: <strong><bean:write name="executionCourse" property="nome"/></strong></p>
	<bean:define id="link">proceedToShiftEnrolment</bean:define>
	<p class="mtop05"><bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.showAllCourses"/>: <html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" + registrationOID.toString() + "&amp;executionSemesterID=" + executionSemesterID %>"><bean:message bundle="STUDENT_RESOURCES" key="link.student.seeAllClasses"/></html:link></p>
</logic:present>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" filter="false"/></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<h3 class="mtop15"><bean:message bundle="STUDENT_RESOURCES"  key="label.class" /> <bean:write name="selectedSchoolClass" property="nome"/></h3>
<div style="overflow: auto;">
	<logic:present name="ram">
	
	
		<logic:present name="executionCourse">
			<bean:define id="executionCourseID" name="executionCourse"
				property="externalId" />
			<app:gerarHorario name="infoClasslessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionCourseID="<%= executionCourseID.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoClasslessonsEndTime.toString() %>" action="addRAM" />
		</logic:present>
	
		<logic:notPresent name="executionCourse">
			<app:gerarHorario name="infoClasslessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoClasslessonsEndTime.toString() %>" action="addRAM" />
		</logic:notPresent>
	
	
		<p class="mtop3"><strong><bean:message
			bundle="STUDENT_RESOURCES"
			key="message.showShiftsToEnroll.actual.timetable" /></strong></p>
	
	
		<logic:present name="executionCourse">
			<bean:define id="executionCourseID" name="executionCourse"
				property="externalId" />
			<app:gerarHorario name="infoLessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionCourseID="<%= executionCourseID.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoLessonsEndTime.toString() %>" action="removeRAM" />
		</logic:present>
	
		<logic:notPresent name="executionCourse">
			<app:gerarHorario name="infoLessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoLessonsEndTime.toString() %>" action="removeRAM" />
		</logic:notPresent>
	</logic:present>
	
	<logic:notPresent name="ram">
	
	
		<logic:present name="executionCourse">
			<bean:define id="executionCourseID" name="executionCourse"
				property="externalId" />
			<app:gerarHorario name="infoClasslessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionCourseID="<%= executionCourseID.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoClasslessonsEndTime.toString() %>" action="add" />
		</logic:present>
	
		<logic:notPresent name="executionCourse">
			<app:gerarHorario name="infoClasslessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoClasslessonsEndTime.toString() %>" action="add" />
		</logic:notPresent>
	
	
		<p class="mtop3"><strong><bean:message
			bundle="STUDENT_RESOURCES"
			key="message.showShiftsToEnroll.actual.timetable" /></strong></p>
	
	
		<logic:present name="executionCourse">
			<bean:define id="executionCourseID" name="executionCourse"
				property="externalId" />
			<app:gerarHorario name="infoLessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionCourseID="<%= executionCourseID.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoLessonsEndTime.toString() %>" action="remove" />
		</logic:present>
	
		<logic:notPresent name="executionCourse">
			<app:gerarHorario name="infoLessons"
				type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
				studentID="<%= registrationOID.toString() %>"
				application="<%= request.getContextPath() %>"
				classID="<%= classId.toString() %>"
				executionSemesterID="<%= executionSemesterID %>"
				endTime="<%= infoLessonsEndTime.toString() %>" action="remove" />
		</logic:notPresent>
	</logic:notPresent>
</div>


<br/>
<ul>
	<li><html:link page="<%="/studentShiftEnrollmentManager.do?method=start&registrationOID=" + registrationOID + "&amp;executionSemesterID=" + executionSemesterID %>"><strong><bean:message bundle="STUDENT_RESOURCES"  key="button.finish" /></strong></html:link></li>
</ul>
</div>