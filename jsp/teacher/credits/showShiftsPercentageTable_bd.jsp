<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants,  
				 java.util.Calendar, DataBeans.InfoLesson" %>
<bean:define id="infoShiftsPercentageList" name="infoShiftPercentageList" scope="request"/>

<html:form action="/executionCourseShiftsPercentageManager">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="executionCourseInternalCode"/>
	<table border="1">
		<tr>
			<th rowspan="2">Turno</th>
			<th colspan="4"> Aulas </th>
			<th rowspan="2">Lecciona?</th>
			<th rowspan="2">Percentagem em que lecciona</th>
			<th rowspan="2">Os meus colegas</th>			
		</tr>
			<td>
				Dia da Semana
			</td>
			<td>
				Inicio
			</td>
			<td>
				Fim
			</td>
			<td>
				Sala
			</td>
		<tr>
		</tr>
		<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
			<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
			<bean:size id="lessonsSize" name="infoShiftPercentage" property="infoLessons"/>
			<tr>
				<td>
					<bean:write name="infoShiftPercentage" property="shift.nome"/>			
				</td>

				<logic:equal name="lessonsSize" value="0">
					<td colspan="7"> Não tem aulas </td>
				</logic:equal>
				<logic:notEqual name="lessonsSize" value="0">
					<td colspan="4">
						<table>
						<logic:iterate id="infoLesson" name="infoShiftPercentage" property="infoLessons">
		                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
		                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
		                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
		                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
		                    <tr>
								<td>
									<bean:write name="infoLesson" property="diaSemana"/>
								</td>
								<td>
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
								</td>
								<td>
									<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
								</td>
								<td>
									<bean:write name="infoLesson" property="infoSala.nome"/>					
								</td>
		                    </tr>
						</logic:iterate>
						</table>
					</td>
					<td>
						<html:multibox property="shiftProfessorships">
							<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
						</html:multibox>
					</td>
					<td>
						<bean:define id="propertyName">
							percentage_<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
						</bean:define>
						<html:text property='<%= propertyName.toString() %>' size="4" value="" />%
						<bean:write name="availablePercentage"/>
						<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
					</td>
					<td>
						<logic:iterate id="teacherShiftPercentage"	name="infoShiftPercentage" property="teacherShiftPercentageList">
	<%--						<bean:write name="teacherShiftPercentage" property=""/>
		 					<bean:write name="teacherShiftPercentage" property="" --%>
						</logic:iterate>
					</td>
				</logic:notEqual>
			</tr>
		</logic:iterate>
	</table>
	
	<html:submit value="Submeter"/>
</html:form>