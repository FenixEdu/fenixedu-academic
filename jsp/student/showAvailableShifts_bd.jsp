<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<table width="70%" align="center">
	<tr>
		<td align="left">
			<bean:write name="infoStudentShiftEnrolment" property="infoStudent.infoPerson.nome"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<logic:present name="infoStudentShiftEnrolment">
				<!-- aqui falta scope ? -->
				<h3>
					Turnos onde estás inscrito:
				</h3>
				<logic:iterate id="enroledShift" name="infoStudentShiftEnrolment"  property="currentEnrolment" offset="0" length="1" type="DataBeans.InfoShift">
					<bean:define id="infoExecutionCourseOID" value="<%= enroledShift.getInfoDisciplinaExecucao().getIdInternal().toString() %>"/>
					<h4>
						<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
					</h4>
				</logic:iterate>
				<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment" offset="1" type="DataBeans.InfoShift">
					<bean:define id="actualInfoExecutionCourseOID" value="<%= enroledShift.getInfoDisciplinaExecucao().getIdInternal().toString() %>" />
					<logic:notEqual name="infoExecutionCourseOID" value="<%= actualInfoExecutionCourseOID %>" >
						<bean:define id="infoExecutionCourseOID" value="<%= enroledShift.getInfoDisciplinaExecucao().getIdInternal().toString() %>" />
						<h4>
							<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
						</h4>
					</logic:notEqual>
					<blockquote>
						<b>
							Nome:
						</b>
						<bean:write name="enroledShift" property="nome"/>
						<b>
							Tipo:
						</b>
						<bean:write name="enroledShift" property="tipo.siglaTipoAula"/>
						<br />
						<blockquote>
							<logic:iterate id="lesson" name="enroledShift" property="infoLessons">
								<bean:write name="lesson" property="diaSemana"/>
								das
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
								até as 
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
								<br />
							</logic:iterate>
						</blockquote>
					</blockquote>
				</logic:iterate>
				<br />
				<h3>
					Turnos onde te podes inscrever:
				</h3>
				<html:form action="studentShiftEnrolmentManager">
					<html:hidden property="method" value="validateAndConfirmShiftEnrolment"/>
					<bean:define id="index" value="0"/>
					<logic:iterate name="infoStudentShiftEnrolment"  id="list" property="dividedList" indexId="courseIndex">
						<br />
						Disciplina:
						<bean:write name="list" property="type"/>
						<br />
						Turnos
						<logic:iterate name="list" id="sublist" property="list" indexId="groupIndex">
						<br />
							Tipo:
							<bean:write name="sublist" property="type"/>
							<br />
							<logic:iterate name="sublist" id="shiftWithLessons" property="list" >
								
								<bean:define id="shift" name="shiftWithLessons" property="infoShift" />
								&nbsp
								<html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" />
								<bean:write name="shiftWithLessons" property="infoShift.nome"/>
								: 
								(Capacidade
								<bean:write name="shiftWithLessons" property="infoShift.availabilityFinal"/>
								)
								<br />
								<blockquote>
									<logic:iterate id="lesson" name="shiftWithLessons" property="infoLessons">
										<bean:write name="lesson" property="diaSemana"/>
										das
										<dt:format pattern="HH:mm">
											<bean:write name="lesson" property="inicio.time.time"/>
										</dt:format>
										até as 
										<dt:format pattern="HH:mm">
											<bean:write name="lesson" property="fim.time.time"/>
										</dt:format>
										<br />
									</logic:iterate>
								</blockquote>
							</logic:iterate>
					<bean:define id="index" value="<%=  (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						</logic:iterate>
					</logic:iterate>
					<html:submit value="Inscrever"/>
				</html:form>
			</logic:present>
		</td>
	</tr>
</table>