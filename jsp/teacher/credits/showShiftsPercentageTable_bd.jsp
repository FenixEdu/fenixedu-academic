<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants,  
				 java.util.Calendar, DataBeans.InfoLesson" %>

			 
<bean:define id="infoShiftsPercentageList" name="infoShiftPercentageList" scope="request" />

<span class="error"><html:errors/></span>

<html:form action="/executionCourseShiftsPercentageManager">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="executionCourseInternalCode"/>
	
	<table border="1">
		<tr>
			<th rowspan="2" align="center">Turno</th>
			<th colspan="4" align="center">Aulas</th>
			<th rowspan="2" align="center">% que lecciona</th>
			<th rowspan="2" align="center">Alterar?</th>			
			<th colspan="2" align="center">Atribuidos</th>			
		</tr>
		<tr>
			<td align="center">
				Dia da Semana
			</td>
			<td align="center">
				Inicio
			</td>
			<td align="center">
				Fim
			</td>
			<td align="center">
				Sala
			</td>			
			<td align="center">
				Professor - % que lecciona
			</td>
		</tr> 
		
		<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
			<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
			
			<bean:size id="lessonsSize" name="infoShiftPercentage" property="infoLessons" />	
										
			<tr>
				<td rowspan="<%= String.valueOf(lessonsSize.intValue()) %>">
					<bean:write name="infoShiftPercentage" property="shift.nome"/>			
				</td>

				<logic:equal name="lessonsSize" value="0">
					<td colspan="7" rowspan="<%= String.valueOf(lessonsSize.intValue()) %>"> Não tem aulas </td>
				</logic:equal>
				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="infoLesson" name="infoShiftPercentage" property="infoLessons" indexId="indexLessons" >
		        		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
				        <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
			            <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
			            <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
			            
			            <logic:equal name="indexLessons" value="0">
							<td align="center">
								<bean:write name="infoLesson" property="diaSemana"/>
							</td>
							<td align="center">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
							</td>
							<td align="center">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
							</td>
							<td align="center">
								<bean:write name="infoLesson" property="infoSala.nome"/>					
							</td>
							
							<td align="center" rowspan="<%= String.valueOf(lessonsSize.intValue()) %>">
						  		<bean:define id="propertyName">
									percentage_<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</bean:define>
								<html:text property='<%= propertyName.toString() %>' size="4" value="" />%
							</td>
							<td align="center" rowspan="<%= String.valueOf(lessonsSize.intValue()) %>">
								<html:multibox property="shiftProfessorships">
									<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</html:multibox>
							</td>	
							
							<td align="center" rowspan="<%= String.valueOf(lessonsSize.intValue()) %>">					
								<logic:iterate id="teacherShiftPercentage"	name="infoShiftPercentage" property="teacherShiftPercentageList" indexId="indexPercentage">						
						    		<bean:write name="teacherShiftPercentage" property="infoProfessorship.infoTeacher.infoPerson.nome" />
			 						&nbsp;-&nbsp;<bean:write name="teacherShiftPercentage" property="percentage" />
			 						<br>
								</logic:iterate> 					
							</td>						
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							</tr>
							<tr>
								<td align="center">
									<bean:write name="infoLesson" property="diaSemana"/>
								</td>
								<td align="center">
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
								</td>
								<td align="center">
									<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
								</td>
								<td align="center">
									<bean:write name="infoLesson" property="infoSala.nome"/>					
								</td>						
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
			</tr>			
		</logic:iterate>
	</table>
	<p>
	<html:submit value="Submeter"/>
</html:form>