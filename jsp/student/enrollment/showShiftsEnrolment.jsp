<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p align="left">
	<bean:message key="message.student.shiftEnrollment" />
</p>

<logic:notPresent name="infoShiftEnrollment" >
	<span class="error"><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>
<logic:present name="infoShiftEnrollment" >
	<bean:define id="executionDegreeId" name="infoShiftEnrollment" property="infoExecutionDegree.idInternal" />
	<bean:define id="studentId" name="infoShiftEnrollment" property="infoStudent.idInternal" />

	<html:link page="/studentShiftEnrolmentManager.do?method=start"><bean:message key="link.shift.chooseCourses" /></html:link>
	<br />

	<html:link page="<%= "/studentShiftEnrolmentManagerLoockup.do?method=proceedToShiftEnrolment&amp;studentId=" + pageContext.fincAttribute("studentId").toString()%>"><bean:message key="link.shift.enrolement.edit" /></html:link>

	<logic:present name="infoShiftEnrollment" property="infoShiftEnrollment">
		<table>
		<logic:iterate id="infoShift" name="infoShiftEnrollment" property="infoShiftEnrollment">
			<tr>
				<td>
					<b><bean:message key="label.course" />:</b>&nbsp;
					<bean:write name="infoShift" property="infoDisciplinaExecucao.nome" />
				</td>
			</tr>	
			<tr>
				<td>
					<bean:message key="property.turno" />:</b>&nbsp;
					<bean:write name="infoShift" property="nome" />
				</td>
			</tr>
			<tr>
				<td>
					<b><bean:message key="property.lessons" />:</b>&nbsp;
					<ul>
						<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
							<li>
								<bean:write name="infoLesson" property="diaSemana" />
								&nbsp;
								<bean:write name="infoLesson" property="inicio" />
								&nbsp;-&nbsp;
								<bean:write name="infoLesson" property="fim" />
								&nbsp;
								<bean:write name="infoLesson" property="tipo" />								
								&nbsp;
								<bean:write name="infoLesson" property="infoSala.nome" />
							</li>
						</logic:iterate>
					</ul>
				</td>
			</tr>			
		</logic:iterate>
		</table>
	</logic:present>
</logic:present>


