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
	<html:hidden property="objectCode"/>
	
	<table width="100%">
		<tr>
			<td rowspan="2" class="listClasses-header">Turno</td>
			<td rowspan="2" class="listClasses-header">Tipo</td>
			<td colspan="4" class="listClasses-header">Aulas</td>
			<td rowspan="2" class="listClasses-header">% que lecciona</td>
			<td rowspan="2" class="listClasses-header">Alterar?</td>			
			<td colspan="2" class="listClasses-header">Atribuidos</td>			
		</tr>
		<tr>
			<td class="listClasses-header">
				Dia da Semana
			</td>
			<td class="listClasses-header">
				Inicio
			</td>
			<td class="listClasses-header">
				Fim
			</td>
			<td class="listClasses-header">
				Sala
			</td>			
			<td class="listClasses-header">
				Professor - % que lecciona
			</td>
		</tr> 
		
		<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
			<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
			
			<bean:size id="lessonsSize" name="infoShiftPercentage" property="infoLessons" />	
										

				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
						<td class="listClasses" colspan="7"> Não tem aulas </td>
					</tr>
				</logic:equal>
				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="infoLesson" name="infoShiftPercentage" property="infoLessons" indexId="indexLessons" >
		        		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
				        <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
			            <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
			            <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
			            
			            <logic:equal name="indexLessons" value="0">
							<tr>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/>
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
							</td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="infoSala.nome"/>					
							</td>
							
							<td class="listClasses" rowspan="<%= lessonsSize %>">
						  		<bean:define id="propertyName">
									percentage_<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</bean:define>
								<html:text property='<%= propertyName.toString() %>' size="4"/> %
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<html:multibox property="shiftProfessorships">
									<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</html:multibox>
							</td>	
							
							<td class="listClasses" rowspan="<%= lessonsSize %>">					
								<logic:iterate id="teacherShiftPercentage"	name="infoShiftPercentage" property="teacherShiftPercentageList" indexId="indexPercentage">						
						    		<bean:write name="teacherShiftPercentage" property="infoProfessorship.infoTeacher.infoPerson.nome" />
			 						&nbsp;-&nbsp;<bean:write name="teacherShiftPercentage" property="percentage" />
			 						<br>
								</logic:iterate> 					
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td class="listClasses">
									<bean:write name="infoLesson" property="diaSemana"/>
								</td>
								<td class="listClasses">
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
								</td>
								<td class="listClasses">
									<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
								</td>
								<td class="listClasses">
									<bean:write name="infoLesson" property="infoSala.nome"/>					
								</td>						
							</tr>
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
		</logic:iterate>
	</table>
	<p>
	<html:submit styleClass="inputbutton" value="Submeter"/>
</html:form>