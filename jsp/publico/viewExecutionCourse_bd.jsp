<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="publico.infoExecCourse" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="publico.infoExecCourse" scope="session">

	<center> <font color='#034D7A' size='5'> <b>
		<bean:write name="publico.infoExecCourse" property="nome"/>
	</center> </b> </font>

	</br>
	</br>
	</br>

	<bean:message key="property.executionCourse.associatedCurricularCourses"/>
	<logic:present name="publico.infoCurricularCourses" scope="session">
			<table align="center" border='1' cellpadding='10'>
					<tr align="center">
						<td>
							<bean:message key="property.curricularCourse.name"/>
						</td>
						<td>
							<bean:message key="property.degree.initials"/>
						</td>
						<td>
							<bean:message key="property.curricularCourse.curricularYear"/>
						</td>
						<td>
							<bean:message key="property.curricularCourse.semester"/>
						</td>
					</tr>			
				<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
					<tr align="center">
						<td>
							<bean:write name="curricularCourse" property="name"/>
						</td>
						<td>
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
						</td>
						<td>
							<bean:write name="curricularCourse" property="curricularYear"/>
						</td>
						<td>
							<bean:write name="curricularCourse" property="semester"/>
						</td>
					</tr>
				</logic:iterate>
			</table>			
	</logic:present>
	
	<logic:notPresent name="publico.infoCurricularCourses" scope="session">
		<bean:message key="message.public.notfound.curricularCourses"/>
	</logic:notPresent>

	</br>
	</br>

	<bean:message key="property.executionCourse.curricularHours"/>
	<blockquote>
		<logic:notEqual name="publico.infoExecCourse" property="theoreticalHours" value="0">
			<bean:message key="property.executionCourse.theoreticalHours"/>
			<bean:write name="publico.infoExecCourse" property="theoreticalHours"/>
			</br>
		</logic:notEqual>

		<logic:notEqual name="publico.infoExecCourse" property="praticalHours" value="0">
			<bean:message key="property.executionCourse.practicalHours"/>
			<bean:write name="publico.infoExecCourse" property="praticalHours"/>
			</br>
		</logic:notEqual>

		<logic:notEqual name="publico.infoExecCourse" property="theoPratHours" value="0">
			<bean:message key="property.executionCourse.theoreticalPracticalHours"/>
			<bean:write name="publico.infoExecCourse" property="theoPratHours"/>
			</br>
		</logic:notEqual>
		
		<logic:notEqual name="publico.infoExecCourse" property="labHours" value="0">
			<bean:message key="property.executionCourse.labHours"/>
			<bean:write name="publico.infoExecCourse" property="labHours"/>		
		</logic:notEqual>
	</blockquote>

	</br>
	</br>

	<bean:message key="property.shifts"/>
	<logic:present name="publico.infoShifts" scope="session">
			<table align="center" border='1' cellpadding='10'>
					<tr align="center">
						<td rowspan="2">
							<bean:message key="property.turno"/>
						</td>
						<td colspan="4">
							<bean:message key="property.lessons"/>
						</td>
						<td rowspan="2">
							<bean:message key="property.classes"/>
						</td>
					</tr>
					<tr>
						<td>
							<bean:message key="property.lesson.weekDay"/>
						</td>
						<td>
							<bean:message key="property.lesson.beginning"/>
						</td>
						<td>
							<bean:message key="property.lesson.end"/>
						</td>
						<td>
							<bean:message key="property.lesson.room"/>
						</td>
					</tr>
			
				<logic:iterate id="infoShift" name="publico.infoShifts" scope="session">

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr>
							<td rowspan="<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>">
								<bean:write name="infoShift" property="infoShift.nome"/>
							</td>
							<td>
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td>
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
							</td>
							<td>
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td>
							<td rowspan=<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>>
								<logic:iterate id="infoClass" name="infoShift" property="infoClasses">
									<html:link page="/viewClassTimeTable.do" paramId="className" paramName="infoClass" paramProperty="nome">
										<bean:write name="infoClass" property="nome"/>
									</html:link> &nbsp;
								</logic:iterate>
							</td>
						</tr>
					</logic:iterate>
					

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr>
							<td>
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td>
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td>
						</tr>
					</logic:iterate>

				</logic:iterate>
			</table>			
	</logic:present>
	
	<logic:notPresent name="publico.infoShifts" scope="session">
		<bean:message key="message.public.notfound.infoShifts"/>
	</logic:notPresent>

	</br>
	</br>

	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/> 

	
</logic:present>	
		
