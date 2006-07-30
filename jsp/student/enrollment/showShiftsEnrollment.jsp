<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="message.student.shift.enrollment" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:notPresent name="infoShiftEnrollment" >
	<span class="error"><!-- Error messages go here --><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>
<logic:present name="infoShiftEnrollment" >
	<bean:define id="executionDegreeId" name="infoShiftEnrollment" property="infoExecutionDegree.idInternal" />
	<bean:define id="studentId" name="infoShiftEnrollment" property="infoStudent.idInternal" />



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
	<html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()%>"><strong><bean:message key="link.shift.enrollment.item1" /></strong></html:link>
	</li>
	
	<li>
	<html:link page="/studentTimeTable.do" target="_blank" >
	<strong><bean:message key="link.shift.enrollment.item2" /></strong>
	</html:link>
	</li>

	<li>
	<html:link href="<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>"><strong><bean:message key="link.shift.enrollment.item3" /></strong></html:link>
	<!-- <bean:message key="message.shift.enrollment.over.help" /> -->
	</li>
</ul>

<br />



<style>
table.special {
/*width: 60%;*/
border-collapse: collapse;
border: 2px solid #aaa;
}
table.special tr th {
width: 10%;
text-align: center;
border: 2px solid #aaa;
padding: 0.5em;
background-color: #eee;
}
table.special tr td {
/*text-align: center;*/
border: 1px solid #aaa;
margin: 1em;
padding: 0.4em;
background-color: #fff;
}
table.special tr td.disciplina {
width: 40%;
background-color: #fafafa;
}
table.special tr td.reservado {
background-color: #dfd;
text-align: center;
}
table.special tr td.reservar {
background-color: #fdd;
text-align: center;
padding: 0;
margin: 0;
}
table.special tr td.reservar a {
/*background-color: #faa;*/
/*color: #00f;*/
}
table.special tr td.reservar a:hover {
}
span.t_warning {
background-color: #000;
color: #ffc;
padding: 0.5em;
}
span.t_success {
background-color: #000;
color: #bdb;
padding: 0.5em;
}

.box1 {
border: 1px solid #ddc;
padding: 2%;
background-color: #f5f5e5;
width: 98%;
}
</style>


<div class="box1">

<logic:present name="infoShiftEnrollment" property="infoShiftEnrollment">
	<bean:define id="numberCourseUnenrolledShifts" name="infoShiftEnrollment" property="numberCourseUnenrolledShifts" />
	<logic:lessEqual name="numberCourseUnenrolledShifts" value="0">
	<p><span class="t_success"><strong><bean:message key="message.student.shiftEnrollment.confirmation" /></strong></span></p>
	</logic:lessEqual>
	
	<logic:greaterThan  name="numberCourseUnenrolledShifts" value="0">
	<p><span class="t_warning"><strong><bean:message key="message.student.shiftEnrollment.lacksCourses" arg0="<%= numberCourseUnenrolledShifts.toString()%>"/></strong></span></p>
	</logic:greaterThan >
</logic:present>	


<p><strong><bean:message key="message.shift.enrollement.curricular"/></strong></p>	
		<logic:notEmpty name="infoEnrolledNewShiftEnrollmentList">
		<table class="special">
			<tr>
				<th><bean:message key="label.curricular.course.name"/></th>
				<th title="Teórica"><bean:message key="label.shiftType.theoric"/></th>
				<th title="Prática"><bean:message key="label.shiftType.pratic"/></th>
				<th title="Laboratório"><bean:message key="label.shiftType.laboratory"/></th>
				<th title="Teórico-prática"><bean:message key="label.shiftType.theoricoPratic"/></th>
			</tr>			
			<logic:iterate id="infoNewShiftEnrollment" name="infoEnrolledNewShiftEnrollmentList" 
					type="net.sourceforge.fenixedu.dataTransferObject.InfoNewShiftEnrollment">
				<bean:define id="executionCourseID" name="infoNewShiftEnrollment" property="infoExecutionCourse.idInternal"/>
				<tr>
					<td class="disciplina">
						<bean:write name="infoNewShiftEnrollment" property="infoExecutionCourse.nome"/> - 
						<bean:write name="infoNewShiftEnrollment" property="infoExecutionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="theoricType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="theoricShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="theoricShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="praticType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="praticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="praticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="laboratoryType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="laboratoryShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="laboratoryShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="theoricoPraticType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="theoricoPraticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="theoricoPraticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="theoricoPraticType">
						<td></td>
					</logic:empty>																				
				</tr>														
			</logic:iterate>
			</table>
		</logic:notEmpty>


				
		<logic:notEmpty name="infoNotEnrolledNewShiftEnrollmentList">
		<p><strong><bean:message key="message.shift.enrollement.extra"/></strong></p>
		<table class="special">
			<tr>
				<th><bean:message key="label.curricular.course.name"/></h>
				<th title="Teórica"><bean:message key="label.shiftType.theoric"/></th>
				<th title="Prática"><bean:message key="label.shiftType.pratic"/></th>
				<th title="Laboratório"><bean:message key="label.shiftType.laboratory"/></th>
				<th title="Teórico-prática"><bean:message key="label.shiftType.theoricoPratic"/></th>
			</tr>			
			<logic:iterate id="infoNewShiftEnrollment" name="infoNotEnrolledNewShiftEnrollmentList" 
					type="net.sourceforge.fenixedu.dataTransferObject.InfoNewShiftEnrollment">
				<bean:define id="executionCourseID" name="infoNewShiftEnrollment" property="infoExecutionCourse.idInternal"/>
				<tr>
					<td class="disciplina">
						<bean:write name="infoNewShiftEnrollment" property="infoExecutionCourse.nome"/> - 
						<bean:write name="infoNewShiftEnrollment" property="infoExecutionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="theoricType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="theoricShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="theoricShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="praticType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="praticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="praticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="laboratoryType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="laboratoryShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="laboratoryShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="infoNewShiftEnrollment" property="theoricoPraticType">
						<logic:notEmpty name="infoNewShiftEnrollment" property="theoricoPraticShift">
							<td class="reservado"><bean:message key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="infoNewShiftEnrollment" property="theoricoPraticShift">
							<td class="reservar"><html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" 
							+ pageContext.findAttribute("studentId").toString() + "&amp;executionCourseID=" + executionCourseID %>">
							<bean:message key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoNewShiftEnrollment" property="theoricoPraticType">
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
<logic:present name="infoShiftEnrollment" property="infoShiftEnrollment">	
<table style="width: 100%;">
			<bean:define id="elem" value="" type="java.lang.String"/>
			<logic:iterate id="infoShift" name="infoShiftEnrollment" property="infoShiftEnrollment" type="net.sourceforge.fenixedu.dataTransferObject.InfoShift">
				<%-- COURSES --%>
				<logic:present name="elem">
					<logic:notEqual name="elem" value="<%=infoShift.getInfoDisciplinaExecucao().getNome()%>">
						<tr>
							<td colspan='6'>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>	
						<tr>
							<td class="listClasses-subheader" style="text-align:left;background:#4F82B5" colspan='6' >
								<bean:write name="infoShift" property="infoDisciplinaExecucao.nome" />
							</td>
						</tr>	
					</logic:notEqual>
				</logic:present>
				<%-- SHIFTS --%>
				<tr>
					<th class="listClasses-header" style="text-align:left" colspan='6'>
						<bean:message key="property.turno" />:</b>&nbsp;
						<bean:write name="infoShift" property="nome" />
						<bean:define id="infoShiftId" name="infoShift" property="idInternal" />
						-
						<html:link page="<%= "/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&amp;studentId="
													+ pageContext.findAttribute("studentId").toString()
													+ "&amp;shiftId="
													+ pageContext.findAttribute("infoShiftId").toString()
													%>">
							<bean:message key="link.unenrole.shift" />
						</html:link>
					</th>
				</tr>

				<%-- LESSONS --%>
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
					<tr>
						<td style="text-align:center">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td style="text-align:center">
							<bean:write name="infoLesson" property="tipo" />								
						</td>
						<td style="text-align:center">
							<bean:write name="infoLesson" property="diaSemana" />
						</td>
						<td style="text-align:center">
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="inicio.timeInMillis" />
							</dt:format>								
							&nbsp;-&nbsp;
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="fim.timeInMillis" />
							</dt:format>
						</td>
						<td style="text-align:center">
							<logic:notEmpty name="infoLesson" property="infoSala.nome">
								<bean:write name="infoLesson" property="infoSala.nome" />
							</logic:notEmpty>	
						</td>
					</tr>			
				</logic:iterate>
				<bean:define id="elem" name="infoShift" property="infoDisciplinaExecucao.nome" type="java.lang.String"/> 
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="infoShiftEnrollment" property="infoShiftEnrollment">
			<logic:empty name="infoShiftEnrollment" property="infoShiftEnrollment">
				<tr>
					<td>
						<span class="error"><!-- Error messages go here --><bean:message key="message.warning.student.notYet.enroll" /></span>	
					</td>
				</tr>				
			</logic:empty>
		</logic:notPresent>
</table>

</div>

</logic:present>