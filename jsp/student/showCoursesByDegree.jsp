<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:notPresent name="infoShiftEnrollment" >
	<span class="error"><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>
<logic:present name="infoShiftEnrollment" >
<div  align="center" >
	<span class="error"><html:errors /></span>
	<br />
	<p align="left">
		<span class="error">ATENÇÃO: A INSCRIÇÃO EM TURNOS/TURMAS NÃO SUBSTITUI A INSCRIÇÃO EM DISCIPLINAS EFECTUADA NA <a href="http://secreta.ist.utl.pt/secretaria/" target="_blank">SECRETARIA</a>.</span>
		<h2 class="redtxt" style="text-align:center">
			<bean:message key="label.useInformation" />:
		</h2>
		<bean:message key="message.student.shiftEnrollment" />
	</p>

	<bean:define id="studentNumberToEnrollment" name="infoShiftEnrollment" property="infoStudent.number" />
	<div class="infotable" style="width:95%;">		
	<html:form action="/studentShiftEnrolmentManager" >
		<html:hidden property="method" value="start" />
		<html:hidden property="studentNumber" value="<%= studentNumberToEnrollment.toString() %>" />
		<p style="text-align:left;margin-bottom:0px">
			<b><bean:message key="label.chooseCourses" />:</b>
		</p>			
		<html:select property="degree" styleClass="degrees" size="1" onchange="this.form.method.value='start'; this.form.submit();" >
			<html:optionsCollection name="infoShiftEnrollment" property="infoExecutionDegreesLabelsList"/>				
		</html:select>
	</html:form>
	<html:form action="/studentShiftEnrolmentManagerLoockup" >
		<html:hidden property="studentNumber" value="<%= studentNumberToEnrollment.toString() %>" />
		<p style="text-align:left;margin-bottom:0px">
			<b><bean:message key="label.degreeSelected.courses" />:</b>
		</p>
		
		<logic:present name="infoShiftEnrollment" property="infoAttendingCourses">
			<bean:size id="wantedCoursesSize" name="infoShiftEnrollment" property="infoAttendingCourses"/>	
			<bean:define id="attendingCourses" name="infoShiftEnrollment" property="infoAttendingCourses"/>
		</logic:present>
		<logic:notPresent name="infoShiftEnrollment" property="infoAttendingCourses">
			<bean:define id="wantedCoursesSize" value="0"/>
		</logic:notPresent>
		
		<bean:define id="executionCourses" name="infoShiftEnrollment" property="infoExecutionCoursesList" />
		<html:select property="wantedCourse" size="8" styleClass="courseEnroll">
			<html:options collection="executionCourses" labelProperty="nome" property="idInternal"/>
		</html:select>
		<p style="text-align:center;margin-top:1px">
			<logic:lessThan name="wantedCoursesSize" value="8">			
				<html:submit property="method" styleClass="inputbutton" style="width:100%">
					<bean:message key="button.addCourse"/>
				</html:submit>
			</logic:lessThan>
			<logic:greaterEqual name="wantedCoursesSize" value="8">
				<br />
				<span class="error"><bean:message key="message.maximum.number.curricular.courses.to.enroll" arg0="8"/></span>
			</logic:greaterEqual>		
		</p>
	</div>
	<br/>
	<br/>

	<div class="infotable" style="width:95%;">
		<p style="text-align:left; margin-bottom:0px">
			<b>
				<bean:message key="label.attendCourses" />:
			</b>
		</p>
		<logic:present name="attendingCourses">
			<html:select property="removedCourse" size="8" styleClass="courseEnroll">
				<html:options collection="attendingCourses" labelProperty="nome"  property="idInternal"/>
			</html:select>
			<logic:notEqual name="wantedCoursesSize" value="0">
				<p style="text-align:center;margin-top:1px">
					<html:submit property="method" styleClass="inputbutton" style="width:100%">
						<bean:message key="button.removeCourse"/>
					</html:submit>
				</p>
			</logic:notEqual>
		</logic:present>
		<logic:notPresent name="attendingCourses">
			<br />
			<span class="error"><bean:message key="message.noStudentExecutionCourses" /></span>
		</logic:notPresent>		

	</div>
	<br/>
	<br/>
	
			
	<logic:notEqual name="wantedCoursesSize" value="0">
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="button.continue.enrolment"/>
		</html:submit>
	</logic:notEqual>	
	<html:submit property="method" styleClass="inputbutton" style="width:35%">
		<bean:message key="button.exit.enrollment"/>
	</html:submit>		
	</html:form>

</div>
</logic:present>
