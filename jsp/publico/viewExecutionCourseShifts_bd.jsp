<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>
</br>
	</br>
		<bean:parameter id="ePName" name="ePName" />
		<bean:parameter id="eYName" name="eYName" />

<logic:present name="publico.infoShifts" >
			<table align="center" cellspacing='0' cellpadding='10'>
					<tr class="timeTable_line" align="center">
						<td class="degreetablestd" rowspan="2">
							<bean:message key="property.turno"/>
						</td>
						<td class="degreetablestd" colspan="4">
							<bean:message key="property.lessons"/>
						</td>
						<td class="degreetablestd" rowspan="2">
							<bean:message key="property.classes"/>
						</td>
					</tr>
					<tr class="timeTable_line">
						<td class="degreetablestd">
							<bean:message key="property.lesson.weekDay"/>
						</td>
						<td class="degreetablestd">
							<bean:message key="property.lesson.beginning"/>
						</td>
						<td class="degreetablestd">
							<bean:message key="property.lesson.end"/>
						</td>
						<td class="degreetablestd">
							<bean:message key="property.lesson.room"/>
						</td>
					</tr>
			
				<logic:iterate id="infoShift" name="publico.infoShifts"  indexId="infoShiftIndex">

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td  class="degreetablestd" rowspan="<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>">
									<bean:write name="infoShift" property="infoShift.nome"/>
							</td>
							<td class="degreetablestd">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="degreetablestd">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="degreetablestd">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
							</td>
							
							<td class="degreetablestd">
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;ePName=<bean:write name="ePName" />&amp;eYName=<bean:write name="eYName" />'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td>

							<td  class="degreetablestd" rowspan=<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>>
								<logic:iterate id="infoClass" name="infoShift" property="infoClasses">

										<bean:define id="className" name="infoClass" property="nome" toScope="request"/>
										<bean:define id="degreeInitials" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla" toScope="request"/>
										<bean:define id="nameDegreeCurricularPlan" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.name" toScope="request"/>
										<a href="viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?className=<%= request.getAttribute("className").toString() %>&amp;ePName=<bean:write name="ePName" />&amp;eYName=<bean:write name="eYName" />&amp;degreeInitials=<%= request.getAttribute("degreeInitials").toString() %>&amp;nameDegreeCurricularPlan=<%= request.getAttribute("nameDegreeCurricularPlan").toString() %>">
											<bean:write name="infoClass" property="nome" /> <br/>
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
						<tr >
							<td class="degreetablestd">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="degreetablestd">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="degreetablestd">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="degreetablestd">
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;ePName=<bean:write name="ePName" />&amp;eYName=<bean:write name="eYName" />'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td class="degreetablestd">
						</tr>
					</logic:iterate>

				</logic:iterate>
			</table>			
	</logic:present>
	
	<logic:notPresent name="publico.infoShifts" >
		<bean:message key="message.public.notfound.infoShifts"/>
	</logic:notPresent>		



