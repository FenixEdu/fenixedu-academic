<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<h3>
	Turmas
</h3>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<logic:present name="infoStudentShiftEnrolment" property="allowedClasses">
	<table width="70%" align="center">
		<tr>
			<th>
				Ano
			</th>
			<th>
				Turma
			</th>
			<th>
				Horário
			</th>
			<th>
				Selecção
			</th>
		</tr>
		<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" >
			<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
			<tr>
				<td>
					<bean:write name="curricularYear"/>
					º Ano
				</td>
				<td>
					<bean:write name="infoClass" property="nome"/>
				</td>
				<td>
					<html:link page="/studentShiftEnrolmentManager.do?method=viewClassTimeTable" paramId="classId" paramName="infoClass" paramProperty="idInternal" target="_blank" >
						Ver Horário
					</html:link>
				</td>
				<td>
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" transaction="true">
						Seleccionar Turma
					</html:link>
				</td>
			</tr>
		</logic:iterate>
		<%-- tdi-dev (bruno.lopes2edgar.goncalves) - what are you doing dave? %-->
<%--	    <logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" offset="2">
              <bean:define id="infoClassCurricularYear" name="infoClass" property="anoCurricular"/>
	      <logic:notEqual name="curricularYear" value="<%= infoClassCurricularYear.toString() %>">
		<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
		<h4>
		 <bean:write name="curricularYear"/>º Ano (B)
		</h4>
	      </logic:notEqual>
	      <html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" >
		<bean:write name="infoClass" property="nome"/>
	      </html:link>
	    </logic:iterate>
--%>
	</table>
	<br>
</logic:present>

<logic:notPresent name="infoStudentShiftEnrolment" property="lessons">
	
	Não está inscrito em nenhum turno...
</logic:notPresent>
<logic:present name="infoStudentShiftEnrolment" property="lessons">
	<bean:define id="infoLessons" name="infoStudentShiftEnrolment" property="lessons" />
	<h3>
		Horário:
	</h3>
	<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/>
</logic:present>
<logic:notPresent name="infoStudentShiftEnrolment" property="allowedClasses">
	Não existem turmas disponíveis.
</logic:notPresent>

