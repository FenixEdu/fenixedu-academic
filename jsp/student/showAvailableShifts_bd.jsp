<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>
<h3>
<html:link page="/studentShiftEnrolmentManager.do?method=initializeShiftEnrolment">
	Visualizar Turmas e Horário
</html:link>
</h3>

<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<div align="center"><h3><bean:write name="infoStudentShiftEnrolment" property="infoStudent.infoPerson.nome"/></h3></div>

<logic:present name="infoStudentShiftEnrolment">
				
<div align="center"><table>
	<tr>
		<td class="listClasses-header">
			Turnos onde está inscrito:
		</td>
		<td class="listClasses-header">
			Tipo:
		</td>
		<td class="listClasses-header">
			Aulas:
		</td>
	</tr>
		
	<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment"  type="DataBeans.InfoShift">
	<tr>		
		<td class="listClasses">	
						<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
		</td>
		<td class="listClasses">
						<bean:write name="enroledShift" property="tipo.fullNameTipoAula"/>
		</td>
		<td class="listClasses">	
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
								<br/>
			</logic:iterate> 
		</td>
	</tr>		
	</logic:iterate>
</table></div>	


				
				<br /> 

<div align="center"><h3>Turnos onde se pode inscrever:</h3></div>
<div align="center"><table>
				<html:form action="studentShiftEnrolmentManager">
					<html:hidden property="method" value="validateAndConfirmShiftEnrolment"/>
					<bean:define id="index" value="0"/>
					<logic:notEmpty name="infoStudentShiftEnrolment" property="dividedList"	>							

					<logic:iterate name="infoStudentShiftEnrolment"  id="list" property="dividedList" indexId="courseIndex">

<tr>
	<td class="listClasses-header">
					<bean:write name="list" property="type"/>
	</td>
	<td class="listClasses-header">
				&nbsp;
	</td>	
	<td class="listClasses-header">
		Aulas:
	</td>
</tr>
						<logic:iterate name="list" id="sublist" property="list" indexId="groupIndex">

		<tr>
		<td class="listClasses">
			<bean:write name="sublist" property="type"/>
		</td>
		
							<logic:iterate name="sublist" id="shiftWithLessons" property="list" >
				<td class="listClasses">			
								<bean:define id="shift" name="shiftWithLessons" property="infoShift" />
								&nbsp
								<html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" />
								
								
				</td>		
				<td class="listClasses">				
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
					</td>			
							</logic:iterate>
					<bean:define id="index" value="<%=  (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				</tr>
						</logic:iterate>
					</logic:iterate>
					</logic:notEmpty>

</table></div>
<br/>
<br/>
			<div align="center"><html:submit value="Inscrever"/></div>
				</html:form> 
		


</logic:present>			
