<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<br/>
<bean:define id="link"><%= request.getContextPath() %>/dotIstPortal.do?prefix=/student&amp;page=/index.do</bean:define>
<html:link href='<%= link %>'><b>
		Sair do processo de inscriï¿½ï¿½o
	</b>
</html:link>
<br/>
<br/>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<logic:present name="infoStudentShiftEnrolment" property="allowedClasses">
<br />	
	<table width="70%" align="center">
		<tr>
			<td colspan="3"><h2 class="redtxt">Informaï¿½ï¿½es de utilizaï¿½ï¿½o:</h2><p>Na lista abaixo estï¿½o as turmas que dispï¿½em de vagas nas disciplinas a que pretende frequentar. Para facilitar a escolha, encontram-se ordenadas, por ordem decrescente, de nï¿½mero de disciplinas que se pretende frequentar. Seleccione, por favor, a turma desejada.</p><h2>Turmas</h2></td>
		</tr>
		<tr>
			<th class="listClasses-header">Ano</th>
			<th class="listClasses-header">Turma</th>
			<th class="listClasses-header">Horï¿½rio</th>
		</tr>
		<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" >
			<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
			<tr>
				<td class="listClasses"><bean:write name="curricularYear"/>ï¿½ Ano</td>
				<td class="listClasses">
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" transaction="true">
					<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page="/studentShiftEnrolmentManager.do?method=viewClassTimeTable" paramId="classId" paramName="infoClass" paramProperty="idInternal" target="_blank" >Ver Horï¿½rio</html:link>
				</td>
				<%--<td>
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" transaction="true">
						Seleccionar Turma
					</html:link>
				</td>--%>
			</tr>
		</logic:iterate>
	</table>
<br />
</logic:present>
<table width="70%" align="center">
	<tr>
		<td><h2>Disciplinas que pretende frequentar:</h2></td>
	</tr>
	<tr>
		<td class="listClasses" style="text-align: left; padding: 10px;">
			<logic:iterate name="infoStudentShiftEnrolment"  id="infoExecutionCourse" property="enrolledExecutionCourses">
				<bean:write name="infoExecutionCourse" property="nome"/><br />
			</logic:iterate>
		</td>	
	</tr>
</table>
<br />
	<logic:notPresent name="infoStudentShiftEnrolment" property="lessons">Não estï¿½ inscrito em nenhum turno...</logic:notPresent>
	<logic:present name="infoStudentShiftEnrolment" property="lessons">
<table width="70%" align="center">
	<tr>
		<td colspan="3"><h2>Turnos em que estï¿½ inscrito:</h2></td>
	</tr>
		<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment"  type="net.sourceforge.fenixedu.dataTransferObject.InfoShift">
	<tr>
		<td class="listClasses"><bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/></td>
		<td class="listClasses"><bean:write name="enroledShift" property="tipo.fullNameTipoAula"/></td>
		<td class="listClasses">
				<logic:iterate id="lesson" name="enroledShift" property="infoLessons">
						&nbsp;
								<bean:write name="lesson" property="diaSemana"/>
								das
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
								atï¿½ as 
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
								<logic:notEmpty name="lesson" property="infoSala.nome">
									na sala
									<bean:write name="lesson" property="infoSala.nome"/>
								</logic:notEmpty>	
						<br/>
							</logic:iterate> 
		</td>
	</tr>					
		</logic:iterate>
</table>
<br />
<bean:define id="infoLessons" name="infoStudentShiftEnrolment" property="lessons" />
<div align="center"><h2>O Meu Horï¿½rio</h2></div>
<div align="center"><app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/></div><br />
</logic:present>
<logic:notPresent name="infoStudentShiftEnrolment" property="allowedClasses">
<div class="error" align="center">Não existem turmas disponï¿½veis.</div>
</logic:notPresent>
<br />

