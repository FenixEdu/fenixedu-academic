<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<bean:define id="hoursPattern">HH : mm</bean:define>
<bean:define id="infoLessons" name="infoLessons"/>
<bean:define id="registrationOID" name="registrationOID"/>
<bean:define id="classId" name="selectedSchoolClass" property="externalId"/>
<bean:define id="infoClasslessonsEndTime" name="infoClasslessonsEndTime"/>
<bean:define id="infoLessonsEndTime" name="infoLessonsEndTime"/>
<bean:define id="infoClasslessons" name="infoClasslessons"/>	

<h2><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.title" /></h2>

<div class="infoop2">		
	<ul>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.instructions1"/> <img src="<%= request.getContextPath() + "/images/add1.gif" %>" alt="<bean:message bundle="STUDENT_RESOURCES"  key="add1" />" /> <bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.instructions2" /> <img src="<%= request.getContextPath() + "/images/remove1.gif" %>" alt="<bean:message bundle="STUDENT_RESOURCES"  key="remove1" />" /> <bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.instructions3" /></li>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.showShiftsToEnroll.instructions4"/></li>
	</ul>
</div>

<logic:present name="executionCourse">
	<p class="mbottom05"><bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.visibleCourse"/>: <strong><bean:write name="executionCourse" property="nome"/></strong></p>
	<bean:define id="link"><bean:message bundle="STUDENT_RESOURCES"  key="link.shift.enrolement.edit"/></bean:define>
	<p class="mtop05"><bean:message bundle="STUDENT_RESOURCES"  key="message.showShiftsToEnroll.showAllCourses"/>: <html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;registrationOID=" + registrationOID.toString() %>"><bean:message bundle="STUDENT_RESOURCES" key="link.student.seeAllClasses"/></html:link></p>
</logic:present>


<h3 class="mtop15"><bean:message bundle="STUDENT_RESOURCES"  key="label.class" /> <bean:write name="selectedSchoolClass" property="nome"/></h3>

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
			endTime="<%= infoClasslessonsEndTime.toString() %>" action="addRAM" />
	</logic:present>

	<logic:notPresent name="executionCourse">
		<app:gerarHorario name="infoClasslessons"
			type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
			studentID="<%= registrationOID.toString() %>"
			application="<%= request.getContextPath() %>"
			classID="<%= classId.toString() %>"
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
			endTime="<%= infoLessonsEndTime.toString() %>" action="removeRAM" />
	</logic:present>

	<logic:notPresent name="executionCourse">
		<app:gerarHorario name="infoLessons"
			type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
			studentID="<%= registrationOID.toString() %>"
			application="<%= request.getContextPath() %>"
			classID="<%= classId.toString() %>"
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
			endTime="<%= infoClasslessonsEndTime.toString() %>" action="add" />
	</logic:present>

	<logic:notPresent name="executionCourse">
		<app:gerarHorario name="infoClasslessons"
			type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
			studentID="<%= registrationOID.toString() %>"
			application="<%= request.getContextPath() %>"
			classID="<%= classId.toString() %>"
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
			endTime="<%= infoLessonsEndTime.toString() %>" action="remove" />
	</logic:present>

	<logic:notPresent name="executionCourse">
		<app:gerarHorario name="infoLessons"
			type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>"
			studentID="<%= registrationOID.toString() %>"
			application="<%= request.getContextPath() %>"
			classID="<%= classId.toString() %>"
			endTime="<%= infoLessonsEndTime.toString() %>" action="remove" />
	</logic:notPresent>
</logic:notPresent>



<ul>
	<li><html:link page="<%="/studentShiftEnrollmentManager.do?method=start&registrationOID=" + registrationOID%>"><strong><bean:message bundle="STUDENT_RESOURCES"  key="button.finish" /></strong></html:link></li>
</ul>
