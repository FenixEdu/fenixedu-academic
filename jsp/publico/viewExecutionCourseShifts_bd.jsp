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


<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">

	<center> <font color='#034D7A' size='5'> <b>
		<bean:message key="title.viewExecutionCourse.shifts"/>
		<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="nome"/>
	</center> </b> </font>

	</br>
	</br>
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
			
				<logic:iterate id="infoShift" name="publico.infoShifts" scope="session" indexId="infoShiftIndex">

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
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

										<bean:define id="className" name="infoClass" property="nome" toScope="request"/>
										<bean:define id="degreeInitials" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla" toScope="request"/>
										<bean:define id="nameDegreeCurricularPlan" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.name" toScope="request"/>
										<a href="viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?className=<%= request.getAttribute("className").toString() %>&amp;degreeInitials=<%= request.getAttribute("degreeInitials").toString() %>&amp;nameDegreeCurricularPlan=<%= request.getAttribute("nameDegreeCurricularPlan").toString() %>">
											<bean:write name="infoClass" property="nome" />
										</a>
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
	
</logic:present>
