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
  <TR>
  <TH>
    Ano
</TH>
<TH>
  Turma
</TH>
<TH>
  Horário
</TH>
<TH>
  Selecção
</TH>
</TR>


			
				

		                  
<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" length="1" offset="0">
<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
<TR>
  <TD>
    <bean:write name="curricularYear"/>
    º Ano
  </TD>
  
  <TD>
    PRIMEIRO ITERATE <bean:write name="infoClass" property="nome"/>
  </TD>
  
  <TD>Ver Horário (adicionar link!)
  </TD>
  
  <TD>
    <html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" >
    Seleccionar Turma
    </html:link>
  </TD>
  
</TR>

</logic:iterate>




<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" offset="2">
<bean:define id="infoClassCurricularYear" name="infoClass" property="anoCurricular"/>
<logic:notEqual name="curricularYear" value="<%= infoClassCurricularYear.toString() %>">
<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
<h4>
<bean:write name="curricularYear"/>
º Ano (B)
</h4>
</logic:notEqual>
<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" >
<bean:write name="infoClass" property="nome"/>
</html:link>
</logic:iterate>

</table>
				<br>
			</logic:present>
			<logic:present name="infoLessons">
				<bean:size id="infoLessonsSize" name="infoLessons"/>
				<logic:equal name="infoLessonsSize" value="0">
					Não está inscrito em nenhum turno...
				</logic:equal>
				<logic:notEqual name="infoLessonsSize" value="0">
					<h3>Horário:</h3>
					<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/>
				</logic:notEqual>
			</logic:present>
			<logic:notPresent name="infoStudentShiftEnrolment" property="allowedClasses">
				Não existem turmas disponíveis.
			</logic:notPresent>



