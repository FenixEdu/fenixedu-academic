<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html:xhtml/>

<h2><bean:message key="message.student.shift.enrollment" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<logic:notPresent name="student">
	<span class="error"><!-- Error messages go here --><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>

<logic:present name="student">

<bean:define id="studentId" name="student" property="idInternal" />

<div style="width: 65%">

	<div class="infoselected">
		<!-- <strong><bean:message key="label.attention" /></strong> -->
		<ul>
		<li><bean:message key="message.shift.enrollement.resume.item1"/> <html:link page="<%= "/warningFirst.do" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link></li>
		<li><bean:message key="message.shift.enrollement.resume.item2"/> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&selectCourses=true" %>"><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></li>
		</ul>
	</div>


	<ul>
		<li>
		<bean:define id="link"><bean:message key="link.shift.enrolement.edit"/></bean:define>
		<html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" + studentId.toString() %>"><strong><bean:message key="link.shift.enrollment.item1" /></strong></html:link>
		</li>
		
		<li>
		<html:link page="/studentTimeTable.do?method=showTimeTable" target="_blank" paramId="registrationId" paramName="studentId">
		<strong><bean:message key="link.shift.enrollment.item2" /></strong>
		</html:link>
		</li>
	
		<li>
		<html:link href="<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>"><strong><bean:message key="link.shift.enrollment.item3" /></strong></html:link>
		<!-- <bean:message key="message.shift.enrollment.over.help" /> -->
		</li>
	</ul>

	<br />


	<div class="box1">

	<logic:present name="studentShifts">
		<bean:define id="numberCourseUnenrolledShifts" name="numberOfExecutionCoursesHavingNotEnroledShifts"/>
		<logic:lessEqual name="numberCourseUnenrolledShifts" value="0">
			<p><span class="t_success"><strong><bean:message key="message.student.shiftEnrollment.confirmation" /></strong></span></p>
		</logic:lessEqual>
		
		<logic:greaterThan  name="numberCourseUnenrolledShifts" value="0">
			<p><span class="t_warning"><strong><bean:message key="message.student.shiftEnrollment.lacksCourses" arg0="<%= numberCourseUnenrolledShifts.toString() %>"/></strong></span></p>
		</logic:greaterThan >
	</logic:present>	


	<p><strong><bean:message key="message.shift.enrollement.curricular"/></strong></p>
	
		<logic:notEmpty name="shiftsToEnrolFromEnroledExecutionCourses">
		<table class="special">
			<tr>
				<th><bean:message key="label.curricular.course.name"/></th>
				<th title="Teórica"><bean:message key="label.shiftType.theoric"/></th>
				<th title="Prática"><bean:message key="label.shiftType.pratic"/></th>
				<th title="Laboratório"><bean:message key="label.shiftType.laboratory"/></th>
				<th title="Teórico-prática"><bean:message key="label.shiftType.theoricoPratic"/></th>
			</tr>			
			<logic:iterate id="shiftToEnrol" name="shiftsToEnrolFromEnroledExecutionCourses" type="net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol">
				<bean:define id="executionCourseID" name="shiftToEnrol" property="executionCourse.idInternal"/>
				<tr>
					<td class="disciplina">
						<bean:write name="shiftToEnrol" property="executionCourse.nome"/> - <bean:write name="shiftToEnrol" property="executionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricType">
						<logic:notEmpty name="shiftToEnrol" property="theoricShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="praticType">
						<logic:notEmpty name="shiftToEnrol" property="praticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="praticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="laboratoryType">
						<logic:notEmpty name="shiftToEnrol" property="laboratoryShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="laboratoryShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricoPraticType">
						<logic:notEmpty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricoPraticType">
						<td></td>
					</logic:empty>																				
				</tr>														
			</logic:iterate>
			</table>
		</logic:notEmpty>
	
					
		<logic:notEmpty name="shiftsToEnrolFromUnenroledExecutionCourses">
		<p><strong><bean:message key="message.shift.enrollement.extra"/></strong></p>
		<table class="special">
			<tr>
				<th><bean:message key="label.curricular.course.name"/></h>
				<th title="Teórica"><bean:message key="label.shiftType.theoric"/></th>
				<th title="Prática"><bean:message key="label.shiftType.pratic"/></th>
				<th title="Laboratório"><bean:message key="label.shiftType.laboratory"/></th>
				<th title="Teórico-prática"><bean:message key="label.shiftType.theoricoPratic"/></th>
			</tr>			
			<logic:iterate id="shiftToEnrol" name="shiftsToEnrolFromUnenroledExecutionCourses" type="net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol">
				<bean:define id="executionCourseID" name="shiftToEnrol" property="executionCourse.idInternal"/>
				<tr>
					<td class="disciplina">
						<bean:write name="shiftToEnrol" property="executionCourse.nome"/> - <bean:write name="shiftToEnrol" property="executionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricType">
						<logic:notEmpty name="shiftToEnrol" property="theoricShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="praticType">
						<logic:notEmpty name="shiftToEnrol" property="praticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="praticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="laboratoryType">
						<logic:notEmpty name="shiftToEnrol" property="laboratoryShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="laboratoryShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricoPraticType">
						<logic:notEmpty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ studentId.toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricoPraticType">
						<td></td>
					</logic:empty>																				
				</tr>														
			</logic:iterate>
			</table>
		</logic:notEmpty>
		
	<p><bean:message key="message.shift.enrollement.legend"/></p>
	
	</div>

	<br />
	<br />
	<strong><bean:message key="message.shift.enrollement.list"/></strong>
	
	<logic:present name="studentShifts">	
	<table style="width: 100%;">
		<bean:define id="elem" value="" type="java.lang.String"/>
		<logic:iterate id="shift" name="studentShifts" type="net.sourceforge.fenixedu.domain.Shift">
			<%-- COURSES --%>
			<logic:present name="elem">
				<logic:notEqual name="elem" value="<%=shift.getDisciplinaExecucao().getNome()%>">
					<tr>
						<td colspan='6'>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>	
					<tr>
						<td class="listClasses-subheader" style="text-align:left;background:#4F82B5" colspan='6' >
							<bean:write name="shift" property="disciplinaExecucao.nome" />
						</td>
					</tr>	
				</logic:notEqual>
			</logic:present>
			<%-- SHIFTS --%>
			<tr>
				<th class="listClasses-header" style="text-align:left" colspan='6'>
					<bean:message key="property.turno" />:</b>&nbsp;
					<bean:write name="shift" property="nome" />
					<bean:define id="shiftId" name="shift" property="idInternal" />
					-
					<html:link page="<%= "/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&amp;studentId="
												+ studentId.toString()
												+ "&amp;shiftId="
												+ shiftId.toString()
												%>">
						<bean:message key="link.unenrole.shift" />
					</html:link>
				</th>
			</tr>

			<%-- LESSONS --%>
			<logic:iterate id="lesson" name="shift" property="associatedLessons">
				<tr>
					<td style="text-align:center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td style="text-align:center">
						<bean:write name="lesson" property="tipo" />								
					</td>
					<td style="text-align:center">
						<bean:write name="lesson" property="diaSemana" />
					</td>
					<td style="text-align:center">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="begin.time" />
						</dt:format>								
						&nbsp;-&nbsp;
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="end.time" />
						</dt:format>
					</td>
					<td style="text-align:center">
						<logic:notEmpty name="lesson" property="sala.name">
							<bean:write name="lesson" property="sala.name" />
						</logic:notEmpty>	
					</td>
				</tr>			
			</logic:iterate>
			<bean:define id="elem" name="shift" property="disciplinaExecucao.nome" type="java.lang.String"/> 
		</logic:iterate>
	</logic:present>
			
	<logic:notPresent name="studentShifts">
		<tr>
			<td>
				<span class="error"><!-- Error messages go here --><bean:message key="message.warning.student.notYet.enroll" /></span>	
			</td>
		</tr>
	</logic:notPresent>
	</table>

	</div>
	
</logic:present>
