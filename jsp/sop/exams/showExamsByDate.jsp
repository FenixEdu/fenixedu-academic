<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
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
			<th class="listClasses-header">Disciplina</th>
			<th class="listClasses-header">Curso</th>		
			<th class="listClasses-header">Época</th>
			<th class="listClasses-header">Hora</th>
			<th class="listClasses-header">Salas</th>
			<th class="listClasses-header">Nï¿½ Alunos</th>	
			<th class="listClasses-header">Faltam X Lugares</th>	
		</tr>
		<logic:iterate id="infoViewExam" indexId="index" name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="request">
				
			<logic:notEmpty name="infoViewExam" property="infoExam.associatedCurricularCourseScope">
				<logic:iterate id="infoScope" name="infoViewExam" property="infoExam.associatedCurricularCourseScope">
					<bean:define id="curricularYearOID" name="infoScope" property="infoCurricularSemester.infoCurricularYear.year"/>				
				</logic:iterate>			
			</logic:notEmpty>
			<logic:empty name="infoViewExam" property="infoExam.associatedCurricularCourseScope">
				<logic:iterate id="degreeModuleScope" name="infoViewExam" property="infoExam.writtenEvaluation.degreeModuleScopes">
					<bean:define id="curricularYearOID" name="degreeModuleScope" property="curricularYear"/>				
				</logic:iterate>			
			</logic:empty>
			
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
					<bean:write name="infoViewExam" property="availableRoomOccupation"/>
				</td>
				
			</tr>
		</logic:iterate>
	</table>
</logic:present>






