<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>

<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />


<center><h2>
	Turmas
</h2></center>

<logic:present name="infoStudentShiftEnrolment" property="allowedClasses">
	<table width="70%" align="center">
		<tr>
			<td class="listClasses-header">
				Ano
			</td>
			<td class="listClasses-header">
				Turma
			</td>
			<td class="listClasses-header">
				Horário
			</td>
			
		</tr>
		<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" >
			<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
			<tr>
				<td class="listClasses">
					<bean:write name="curricularYear"/>
					º Ano
				</td>
				<td class="listClasses">
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" transaction="true">
					<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page="/studentShiftEnrolmentManager.do?method=viewClassTimeTable" paramId="classId" paramName="infoClass" paramProperty="idInternal" target="_blank" >
						Ver Horário
					</html:link>
				</td>
				<%--<td>
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" transaction="true">
						Seleccionar Turma
					</html:link>
				</td>--%>
			</tr>
		</logic:iterate>
	</table>
	<br>
</logic:present>
<center><h2>
Disciplinas em que está inscrito
</h2></center>
<table width="70%" align="center">
<logic:iterate name="infoStudentShiftEnrolment"  id="infoExecutionCourse" property="enrolledExecutionCourses">
<tr><td class="listClasses">
<bean:write name="infoExecutionCourse" property="nome"/>
</td></tr>
</logic:iterate>
</table>
<logic:notPresent name="infoStudentShiftEnrolment" property="lessons">
	
	Não está inscrito em nenhum turno...
</logic:notPresent>
<logic:present name="infoStudentShiftEnrolment" property="lessons">
<center><h2>
		Turnos em que está inscrito:
	</h2></center>
<table width="70%" align="center">
		<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment" offset="1" type="DataBeans.InfoShift">
		<tr>
		<td class="listClasses">		
						<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
		</td>
		<td class="listClasses">
						<bean:write name="enroledShift" property="tipo.fullNameTipoAula"/>
		</td>
		<td class="listClasses">
						<logic:iterate id="lesson" name="enroledShift" property="infoLessons">
						&nbsp;
								<bean:write name="lesson" property="diaSemana"/>
								das
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
								até as 
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
						&nbsp;
							</logic:iterate> 
			</td></tr>					
				</logic:iterate>
</table> <br />
	<bean:define id="infoLessons" name="infoStudentShiftEnrolment" property="lessons" />
	<center><h2>
		O Meu Horário:
	</h2></center>
	<center><app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/></center>
</logic:present>
<logic:notPresent name="infoStudentShiftEnrolment" property="allowedClasses">
	Não existem turmas disponíveis.
</logic:notPresent>

