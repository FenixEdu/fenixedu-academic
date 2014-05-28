<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>

<logic:notPresent name="registration" >
	<span class="error"><!-- Error messages go here --><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>

<logic:present name="registration" >

<div>
	<h2 class="acenter"><bean:message key="title.student.shift.enrollment" /></h2>
	
	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

	<div class="infoop2">
		<p><bean:message key="message.warning.student.enrolmentClasses" /> <html:link page="<%= "/studentEnrollmentManagement.do?method=prepare" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link></p>
	</div>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<p>Aqui pode, <strong>a título condicional</strong>, escolher disciplinas em que não se encontra inscrito curricularmente  mas nas quais pretende efectuar reserva de turma/turnos. Por exemplo, sobre as seguintes condições:</p>
	
	<ul style="margin-left: 4em">
		<li>Alunos Externos</li>
		<li>Melhorias de Nota</li>
		<li>Alunos com processos de Equivalência em curso</li>
	</ul>

	<ul>
		<li><bean:message key="message.student.shiftEnrollment" /></li>
	</ul>


<bean:define id="registrationToEnrol" name="registration" property="externalId" />

<logic:present name="selectedExecutionDegree">

	<bean:define id="degreeSelected" name="selectedExecutionDegree" property="externalId" />
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
			</html:form>
			
			<html:form action="/studentShiftEnrollmentManagerLookup">

				<input type="hidden" name="method" id="mainMethod" />
			
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
						<html:options collection="executionCourses" labelProperty="nome" property="externalId"/>
					</html:select>
					
					<p style="text-align:center;margin-top:5px">
					<logic:lessThan name="wantedCoursesSize" value="8">			
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" style="width:13em"
							onclick="document.getElementById('mainMethod').value='addCourses'">
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
									<html:options collection="attendingCourses" labelProperty="nome"  property="externalId"/>
								</html:select>
								<logic:notEqual name="wantedCoursesSize" value="0">
									<p style="text-align:center;margin-top:5px"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" style="width:13em" onclick="document.getElementById('mainMethod').value='removeCourses'"><bean:message key="button.removeCourse"/></html:submit></p>
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
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton"
						 onclick="document.getElementById('mainMethod').value='prepareStartViewWarning'">
							<bean:message key="button.continue.enrolment"/>
						</html:submit>
					</logic:notEqual>
				</logic:present>
			</html:form>

	</logic:present>
</logic:present>

</div>
</logic:present>
