<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoRoomOccupation" %>
<%@ page import="DataBeans.InfoViewExamByDayAndShift" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<br/>
<h3><bean:write name="<%= SessionConstants.EXAM_DATEANDTIME_STR %>" scope="request"/></h3>
<br/>

<logic:notPresent name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="request">
	<table align="center">
		<tr>
			<td>
				<span class="error"><bean:message key="message.exams.none.for.day.shift"/></span>
			</td>
		</tr>
	</table>
</logic:notPresent>


<logic:present name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="request">
	<table width="100%" border="1">
		<tr>
			<td class="listClasses-header">Disciplina</td>
			<td class="listClasses-header">Curso</td>		
			<td class="listClasses-header">Época</td>
			<td class="listClasses-header">Hora</td>
			<td class="listClasses-header">Salas</td>
			<td class="listClasses-header">Nº Alunos</td>	
			<td class="listClasses-header">Faltam X Lugares</td>	
		</tr>
		<logic:iterate id="infoViewExam" indexId="index" name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="request">
			<% int seatsReserved = 0; %>
			
			<logic:iterate id="infoScope" name="infoViewExam" property="infoExam.associatedCurricularCourseScope">
				<bean:define id="curricularYearOID" name="infoScope" property="infoCurricularSemester.infoCurricularYear.year"/>				
			</logic:iterate>
			
			<bean:define id="examID" name="infoViewExam" property="infoExam.idInternal"/> 
			<tr>
				<td class="listClasses">
					<%-- DISCIPLINA --%>
					<logic:iterate id="infoExecutionCourse" name="infoViewExam" property="infoExecutionCourses">
						<bean:write name="infoExecutionCourse" property="nome"/><br />
						<bean:define id="executionCourseOID" name="infoExecutionCourse" property="idInternal"/>
						<bean:define id="executionPeriodOID" name="infoExecutionCourse" property="infoExecutionPeriod.idInternal"/>												
					</logic:iterate>					
				</td>
				<td class="listClasses">
					<%-- CURSO --%>
					<logic:iterate id="infoDegree" name="infoViewExam" property="infoDegrees">
						<bean:write name="infoDegree" property="sigla"/><br />
						<bean:define id="executionDegreeOID" name="infoDegree" property="idInternal"/>
					</logic:iterate>
				</td>
				<td class="listClasses">
					<%-- EPOCA --%>
					
					<html:link page="<%= "/showExamsManagement.do?method=edit&amp;"
										+ SessionConstants.EXECUTION_COURSE_OID 
										+ "="
										+ pageContext.findAttribute("executionCourseOID")
										+ "&amp;"
										+ SessionConstants.EXECUTION_PERIOD_OID
										+ "="
										+ pageContext.findAttribute("executionPeriodOID")
										+ "&amp;"
										+ SessionConstants.EXECUTION_DEGREE_OID
										+ "="
										+ pageContext.findAttribute("executionDegreeOID") 
										+ "&amp;"
										+ SessionConstants.CURRICULAR_YEAR_OID
										+ "="
										+ pageContext.findAttribute("curricularYearOID")
										+ "&amp;"
										+ SessionConstants.EXAM_OID
										+ "="
										+ pageContext.findAttribute("examID") 
										+ "&amp;"
										+ SessionConstants.DATE
										+ "="
										+ pageContext.findAttribute("date") 
										+ "&amp;"
										+ SessionConstants.START_TIME
										+ "="
										+ pageContext.findAttribute("start_time")
										+ "&amp;"
										+ SessionConstants.END_TIME
										+ "="
										+ pageContext.findAttribute("end_time") %>"> 

						<bean:write name="infoViewExam" property="infoExam.season"/>
					</html:link>

				</td>
				<td class="listClasses">
					<%-- HORA --%>
					<%= ((InfoViewExamByDayAndShift) infoViewExam).getInfoExam().getBeginningHour() %>
					<br/>
					<%= ((InfoViewExamByDayAndShift) infoViewExam).getInfoExam().getEndHour() %>
				</td>
				<td class="listClasses">
					<%-- SALAS --%>
					<logic:notEmpty name="infoViewExam" property="infoExam.associatedRoomOccupation">
						<logic:iterate id="infoRoomOccupation" name="infoViewExam" property="infoExam.associatedRoomOccupation">
							<bean:write name="infoRoomOccupation" property="infoRoom.nome"/>; 
							<% seatsReserved += ((InfoRoomOccupation) infoRoomOccupation).getInfoRoom().getCapacidadeExame().intValue(); %>
						</logic:iterate> 
					</logic:notEmpty>
					<logic:empty name="infoViewExam" property="infoExam.associatedRoomOccupation">
						<bean:message key="message.exam.no.rooms"/>
					</logic:empty>
					
				</td>
				<td class="listClasses">
					<%-- N ALUNOS --%>
					<bean:write name="infoViewExam" property="numberStudentesAttendingCourse"/>
				</td>
				<td class="listClasses">
					<%-- N LUGARES A FALTAR --%>
					<%= ((InfoViewExamByDayAndShift) infoViewExam).getNumberStudentesAttendingCourse().intValue() - seatsReserved %>
				</td>
				
			</tr>
		</logic:iterate>
	</table>
</logic:present>






