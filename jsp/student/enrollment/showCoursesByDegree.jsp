<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<logic:notPresent name="registration" >
	<span class="error"><!-- Error messages go here --><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>

<logic:present name="registration" >

<div class="center" >
	<h2><bean:message key="title.student.shift.enrollment" /></h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	

	<p class="left">Aqui pode, <strong>a título condicional</strong>, escolher disciplinas em que não se encontra inscrito curricularmente  mas nas quais pretende efectuar reserva de turma/turnos. Por exemplo, sobre as seguintes condições:</p>
	
	<ul class="left" style="margin-left: 4em">
	<li>Alunos Externos</li>
	<li>Melhorias de Nota</li>
	<li>Alunos inscritos em Época Especial 2003/2004</li>
	<li>Alunos cuja inscrição é efectuada pelo Coordenador de Licenciatura/Tutor</li>
	<li>Alunos com processos de Equivalência em curso</li>
	</ul>

<div class="infoselected left">
<p><bean:message key="message.warning.student.enrolmentClasses" /> <html:link page="<%= "/studentEnrollmentManagement.do?method=prepare" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link></p>
</div>

	<ul class="left">
		<li><bean:message key="message.student.shiftEnrollment" /></li>
	</ul>


<bean:define id="registrationToEnrol" name="registration" property="idInternal" />

<logic:present name="selectedExecutionDegree">

	<bean:define id="degreeSelected" name="selectedExecutionDegree" property="idInternal" />
	<logic:present name="executionDegrees">	

		<table class="width100">
		<tr>
		<td class="infotable" style="width: 50%; vertical-align: bottom;">
		<div style="overflow: hidden;">		
			<html:form action="/studentShiftEnrollmentManager" >

				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="start" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationOID" property="registrationOID" value="<%= registrationToEnrol.toString() %>" />
				
				<logic:present name="selectCourses">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectCourses" property="selectCourses" value="<%= pageContext.findAttribute("selectCourses").toString() %>" />
				</logic:present>
				
				<p style="text-align:left;margin-bottom:0px"><b><bean:message key="label.chooseCourses" />:</b></p>			
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degree" property="degree" styleClass="degrees" size="1" onchange="this.form.method.value='start'; this.form.submit();">
					<html:optionsCollection name="executionDegrees"/>
				</html:select>
				
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</html:form>
			
			<html:form action="/studentShiftEnrollmentManagerLoockup">
			
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationOID" property="registrationOID" value="<%= registrationToEnrol.toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree" value="<%= degreeSelected.toString() %>" />
				
				<logic:present name="selectCourses">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectCourses" property="selectCourses" value="<%= pageContext.findAttribute("selectCourses").toString() %>" />
				</logic:present>
		
				<p style="text-align:left;margin-bottom:0px"><b><bean:message key="label.degreeSelected.courses" />:</b></p>
				
				<logic:notPresent name="attendingExecutionCourses">
					<bean:define id="wantedCoursesSize" value="0"/>
				</logic:notPresent>
				<logic:present name="attendingExecutionCourses">
					<bean:size id="wantedCoursesSize" name="attendingExecutionCourses"/>	
					<bean:define id="attendingCourses" name="attendingExecutionCourses"/>
				</logic:present>
				
				<logic:present name="executionCoursesFromExecutionDegree">
					<bean:define id="executionCourses" name="executionCoursesFromExecutionDegree" />
					
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.wantedCourse" property="wantedCourse" size="10" styleClass="courseEnroll">
						<html:options collection="executionCourses" labelProperty="nome" property="idInternal"/>
					</html:select>
					
					<p style="text-align:center;margin-top:5px">
					<logic:lessThan name="wantedCoursesSize" value="8">			
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" style="width:13em">
							<bean:message key="button.addCourse"/>
						</html:submit>
					</logic:lessThan>
					<logic:greaterEqual name="wantedCoursesSize" value="8">
						<br />
						<span class="error"><!-- Error messages go here --><bean:message key="message.maximum.number.curricular.courses.to.enroll" arg0="8"/></span>
					</logic:greaterEqual>		
				</p>
		</div>
		</td>

					<td class="infotable" style="width: 50%; vertical-align: bottom;">
						<div style="overflow: hidden;">
							<p style="text-align:left; margin-bottom:0px"><b><bean:message key="label.attendCourses" />:</b></p>
							<logic:present name="attendingCourses">
								<html:select bundle="HTMLALT_RESOURCES" altKey="select.removedCourse" property="removedCourse" size="10" styleClass="courseEnroll">
									<html:options collection="attendingCourses" labelProperty="nome"  property="idInternal"/>
								</html:select>
								<logic:notEqual name="wantedCoursesSize" value="0">
									<p style="text-align:center;margin-top:5px"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" style="width:13em" ><bean:message key="button.removeCourse"/></html:submit></p>
								</logic:notEqual>
							</logic:present>
							<logic:notPresent name="attendingCourses">
								<br />
								<span class="error"><!-- Error messages go here --><bean:message key="message.noStudentExecutionCourses" /></span>
							</logic:notPresent>
						</div>
					</td>
					</tr>
					</table>
		
					<br/>
					<br/>
						
					<logic:notEqual name="wantedCoursesSize" value="0">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
							<bean:message key="button.continue.enrolment"/>
						</html:submit>
					</logic:notEqual>	
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
						<bean:message key="button.exit.shift.enrollment"/>
					</html:submit>		
			
				</logic:present>
			</html:form>

	</logic:present>
</logic:present>

</div>
</logic:present>
